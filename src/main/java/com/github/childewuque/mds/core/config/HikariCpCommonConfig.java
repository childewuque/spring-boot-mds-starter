/**
 * Copyright © 2018 organization baomidou
 * <pre>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <pre/>
 */
package com.github.childewuque.mds.core.config;

import com.zaxxer.hikari.HikariConfig;

import java.util.Properties;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/3/30 下午2:21
 * @modified By:
 */

public class HikariCpCommonConfig {

    private static final long CONNECTION_TIMEOUT = SECONDS.toMillis(10);
    private static final long VALIDATION_TIMEOUT = SECONDS.toMillis(5);
    private static final long IDLE_TIMEOUT = MINUTES.toMillis(10);
    private static final long MAX_LIFETIME = MINUTES.toMillis(30);
    private static final int DEFAULT_POOL_SIZE = 1;

    private String username;
    private String password;
    private String driverClassName;
    private String jdbcUrl;
    private String poolName;

    private String catalog;
    private Long connectionTimeout;
    private Long validationTimeout;
    private Long idleTimeout;
    private Long leakDetectionThreshold;
    private Long maxLifetime;
    private Integer maxPoolSize;
    private Integer minIdle;

    private Long initializationFailTimeout;
    private String connectionInitSql;
    private String connectionTestQuery;
    private String dataSourceClassName;
    private String dataSourceJndiName;
    private String transactionIsolationName;
    private Boolean isAutoCommit;
    private Boolean isReadOnly;
    private Boolean isIsolateInternalQueries;
    private Boolean isRegisterMbeans;
    private Boolean isAllowPoolSuspension;
    private Properties dataSourceProperties;
    private Properties healthCheckProperties;

    public long getConnectionTimeout() {
        return connectionTimeout == null ? CONNECTION_TIMEOUT : connectionTimeout;
    }

