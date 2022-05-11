package com.github.childewuque.mds.core.metrics;

import com.zaxxer.hikari.metrics.IMetricsTracker;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import com.zaxxer.hikari.metrics.PoolStats;
import io.micrometer.core.instrument.MeterRegistry;

/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/7/2 下午8:05
 * @modified By:
 */

public class MDSMicrometerMetricsTrackerFactory implements MetricsTrackerFactory {

    private final MeterRegistry registry;
    private final String userName;
    private final String jdbcUrl;
    private final String host;
    private final String port;
    private final String db;
    private final String mdsName;
    private final String mdsVersion;


    public MDSMicrometerMetricsTrackerFactory(MeterRegistry registry, String userName, String jdbcUrl, String host, String port, String db, String mdsName, String mdsVersion) {
        this.registry = registry;
        this.userName = userName;
        this.jdbcUrl = jdbcUrl;
        this.host = host;
        this.port = port;
        this.db = db;
        this.mdsName = mdsName;
        this.mdsVersion = mdsVersion;
    }

    @Override
    public IMetricsTracker create(String poolName, PoolStats poolStats) {
        return new MDSMicrometerMetricsTracker(poolName, poolStats, registry, userName, jdbcUrl, host, port, db, mdsName, mdsVersion);
    }
}
