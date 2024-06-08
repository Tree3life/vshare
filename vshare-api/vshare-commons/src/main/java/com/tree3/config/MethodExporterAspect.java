package com.tree3.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * <p>
 * 处理被@MethodExporter标注的方法
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/3/21 19:28 </p>
 */
@Slf4j
@Aspect
//@Component
public class MethodExporterAspect {

//    @Around
    @Around("@annotation(com.tree3.config.MethodExporter)")
    public Object onMethodExporterAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        //前置通知：记录
        //方法返回值 + 全限定方法名
        JoinPoint.StaticPart staticPart = joinPoint.getStaticPart();
        //参数相关信息
        Object[] args = joinPoint.getArgs();
        //json工具
        ObjectMapper jsonMapper = new ObjectMapper();

        //记录日志
        if (log.isInfoEnabled()) {
            StringBuilder builder = new StringBuilder();
            Arrays.stream(args).forEach(item -> {
                builder.append(item.getClass().toString()).append("==>").append(item).append("\n");
            });

            log.info("\n" + staticPart.getSignature() + " 参数列表:\n{}", builder.toString());
        }

        String jsonArgs = jsonMapper.writeValueAsString(joinPoint.getArgs());
        log.info("{}方法接收到的参数为:{}", staticPart, jsonArgs);
        //调用原方法
        Object proceed = joinPoint.proceed();

        //记录返回结果
        String jsonResult = "nullStr";
        if (proceed != null) {
            jsonResult = jsonMapper.writeValueAsString(proceed);
        }
        log.info(staticPart.getSignature() + " 处理结果:{}", jsonResult);
        return proceed;
    }
}