    public void setConnectionTimeout(Long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public long getValidationTimeout() {
        return validationTimeout == null ? VALIDATION_TIMEOUT : validationTimeout;
    }

    public void setValidationTimeout(Long validationTimeout) {
        this.validationTimeout = validationTimeout;
    }

    public long getIdleTimeout() {
        return idleTimeout == null ? IDLE_TIMEOUT : idleTimeout;
    }

    public void setIdleTimeout(Long idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public Long getLeakDetectionThreshold() {
        return leakDetectionThreshold;
    }

    public void setLeakDetectionThreshold(Long leakDetectionThreshold) {
        this.leakDetectionThreshold = leakDetectionThreshold;
    }

    public long getMaxLifetime() {
        return maxLifetime == null ? MAX_LIFETIME : maxLifetime;
    }

    public void setMaxLifetime(Long maxLifetime) {
        this.maxLifetime = maxLifetime;
    }

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Long getInitializationFailTimeout() {
        return initializationFailTimeout;
    }

    public void setInitializationFailTimeout(Long initializationFailTimeout) {
        this.initializationFailTimeout = initializationFailTimeout;
    }

    public String getConnectionInitSql() {
        return connectionInitSql;
    }

    public void setConnectionInitSql(String connectionInitSql) {
        this.connectionInitSql = connectionInitSql;
    }

    public String getConnectionTestQuery() {
        return connectionTestQuery;
    }

    public void setConnectionTestQuery(String connectionTestQuery) {
        this.connectionTestQuery = connectionTestQuery;
    }

    public String getDataSourceClassName() {
        return dataSourceClassName;
    }

    public void setDataSourceClassName(String dataSourceClassName) {
        this.dataSourceClassName = dataSourceClassName;
    }

    public String getDataSourceJndiName() {
        return dataSourceJndiName;
    }

    public void setDataSourceJndiName(String dataSourceJndiName) {
        this.dataSourceJndiName = dataSourceJndiName;
    }

    public String getTransactionIsolationName() {
        return transactionIsolationName;
    }

    public void setTransactionIsolationName(String transactionIsolationName) {
        this.transactionIsolationName = transactionIsolationName;
    }

    public Boolean getAutoCommit() {
        return isAutoCommit;
    }

    public void setAutoCommit(Boolean autoCommit) {
        isAutoCommit = autoCommit;
    }

    public Boolean getReadOnly() {
        return isReadOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        isReadOnly = readOnly;
    }

    public Boolean getIsolateInternalQueries() {
        return isIsolateInternalQueries;
    }

    public void setIsolateInternalQueries(Boolean isolateInternalQueries) {
        isIsolateInternalQueries = isolateInternalQueries;
    }

    public Boolean getRegisterMbeans() {
        return isRegisterMbeans;
    }

    public void setRegisterMbeans(Boolean registerMbeans) {
        isRegisterMbeans = registerMbeans;
    }

    public Boolean getAllowPoolSuspension() {
        return isAllowPoolSuspension;
    }

    public void setAllowPoolSuspension(Boolean allowPoolSuspension) {
        isAllowPoolSuspension = allowPoolSuspension;
    }

    public Properties getDataSourceProperties() {
        return dataSourceProperties;
    }

    public void setDataSourceProperties(Properties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    public Properties getHealthCheckProperties() {
        return healthCheckProperties;
    }

    public void setHealthCheckProperties(Properties healthCheckProperties) {
        this.healthCheckProperties = healthCheckProperties;
    }

    public static int getDefaultPoolSize() {
        return DEFAULT_POOL_SIZE;
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

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    /**
     * 转换为HikariCP配置
     *
     * @param hikariCpConfig 全局配置
     * @return HikariCP配置
     */
    public HikariConfig toHikariConfig(HikariCpCommonConfig hikariCpConfig) {
        HikariConfig config = new HikariConfig();

        String tempCatalog = catalog == null ? hikariCpConfig.getCatalog() : catalog;
        if (tempCatalog != null) {
            config.setCatalog(tempCatalog);
        }

        Long tempConnectionTimeout = connectionTimeout == null ? hikariCpConfig.getConnectionTimeout() : connectionTimeout;
        if (tempConnectionTimeout != null) {
            config.setConnectionTimeout(tempConnectionTimeout);
        }

        Long tempValidationTimeout = validationTimeout == null ? hikariCpConfig.getValidationTimeout() : validationTimeout;
        if (tempValidationTimeout != null) {
            config.setValidationTimeout(tempValidationTimeout);
        }

        Long tempIdleTimeout = idleTimeout == null ? hikariCpConfig.getIdleTimeout() : idleTimeout;
        if (tempIdleTimeout != null) {
            config.setIdleTimeout(tempIdleTimeout);
        }

        Long tempLeakDetectionThreshold = leakDetectionThreshold == null ? hikariCpConfig.getLeakDetectionThreshold() : leakDetectionThreshold;
        if (tempLeakDetectionThreshold != null) {
            config.setLeakDetectionThreshold(tempLeakDetectionThreshold);
        }

        Long tempMaxLifetime = maxLifetime == null ? hikariCpConfig.getMaxLifetime() : maxLifetime;
        if (tempMaxLifetime != null) {
            config.setMaxLifetime(tempMaxLifetime);
        }

        Integer tempMaxPoolSize = maxPoolSize == null ? hikariCpConfig.getMaxPoolSize() : maxPoolSize;
        if (tempMaxPoolSize != null && tempMaxPoolSize > -1) {
            config.setMaximumPoolSize(tempMaxPoolSize);
        }

        Integer tempMinIdle = minIdle == null ? hikariCpConfig.getMinIdle() : getMinIdle();
        if (tempMinIdle != null && tempMinIdle > -1) {
            config.setMinimumIdle(tempMinIdle);
        }

        Long tempInitializationFailTimeout = initializationFailTimeout == null ? hikariCpConfig.getInitializationFailTimeout() : initializationFailTimeout;
        if (tempInitializationFailTimeout != null && tempInitializationFailTimeout > 1) {
            config.setInitializationFailTimeout(tempInitializationFailTimeout);
        }

        String tempConnectionInitSql = connectionInitSql == null ? hikariCpConfig.getConnectionInitSql() : connectionInitSql;
        if (tempConnectionInitSql != null) {
            config.setConnectionInitSql(tempConnectionInitSql);
        }

        String tempConnectionTestQuery = connectionTestQuery == null ? hikariCpConfig.getConnectionTestQuery() : connectionTestQuery;
        if (tempConnectionTestQuery != null) {
            config.setConnectionTestQuery(tempConnectionTestQuery);
        }

        String tempDataSourceClassName = dataSourceClassName == null ? hikariCpConfig.getDataSourceClassName() : dataSourceClassName;
        if (tempDataSourceClassName != null) {
            config.setDataSourceClassName(tempDataSourceClassName);
        }

        String tempDataSourceJndiName = dataSourceJndiName == null ? hikariCpConfig.getDataSourceJndiName() : dataSourceJndiName;
        if (tempDataSourceJndiName != null) {
            config.setDataSourceJNDI(tempDataSourceJndiName);
        }

        String tempTransactionIsolationName = transactionIsolationName == null ? hikariCpConfig.getTransactionIsolationName() : transactionIsolationName;
        if (tempTransactionIsolationName != null) {
            config.setTransactionIsolation(tempTransactionIsolationName);
        }

        Boolean tempAutoCommit = isAutoCommit == null ? hikariCpConfig.getAutoCommit() : isAutoCommit;
        if (tempAutoCommit != null && tempAutoCommit.equals(Boolean.FALSE)) {
            config.setAutoCommit(false);
        }

        Boolean tempReadOnly = isReadOnly == null ? hikariCpConfig.getReadOnly() : isReadOnly;
        if (tempReadOnly != null) {
            config.setReadOnly(tempReadOnly);
        }

        Boolean tempIsolateInternalQueries = isIsolateInternalQueries == null ? hikariCpConfig.getIsolateInternalQueries() : isIsolateInternalQueries;
        if (tempIsolateInternalQueries != null) {
            config.setIsolateInternalQueries(tempIsolateInternalQueries);
        }

        Boolean tempRegisterMbeans = isRegisterMbeans == null ? hikariCpConfig.getRegisterMbeans() : isRegisterMbeans;
        if (tempRegisterMbeans != null) {
            config.setRegisterMbeans(tempRegisterMbeans);
        }

        Boolean tempAllowPoolSuspension = isAllowPoolSuspension == null ? hikariCpConfig.getAllowPoolSuspension() : isAllowPoolSuspension;
        if (tempAllowPoolSuspension != null) {
            config.setAllowPoolSuspension(tempAllowPoolSuspension);
        }

        Properties tempDataSourceProperties = dataSourceProperties == null ? hikariCpConfig.getDataSourceProperties() : dataSourceProperties;
        if (tempDataSourceProperties != null) {
            config.setDataSourceProperties(tempDataSourceProperties);
        }

        Properties tempHealthCheckProperties = healthCheckProperties == null ? hikariCpConfig.getHealthCheckProperties() : healthCheckProperties;
        if (tempHealthCheckProperties != null) {
            config.setHealthCheckProperties(tempHealthCheckProperties);
        }
        return config;
    }
}
