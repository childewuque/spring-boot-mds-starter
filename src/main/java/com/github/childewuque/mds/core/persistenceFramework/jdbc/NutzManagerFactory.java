package com.github.childewuque.mds.core.persistenceFramework.jdbc;

import com.github.childewuque.mds.core.persistenceFramework.MDSDatasourceFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/4/19 下午1:48
 * @modified By:
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@ConditionalOnExpression(value = "'${spring.mds.persistence-framework}'.equals('nutz')")
public class NutzManagerFactory extends MDSDatasourceFactory {
}
