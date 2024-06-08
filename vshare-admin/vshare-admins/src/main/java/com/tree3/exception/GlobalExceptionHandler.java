package com.tree3.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

//前后端分离异常处理
@ControllerAdvice  //给controller增加一些附加操作
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    //自定义方法 处理所有controller异常
    @ExceptionHandler //用来处理controller异常;有异常发生时才处理
    @ResponseBody
    public Map<String, String> exception(Exception exception) {
        log.info("ex: {}", exception.getMessage());
        HashMap<String, String> result = new HashMap<>();
        result.put("msg", exception.getMessage());
        return result;
    }

}