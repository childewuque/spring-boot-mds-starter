package com.github.childewuque.mds.core.persistenceFramework.jpa;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import java.util.Map;

/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/4/15 下午3:09
 * @modified By:
 */
@SuppressWarnings("all")
@Configuration
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnExpression(value = "'${spring.mds.persistence-framework}'.equals('jpa')")
public class JpaMDSConfig extends JpaMDSAbastractConfig {
    public void addEntityManagerFactoryBeanProperties(final String ds, final LocalContainerEntityManagerFactoryBean fb) {
    }

    public void addJpaRepositoriesProperties(final String ds, final Map<String, Object> repoMap) {
    }
}
