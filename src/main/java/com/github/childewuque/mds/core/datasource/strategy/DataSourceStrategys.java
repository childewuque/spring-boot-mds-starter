package com.github.childewuque.mds.core.datasource.strategy;

import javax.sql.DataSource;
import java.util.List;

/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/4/2 下午1:56
 * @modified By:
 */
public interface DataSourceStrategys {
    String determineDataSourceName(List<String> dataSourcesName);
}
