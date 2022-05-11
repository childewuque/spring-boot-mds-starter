package com.github.childewuque.mds.core.persistenceFramework;

import com.github.childewuque.mds.core.config.DataSourceProperties;
import com.github.childewuque.mds.core.datasource.MDSRoutingDataSource;
import com.github.childewuque.mds.core.datasource.HikariDataSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import javax.sql.DataSource;

/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/4/19 下午5:13
 * @modified By:
 */
@Configuration
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties({DataSourceProperties.class})
public abstract class MDSDatasourceFactory {
    @Autowired
    private DataSourceProperties dataSourceProperties;
    @Autowired
    protected ApplicationContext ctx;

    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource() throws Exception {
        Assert.notNull(dataSourceProperties.getHikari(), MDSRoutingDataSource.MDS_LOG_PREFIX + "spring.mds.hikari is null,but it is required!");
        HikariDataSourceBuilder hikariDataSourceCreator = new HikariDataSourceBuilder(dataSourceProperties.getHikari());
        return new MDSRoutingDataSource(hikariDataSourceCreator, dataSourceProperties, ctx);
    }

}
