package com.github.childewuque.mds.core.annotation;


import java.lang.annotation.*;

/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/3/30 下午2:21
 * @modified By:
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DS {
    String value();
}