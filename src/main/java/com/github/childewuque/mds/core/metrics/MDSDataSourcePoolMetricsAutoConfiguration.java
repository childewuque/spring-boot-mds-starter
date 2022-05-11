package com.github.childewuque.mds.core.metrics;

import java.util.*;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import com.github.childewuque.mds.core.config.MDSConstant;
import com.github.childewuque.mds.core.datasource.MDSRoutingDataSource;
import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.export.simple.SimpleMetricsExportAutoConfiguration;
import org.springframework.boot.actuate.metrics.jdbc.DataSourcePoolMetrics;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceUnwrapper;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.log.LogMessage;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for metrics on all available
 * {@link DataSource datasources}.
 *
 * @author Stephane Nicoll
 * @since 2.0.0
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter({MetricsAutoConfiguration.class, DataSourceAutoConfiguration.class,
        SimpleMetricsExportAutoConfiguration.class})
@ConditionalOnClass({DataSource.class, MeterRegistry.class})
@ConditionalOnBean({DataSource.class, MeterRegistry.class})
public class MDSDataSourcePoolMetricsAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnBean(DataSourcePoolMetadataProvider.class)
    static class DataSourcePoolMetadataMetricsConfiguration {

        private static final String DATASOURCE_SUFFIX = "dataSource";

        @Autowired
        void bindDataSourcesToRegistry(Map<String, DataSource> dataSources, MeterRegistry registry,
                                       ObjectProvider<DataSourcePoolMetadataProvider> metadataProviders) {
            List<DataSourcePoolMetadataProvider> metadataProvidersList = metadataProviders.stream()
                    .collect(Collectors.toList());
            dataSources.forEach(
                    (name, dataSource) -> bindDataSourceToRegistry(name, dataSource, metadataProvidersList, registry));
        }

        private void bindDataSourceToRegistry(String beanName, DataSource dataSource,
                                              Collection<DataSourcePoolMetadataProvider> metadataProviders, MeterRegistry registry) {
            String dataSourceName = getDataSourceName(beanName);
            new DataSourcePoolMetrics(dataSource, metadataProviders, dataSourceName, Collections.emptyList())
                    .bindTo(registry);
        }

        /**
         * Get the name of a DataSource based on its {@code beanName}.
         *
         * @param beanName the name of the data source bean
         * @return a name for the given data source
         */
        private String getDataSourceName(String beanName) {
            if (beanName.length() > DATASOURCE_SUFFIX.length()
                    && StringUtils.endsWithIgnoreCase(beanName, DATASOURCE_SUFFIX)) {
                return beanName.substring(0, beanName.length() - DATASOURCE_SUFFIX.length());
            }
            return beanName;
        }

    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(HikariDataSource.class)
    static class HikariDataSourceMetricsConfiguration {

        private static final String itemHost = "host";
        private static final String itemPort = "port";
        private static final String itemDB = "db";

        String mdsName = "";
        String mdsVersion = "";

        private Logger logger = LoggerFactory.getLogger(HikariDataSourceMetricsConfiguration.class);

        private final MeterRegistry registry;

        HikariDataSourceMetricsConfiguration(MeterRegistry registry) {
            this.registry = registry;
        }

        @Autowired
        void bindMetricsRegistryToHikariDataSources(Collection<DataSource> dataSources) {

            Package mdsInfo = MDSDataSourcePoolMetricsAutoConfiguration.class.getPackage();
            mdsName = mdsInfo.getImplementationTitle();
            mdsVersion = mdsInfo.getImplementationVersion();
            logger.info("MDS-hikari cp metric mdsName:{},mdsVersion:{}", mdsName, mdsVersion);

            for (DataSource dataSource : dataSources) {
                MDSRoutingDataSource mdsRoutingDataSource = (MDSRoutingDataSource) dataSource;
                Map<String, DataSource> allDatasources = mdsRoutingDataSource.getCurrentDataSources();
                for (String dsName : allDatasources.keySet()) {
                    if (MDSConstant.shardingDatasourceName.equals(dsName)) {
                        continue;
                    }
                    HikariDataSource hikariDataSource = DataSourceUnwrapper.unwrap(allDatasources.get(dsName), HikariDataSource.class);
                    if (hikariDataSource != null) {
                        bindMetricsRegistryToHikariDataSource(hikariDataSource);
                    }
                }
            }
        }

        private void bindMetricsRegistryToHikariDataSource(HikariDataSource hikari) {
            if (hikari.getMetricRegistry() == null && hikari.getMetricsTrackerFactory() == null) {
                try {
                    hikari.setMetricsTrackerFactory(
                            new MDSMicrometerMetricsTrackerFactory(
                                    this.registry,
                                    hikari.getUsername(),
                                    hikari.getJdbcUrl(),
                                    getUrlItem(hikari.getJdbcUrl(), itemHost),
                                    getUrlItem(hikari.getJdbcUrl(), itemPort),
                                    getUrlItem(hikari.getJdbcUrl(), itemDB),
                                    mdsName,
                                    mdsVersion
                            ));
                } catch (Exception ex) {
                    logger.warn(LogMessage.format("Failed to bind Hikari metrics: %s", ex.getMessage()).toString());
                }
            }
        }

        private String getUrlItem(String jdbcUrl, String itemName) {
            String itemValue = "";
            try {
                if (StringUtils.isNotBlank(jdbcUrl)) {
                    if (jdbcUrl.contains("jdbc:mysql")) {
                        String[] items = jdbcUrl.split(":");
                        switch (itemName) {
                            case itemHost:
                                itemValue = items[2].substring(2);
                                break;
                            case itemPort:
                                itemValue = items[3].split("/")[0];
                                break;
                            case itemDB:
                                itemValue = items[3].split("/")[1].split("\\?")[0];
                                break;
                            default:
                                logger.info("MDS hikari cp no the itemName,itemName={}", itemName);
                        }
                    } else if (jdbcUrl.contains("jdbc:oracle")) {
                        switch (itemName) {
                            case itemHost:
                                itemValue = getOracleValueByKeyInUrl(jdbcUrl, itemHost);
                                if (StringUtils.isBlank(itemValue)) {
                                    itemValue = jdbcUrl.split(":")[3].substring(3);
                                }
                                break;
                            case itemPort:
                                itemValue = getOracleValueByKeyInUrl(jdbcUrl, itemPort);
                                if (StringUtils.isBlank(itemValue)) {
                                    itemValue = jdbcUrl.split(":")[4].split("/")[0];
                                }
                                break;
                            case itemDB:
                                itemValue = getOracleValueByKeyInUrl(jdbcUrl, "SERVICE_NAME");
                                if (StringUtils.isBlank(itemValue)) {
                                    itemValue = jdbcUrl.split(":")[4].split("/")[1];
                                }
                                break;
                            default:
                                logger.info("MDS hikari cp no the itemName,itemName={}", itemName);
                        }
                    } else {
                        logger.info("MDS hikari cp jdbcUrl is not mysql or oracle,jdbcUrl={}", jdbcUrl);
                    }
                }
            } catch (Exception e) {
                logger.error("MDS-hikari cp metric error,Please use the standard jdbc URL suggested by DBA, jdbcUrl={},itemName={}", jdbcUrl, itemName, e);
            }
            return itemValue;
        }

        private String getOracleValueByKeyInUrl(String url, String key) {
            String[] dbItems = url.split(key.toUpperCase() + " =");
            if (dbItems.length < 2) {
                dbItems = url.split(key.toUpperCase() + "=");
                if (dbItems.length < 2) {
                    dbItems = url.split(key + " =");
                    if (dbItems.length < 2) {
                        dbItems = url.split(key + "=");
                    }
                }
            }
            if (dbItems.length >= 2) {
                return dbItems[1].split("\\)")[0].trim();
            } else {
                return null;
            }
        }
    }

}
