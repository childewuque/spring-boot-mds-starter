package com.github.childewuque.mds.core.persistenceFramework.hibernate;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/6/2 下午1:58
 * @modified By:
 */

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@ConditionalOnExpression(value = "'${spring.mds.persistence-framework}'.equals('hibernate')")
public class HibernateMDSConfig extends HibernateMDSAbastractConfig {

    public void addLocalSessionFactoryBeanProperties(final String ds, final BeanDefinitionBuilder beanDefinitionBuilder) {
//        beanDefinitionBuilder.addPropertyValue("entityInterceptor",null);
    }

}
