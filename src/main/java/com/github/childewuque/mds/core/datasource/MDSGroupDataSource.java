package com.github.childewuque.mds.core.datasource;


import com.github.childewuque.mds.core.datasource.strategy.DataSourceStrategys;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/3/30 上午11:31
 * @modified By:
 */
public class MDSGroupDataSource {

    private String groupName;

    private DataSourceStrategys dataSourceStrategy;

    private Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();

    public MDSGroupDataSource(String groupName, DataSourceStrategys dataSourceStrategy) {
        this.groupName = groupName;
        this.dataSourceStrategy = dataSourceStrategy;
    }

    public DataSource addDatasource(String ds, DataSource dataSource) {
        return dataSourceMap.put(ds, dataSource);
    }

    public DataSource removeDatasource(String ds) {
        return dataSourceMap.remove(ds);
    }

    public String determineDataSourceName() {
        return dataSourceStrategy.determineDataSourceName(new ArrayList<>(dataSourceMap.keySet()));
    }

    public int size() {
        return dataSourceMap.size();
    }
}