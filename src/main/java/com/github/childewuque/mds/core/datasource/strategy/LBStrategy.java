package com.github.childewuque.mds.core.datasource.strategy;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/3/30 上午11:36
 * @modified By:
 */
public class LBStrategy implements DataSourceStrategys {

    private final AtomicInteger index = new AtomicInteger(0);

    @Override
    public String determineDataSourceName(List<String> dataSourcesName) {
        return dataSourcesName.get(Math.abs(index.getAndAdd(1) % dataSourcesName.size()));
    }
}
