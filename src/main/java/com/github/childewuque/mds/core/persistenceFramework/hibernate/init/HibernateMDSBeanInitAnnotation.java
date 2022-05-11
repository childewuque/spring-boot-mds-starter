package com.github.childewuque.mds.core.persistenceFramework.hibernate.init;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/6/16 上午10:48
 * @modified By:
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({HibernateMDSBeanInitProcessor.class})
public @interface HibernateMDSBeanInitAnnotation {
}