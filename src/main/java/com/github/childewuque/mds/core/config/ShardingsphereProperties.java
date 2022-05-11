package com.github.childewuque.mds.core.config;


import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;


/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/3/30 上午11:36
 * @modified By:
 */
@Configuration
@ConditionalOnMissingBean(ShardingsphereProperties.class)
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "spring.shardingsphere")
@ConditionalOnProperty(prefix = "spring.shardingsphere", name = "enabled", havingValue = "true", matchIfMissing = false)
public class ShardingsphereProperties {

    private Properties props = new Properties();

    public Properties getProps() {
        return props;
    }

    public void setProps(Properties props) {
        this.props = props;
    }
}
