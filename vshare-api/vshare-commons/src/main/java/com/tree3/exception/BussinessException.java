package com.tree3.exception;

/**
 * <p>
 * 业务异常
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/3/22 9:18 </p>
 */
public class BussinessException extends RuntimeException {

    public BussinessException() {
        super();
    }

    public BussinessException(String message) {
        super(message);
    }

    public BussinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BussinessException(Throwable cause) {
        super(cause);
    }

    protected BussinessException(String message, Throwable cause,
                                 boolean enableSuppression,
                                 boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
