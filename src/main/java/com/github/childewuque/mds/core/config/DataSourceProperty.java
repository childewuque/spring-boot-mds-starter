package com.github.childewuque.mds.core.config;

import org.springframework.boot.context.properties.NestedConfigurationProperty;

import javax.sql.DataSource;

/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/3/30 上午11:31
 * @modified By:
 */
public class DataSourceProperty {
    private String poolName;
    private Class<? extends DataSource> type;

    private String driverClassName;

    private String url;

    private String username;

    private String password;

    @NestedConfigurationProperty
    private HikariCpCommonConfig hikari = new HikariCpCommonConfig();

    private String secretKey;

    private String[] repoPackages;

    public String[] getRepoPackages() {
        return repoPackages;
    }

    public void setRepoPackages(String[] repoPackages) {
        this.repoPackages = repoPackages;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public Class<? extends DataSource> getType() {
        return type;
    }

    public void setType(Class<? extends DataSource> type) {
        this.type = type;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public HikariCpCommonConfig getHikari() {
        return hikari;
    }

    public void setHikari(HikariCpCommonConfig hikari) {
        this.hikari = hikari;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
