package com.github.childewuque.mds.core.datasource;

import com.github.childewuque.mds.core.config.DataSourceProperties;
import com.github.childewuque.mds.core.config.DataSourceProperty;
import com.github.childewuque.mds.core.datasource.strategy.DataSourceStrategys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/3/30 上午11:25
 * @modified By:
 */

public class MDSRoutingDataSource extends AbstractRoutingDataSource {

    private Logger logger = LoggerFactory.getLogger(MDSRoutingDataSource.class);

    public static final String MDS_LOG_PREFIX = "MDS-";
    public static final String UNDERLINE = "_";

    private DataSourceProperties dataSourceProperties;
    private final Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();
    private final Map<String, MDSGroupDataSource> groupDataSources = new ConcurrentHashMap<>();
    private Class<? extends DataSourceStrategys> strategy;
    private HikariDataSourceBuilder hikariDataSourceCreator;
    private Map<String, DataSourceProperty> dataSourcePropertiesMap;
    private ApplicationContext ctx;

    @Override
    public Object determineCurrentLookupKey() {
        return com.github.childewuque.mds.core.aop.MDSDataSourceContextHolder.peek();
    }

    public Map<String, DataSource> getCurrentDataSources() {
        return dataSourceMap;
    }

    public Map<String, MDSGroupDataSource> getGroupDataSources() {
        return groupDataSources;
    }

    public MDSRoutingDataSource(HikariDataSourceBuilder hikariDataSourceCreator, DataSourceProperties dataSourceProperties, ApplicationContext ctx) throws Exception {
        this.dataSourcePropertiesMap = dataSourceProperties.getDatasource();
        this.strategy = dataSourceProperties.getStrategy();
        this.hikariDataSourceCreator = hikariDataSourceCreator;
        this.dataSourceProperties = dataSourceProperties;
        this.ctx = ctx;
        loadMDS();
    }

    @Override
    public DataSource determineTargetDataSource() {
        Object dsObject = determineCurrentLookupKey();
        String dsStr = "";
        if (dsObject == null) {
            dsStr = dataSourceProperties.getDefaultDatasource();
        } else {
            dsStr = dsObject.toString();
        }
        DataSource dataSource = null;
        if (dataSourceMap.containsKey(dsStr)) {
            logger.debug(MDS_LOG_PREFIX + "switch to the datasource:{}", dsStr);
            dataSource = dataSourceMap.get(dsStr);
        } else {
            throw new RuntimeException(MDS_LOG_PREFIX + "could not find a datasource named:" + dsObject.toString());
        }
        return dataSource;
    }


    public synchronized void addDataSource(String ds, DataSource dataSource) {
        DataSource oldDataSource = dataSourceMap.put(ds, dataSource);
        this.addGroupDataSource(ds, dataSource);
        if (oldDataSource != null) {
            logger.info(MDS_LOG_PREFIX + "already exist the datasource:{}", ds);
        }
        logger.info(MDS_LOG_PREFIX + "load a datasource named [{}] success", ds);
    }


    private void addGroupDataSource(String ds, DataSource dataSource) {
        if (ds.contains(UNDERLINE)) {
            String group = ds.split(UNDERLINE)[0];
            MDSGroupDataSource groupDataSource = groupDataSources.get(group);
            if (groupDataSource == null) {
                try {
                    groupDataSource = new MDSGroupDataSource(group, strategy.getDeclaredConstructor().newInstance());
                    groupDataSources.put(group, groupDataSource);
                } catch (Exception e) {
                    throw new RuntimeException(MDS_LOG_PREFIX + "add the datasource named " + ds + " error", e);
                }
            }
            groupDataSource.addDatasource(ds, dataSource);
        }
    }

    private void loadMDS() throws Exception {
        Map<Object, Object> dataSourceMap = new HashMap<>(dataSourcePropertiesMap.size() * 2);

        for (Map.Entry<String, DataSourceProperty> item : dataSourcePropertiesMap.entrySet()) {

            DataSourceProperty dataSourceProperty = null;
            try {
                dataSourceProperty = item.getValue();
                String poolName = dataSourceProperty.getPoolName();
                if (poolName == null || "".equals(poolName)) {
                    poolName = item.getKey();
                }
                dataSourceProperty.setPoolName(poolName);
                dataSourceMap.put(poolName, hikariDataSourceCreator.createDataSource(dataSourceProperty));
                addDataSource(poolName, (DataSource) dataSourceMap.get(poolName));
                if (dataSourcePropertiesMap.size() == 1) {
                    dataSourceProperties.setDefaultDatasource(poolName);
                }
                if (poolName.equals(dataSourceProperties.getDefaultDatasource())) {
                    super.setDefaultTargetDataSource(dataSourceMap.get(dataSourceProperties.getDefaultDatasource()));
                }
            } catch (Exception e) {
                logger.error(MDS_LOG_PREFIX + "create datasource failed,ds={},url={},user={}", item.getKey(), dataSourceProperty.getUrl(), dataSourceProperty.getUsername(), e);
                throw e;
            }
        }
        super.setDefaultTargetDataSource(this);
        super.setTargetDataSources(dataSourceMap);
    }
}