package com.tree3.config.interceptor;


import com.tree3.annotation.Token;
import com.tree3.constants.RedisPrefix;
import com.tree3.pojo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * <p>
 * 处理Token注解
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/3/15 8:34 </p>
 */
@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    private RedisTemplate redisTemplate;

    @Autowired
    public TokenInterceptor(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("@Token中的第三个参数：{}",handler.toString());
        if (handler instanceof HandlerMethod) {
            Method method = ((HandlerMethod) handler).getMethod();
            boolean annotationPresent = method.isAnnotationPresent(Token.class);

            if (annotationPresent) {
                String token = request.getParameter("token");
                if (StringUtils.isEmpty(token)) {
                    throw new RuntimeException("token异常");
                }

                String tokenKey = RedisPrefix.TOKEN_KEY + token;
                User user = (User) redisTemplate.opsForValue().get(tokenKey);
                if (user == null) {
                    throw new RuntimeException("token失效");
                }

                request.setAttribute("token", token);
                request.setAttribute("user", user);
            }
        }

        return true;
    }
}
