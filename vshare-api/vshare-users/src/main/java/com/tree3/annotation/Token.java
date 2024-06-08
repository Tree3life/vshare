package com.tree3.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * token标记
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/3/15 8:26 </p>
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Token {
}
