package com.github.childewuque.mds.core.persistenceFramework.jdbc;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/4/19 下午1:48
 * @modified By:
 */
@Configuration
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@ConditionalOnExpression(value = "'${spring.mds.persistence-framework}'.equals('jdbc')")
public class JdbcManagerFactory extends CommonTransactionManagerFactory {
}
