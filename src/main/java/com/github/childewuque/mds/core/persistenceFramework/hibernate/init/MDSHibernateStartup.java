package com.github.childewuque.mds.core.persistenceFramework.hibernate.init;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/6/15 下午6:23
 * @modified By:
 */
@Configuration
@ConditionalOnExpression(value = "'${spring.mds.persistence-framework}'.equals('hibernate')")
@HibernateMDSBeanInitAnnotation
public class MDSHibernateStartup {
}