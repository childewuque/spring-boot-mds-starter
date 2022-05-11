package com.github.childewuque.mds.core.datasource;


import com.github.childewuque.mds.core.config.DataSourceProperty;
import com.github.childewuque.mds.core.config.HikariCpCommonConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;


public class HikariDataSourceBuilder {
    private HikariCpCommonConfig hikariCpConfig;

    public HikariDataSourceBuilder(HikariCpCommonConfig hikariCpConfig) {
        this.hikariCpConfig = hikariCpConfig;
    }

    public DataSource createDataSource(DataSourceProperty dataSourceProperty) throws Exception {
        HikariConfig config = dataSourceProperty.getHikari().toHikariConfig(hikariCpConfig);
        config.setUsername(dataSourceProperty.getUsername());
        config.setPassword(dataSourceProperty.getPassword());
        config.setJdbcUrl(dataSourceProperty.getUrl());
        config.setPoolName(dataSourceProperty.getPoolName());
        String driverClassName = dataSourceProperty.getDriverClassName();
        if (!StringUtils.isEmpty(driverClassName)) {
            config.setDriverClassName(driverClassName);
        }
        return new HikariDataSource(config);
    }

}
