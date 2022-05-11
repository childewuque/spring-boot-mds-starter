package com.github.childewuque.mds.core.persistenceFramework.jdbc;

import com.github.childewuque.mds.core.persistenceFramework.MDSDatasourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/4/19 下午1:48
 * @modified By:
 */

public abstract class CommonTransactionManagerFactory extends MDSDatasourceFactory {
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
