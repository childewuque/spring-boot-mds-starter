package com.github.childewuque.mds.core.config;

import com.github.childewuque.mds.core.datasource.strategy.DataSourceStrategys;
import com.github.childewuque.mds.core.datasource.strategy.LBStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/3/30 上午11:36
 * @modified By:
 */

@ConfigurationProperties(prefix = DataSourceProperties.PREFIX)
public class DataSourceProperties {

    public static final String PREFIX = "spring.mds";
    private String defaultDatasource = "";
    private String persistenceFramework = "" ;
    @NestedConfigurationProperty
//    private HikariCpCommonConfig hikari = new HikariCpCommonConfig();
    private HikariCpCommonConfig hikari ;
    private Map<String, DataSourceProperty> datasource = new LinkedHashMap<>();
    private Class<? extends DataSourceStrategys> strategy = LBStrategy.class;

    public String getPersistenceFramework() {
        return persistenceFramework;
    }

    public void setPersistenceFramework(String persistenceFramework) {
        this.persistenceFramework = persistenceFramework;
    }

    public String getDefaultDatasource() {
        return defaultDatasource;
    }

    public void setDefaultDatasource(String defaultDatasource) {
        this.defaultDatasource = defaultDatasource;
    }

    public Map<String, DataSourceProperty> getDatasource() {
        return datasource;
    }

    public void setDatasource(Map<String, DataSourceProperty> datasource) {
        this.datasource = datasource;
    }

    public Class<? extends DataSourceStrategys> getStrategy() {
        return strategy;
    }

    public void setStrategy(Class<? extends DataSourceStrategys> strategy) {
        this.strategy = strategy;
    }

    public HikariCpCommonConfig getHikari() {
        return hikari;
    }

    public void setHikari(HikariCpCommonConfig hikari) {
        this.hikari = hikari;
    }

}
