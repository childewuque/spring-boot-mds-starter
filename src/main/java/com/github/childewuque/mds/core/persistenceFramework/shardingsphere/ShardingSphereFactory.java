package com.github.childewuque.mds.core.persistenceFramework.shardingsphere;

import com.github.childewuque.mds.core.config.MDSConstant;
import com.github.childewuque.mds.core.config.DataSourceProperties;
import com.github.childewuque.mds.core.config.ShardingsphereProperties;
import com.github.childewuque.mds.core.datasource.MDSRoutingDataSource;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.RuleConfiguration;
import org.apache.shardingsphere.sharding.algorithm.config.AlgorithmProvidedShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.spi.ShardingAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AbstractAdvisingBeanPostProcessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/4/21 下午4:38
 * @modified By:
 */

@Configuration
@ComponentScan("org.apache.shardingsphere.spring.boot.converter")
@ConditionalOnProperty(prefix = "spring.shardingsphere", name = "enabled", havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties({ShardingsphereProperties.class})
public class ShardingSphereFactory implements InitializingBean {
    private Logger logger = LoggerFactory.getLogger(ShardingSphereFactory.class);
    @Autowired
    DataSource dataSource;
    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Value("${spring.shardingsphere.ds}")
    String[] shardingDS;
    @Autowired
    private ShardingsphereProperties shardingsphereProperties;

    @Autowired
    ObjectProvider<List<RuleConfiguration>> rules;

    /**
     * @throws SQLException SQL exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        shardingTransactionTypeScanner();
        Map<String, DataSource> shardingSphereDataSourceMap = new LinkedHashMap<>();
        Assert.notNull(dataSource, MDSRoutingDataSource.MDS_LOG_PREFIX + "dataSource is null,but it is required!");
        Assert.notNull(dataSourceProperties, MDSRoutingDataSource.MDS_LOG_PREFIX + "dataSourceProperties is null,but it is required!");
        Assert.notNull(shardingDS, MDSRoutingDataSource.MDS_LOG_PREFIX + "spring.shardingsphere.datasource is null,but it is required!");
        MDSRoutingDataSource rds = (MDSRoutingDataSource) dataSource;
        Assert.notNull(rds.getCurrentDataSources(), MDSRoutingDataSource.MDS_LOG_PREFIX + "dataSource.dataSourceMap is null,but it is required!");

        for (String oneDSName : shardingDS) {
            if (rds.getCurrentDataSources().get(oneDSName) != null) {
                if (!dataSourceProperties.getDatasource().containsKey(MDSConstant.shardingDatasourceName)) {
                    dataSourceProperties.getDatasource().put(MDSConstant.shardingDatasourceName, dataSourceProperties.getDatasource().get(oneDSName));
                }
                shardingSphereDataSourceMap.put(oneDSName, rds.getCurrentDataSources().get(oneDSName));
                logger.info(MDSRoutingDataSource.MDS_LOG_PREFIX + "the sharding datasource:{} was found", oneDSName);
            } else {
                logger.info(MDSRoutingDataSource.MDS_LOG_PREFIX + "no the datasource:{},pls check config", oneDSName);
            }
        }
        if (!shardingSphereDataSourceMap.isEmpty()) {
            Collection<RuleConfiguration> ruleConfigurations = rules.getIfAvailable();
            for (RuleConfiguration ruleConfiguration : ruleConfigurations) {
                if (ruleConfiguration instanceof AlgorithmProvidedShardingRuleConfiguration) {
                    AlgorithmProvidedShardingRuleConfiguration algorithmProvidedShardingRuleConfiguration = (AlgorithmProvidedShardingRuleConfiguration) ruleConfiguration;
                    for (Map.Entry<String, ShardingAlgorithm> entry : algorithmProvidedShardingRuleConfiguration.getShardingAlgorithms().entrySet()) {
                        entry.getValue().init();
                    }
                }
            }
            DataSource shardingDS = ShardingSphereDataSourceFactory.createDataSource(shardingSphereDataSourceMap, ruleConfigurations, shardingsphereProperties.getProps());
            rds.getCurrentDataSources().put(MDSConstant.shardingDatasourceName, shardingDS);
        }
        return;
    }

    /**
     * Create transaction type scanner.
     *
     * @return transaction type scanner
     */
    @Bean
    public AbstractAdvisingBeanPostProcessor shardingTransactionTypeScanner() throws Exception {
        String shardingsphere500 = "org.apache.shardingsphere.spring.transaction.TransactionTypeScanner";
        String shardingsphere500BETA = "org.apache.shardingsphere.spring.transaction.ShardingTransactionTypeScanner";
        Class clazz = null;
        try {
            clazz = Class.forName(shardingsphere500BETA);
        } catch (Exception e) {
            logger.info(MDSRoutingDataSource.MDS_LOG_PREFIX + " " + shardingsphere500BETA + " does not exist,now try create TransactionTypeScanner...");
        }
        if (clazz == null) {
            clazz = Class.forName(shardingsphere500);
        }
        return (AbstractAdvisingBeanPostProcessor) clazz.getDeclaredConstructor().newInstance();
    }

}