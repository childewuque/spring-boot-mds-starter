package com.github.childewuque.mds.core.datasource.strategy;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/3/30 上午11:36
 * @modified By:
 */
public class RandomStrategy implements DataSourceStrategys {

    @Override
    public String determineDataSourceName(List<String> dataSourcesName) {
        return dataSourcesName.get(ThreadLocalRandom.current().nextInt(dataSourcesName.size()));
    }
}
