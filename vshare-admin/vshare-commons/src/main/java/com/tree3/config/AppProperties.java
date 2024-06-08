package com.tree3.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description 本框架中的自定义配置
 * 使用示例
 *   1.以实例形式使用
 *     @Autowired
 *     private AppProperties appProperties;
 *
 *   2.EL表达式形式使用
 *     @Value("${app.effectiveTime}")
 *     private long effectiveTime;
 *
 * @Author: Jinhui
 * @Date 2021/12/30 14:12
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    /**
     * JWT 密钥
     */
    private String jwtSecret;

    /**
     * JWT Token有效时间
     */
    private long effectiveTime;

    /**
     * 加密盐值
     */
    private String salt;

    /**
     * 密码加/解密md5散列次数
     */
    private Integer hashIterations;

    /**
     * 开启全局异常处理捕获异常后打印堆栈信息
     */
    private boolean showCase;

}
