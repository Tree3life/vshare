package com.tree3.gateway.filter.factory;

import com.tree3.constants.RedisPrefix;
import com.tree3.exception.IllegalTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;


/**
 * <p>
 * 自定义token工厂， 自定义token过滤器，用于校验请求中的token
 * 自定义网关过滤器---继承自 AbstractGatewayFilterFactory，
 * 参考StripPrefixGatewayFilterFactory实现
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/3/6 17:07 </p>
 */
//@configuration 配置
//@Component  在工厂中创建对象   使用 filtes -Token
@Component  //在工厂中创建对象
public class DiyTokenGatewayFilterFactory extends AbstractGatewayFilterFactory<DiyTokenGatewayFilterFactory.Config> {

    private static final Logger log = LoggerFactory.getLogger(DiyTokenGatewayFilterFactory.class);

    private RedisTemplate redisTemplate;

    @Autowired
    public DiyTokenGatewayFilterFactory(RedisTemplate redisTemplate) {
        super(Config.class);
        this.redisTemplate = redisTemplate;
    }

    /**
     * 主要方法：编写过滤器业务
     *
     * @param config 基于当前类中的Config创建对象，包含 request response session 等信息
     * @return
     */
    @Override
    public GatewayFilter apply(Config config) {
        // servlet service httpServletRequest  httpServletResponse 传统web springmvc   springwebflux new web模型
        // filter   request response filterChain.dofilter(request,response)
        return new GatewayFilter() {
            @Override
            //参数1: exchange 交换机
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                log.info("config required token: {}", config.requiredToken);
                log.info("config name: {}", config.name);
                if (config.requiredToken) {
                    //1.获取requerst中的token信息
                    ServerHttpRequest request = exchange.getRequest();
                    List<String> requestParamsList = request.getQueryParams().get("token");
                    if (requestParamsList == null) {
                        throw new IllegalTokenException("非法令牌!");
                    }

                    String token = requestParamsList.get(0);
                    log.info("token:{}", token);
                    //2.根据token信息去redis获取
                    if (!redisTemplate.hasKey(RedisPrefix.TOKEN_KEY + token))
                        throw new IllegalTokenException("不合法的令牌!");
                }
                return chain.filter(exchange);
            }
        };
    }


    /**
     * 用来配置将使用本filter时指定值赋值给Config中哪个属性
     *
     * @return
     */
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("requiredToken", "name");
    }


    /**
     * 自定义的配置类，用于从项目的yml文件中读取自定义配置
     */
    public static class Config {
        private boolean requiredToken;  //false
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isRequiredToken() {
            return requiredToken;
        }

        public void setRequiredToken(boolean requiredToken) {
            this.requiredToken = requiredToken;
        }
    }


}
