package com.github.childewuque.mds.core.persistenceFramework.hibernate;

import com.github.childewuque.mds.core.config.DataSourceProperties;
import com.github.childewuque.mds.core.config.DataSourceProperty;
import com.github.childewuque.mds.core.config.MDSConstant;
import com.github.childewuque.mds.core.datasource.MDSRoutingDataSource;
import com.github.childewuque.mds.core.persistenceFramework.MDSDatasourceFactory;
import com.github.childewuque.mds.core.persistenceFramework.hibernate.generic.HibernateGenericDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/6/2 下午1:58
 * @modified By:
 */
@EnableConfigurationProperties({JpaProperties.class})
public abstract class HibernateMDSAbastractConfig extends MDSDatasourceFactory {
    private Logger logger = LoggerFactory.getLogger(HibernateMDSAbastractConfig.class);
    //    @Autowired
//    private ApplicationContext ctx;
    @Autowired
    private DataSourceProperties dataSourceProperties;
    @Autowired
    private JpaProperties jpaProperties;

    @Value("${spring.jpa.hibernate.mysql.model-packages:}")
    String[] mysqlModelPackages;
    @Value("${spring.jpa.hibernate.mysql.dialect:}")
    String mysqlDialect;
    @Value("${spring.jpa.hibernate.mysql.enabled:false}")
    boolean mysqlEnabled;
    @Value("${spring.jpa.hibernate.oracle.model-packages:}")
    String[] oracleModelPackages;
    @Value("${spring.jpa.hibernate.oracle.dialect:}")
    String oracleDialect;
    @Value("${spring.jpa.hibernate.oracle.enabled:false}")
    boolean oracleEnabled;

    @Bean
    @ConditionalOnMissingBean
    public HibernateTemplate hibernateTemplate(DataSource dataSource) throws Exception {
        Map<String, DataSourceProperty> dsStrMap = getDataSourceMap();
        HibernateTemplate hibernateTemplate = null;
        for (Map.Entry<String, DataSourceProperty> entry : dsStrMap.entrySet()) {
            try {
                String dialect = null;
                String[] modelPackages = null;
                if (entry.getValue().getDriverClassName().contains("mysql")) {
                    if (!mysqlEnabled) {
                        logger.info(MDSRoutingDataSource.MDS_LOG_PREFIX+" mysqlEnabled is false!");
                        continue;
                    }
                    dialect = mysqlDialect;
                    modelPackages = mysqlModelPackages;
                } else if (entry.getValue().getDriverClassName().contains("oracle")) {
                    if (!oracleEnabled) {
                        logger.info(MDSRoutingDataSource.MDS_LOG_PREFIX+" oracleEnabled is false!");
                        continue;
                    }
                    dialect = oracleDialect;
                    modelPackages = oracleModelPackages;
                }

                String fbName = entry.getKey() + MDSConstant.HIBERNATE_FACTORYBEAN_SUFFIX;
                String htName = entry.getKey() + MDSConstant.HIBERNATE_HIBERNATETEMPLATE_SUFFIX;
                String tmName = entry.getKey() + MDSConstant.HIBERNATE_TRANSACTIONMANAGER_SUFFIX;
                String daoName = entry.getKey() + MDSConstant.HIBERNATE_DAO_SUFFIX;

                com.github.childewuque.mds.core.aop.MDSDataSourceContextHolder.push(entry.getKey());

                Map<String, String> propertiesMap = jpaProperties.getProperties();
                Properties properties = new Properties();
                properties.putAll(propertiesMap);
                properties.setProperty("hibernate.dialect", dialect);

                BeanDefinitionBuilder sfBDB = BeanDefinitionBuilder.genericBeanDefinition(LocalSessionFactoryBean.class);
                sfBDB.addPropertyValue("hibernateProperties", properties);
                sfBDB.addPropertyValue("dataSource", dataSource);
                sfBDB.addPropertyValue("packagesToScan", modelPackages);
                addLocalSessionFactoryBeanProperties(entry.getKey(), sfBDB);
                if (entry.getKey().equals(dataSourceProperties.getDefaultDatasource())) {
                    sfBDB.setPrimary(true);
                }
                getDefaultListableBeanFactory().registerBeanDefinition(fbName, sfBDB.getBeanDefinition());
                Object sf = ctx.getBean(fbName);

                BeanDefinitionBuilder htBDB = BeanDefinitionBuilder.genericBeanDefinition(HibernateTemplate.class);
                htBDB.addPropertyValue("sessionFactory", sf);
                BeanDefinitionBuilder tmBDB = BeanDefinitionBuilder.genericBeanDefinition(HibernateTransactionManager.class);
                tmBDB.addPropertyValue("sessionFactory", sf);
                if (entry.getKey().equals(dataSourceProperties.getDefaultDatasource())) {
                    htBDB.setPrimary(true);
                    tmBDB.setPrimary(true);
                }
                getDefaultListableBeanFactory().registerBeanDefinition(htName, htBDB.getBeanDefinition());
                getDefaultListableBeanFactory().registerBeanDefinition(tmName, tmBDB.getBeanDefinition());

                HibernateTemplate ht = (HibernateTemplate) ctx.getBean(htName);
                if (entry.getKey().equals(dataSourceProperties.getDefaultDatasource())) {
                    hibernateTemplate = ht;
                }

                BeanDefinitionBuilder daoBDB = BeanDefinitionBuilder.genericBeanDefinition(HibernateGenericDao.class);
                daoBDB.addConstructorArgValue(ht);
                getDefaultListableBeanFactory().registerBeanDefinition(daoName, daoBDB.getBeanDefinition());

            } finally {
                com.github.childewuque.mds.core.aop.MDSDataSourceContextHolder.poll();
            }
        }
        return hibernateTemplate;
    }

    public abstract void addLocalSessionFactoryBeanProperties(final String ds, final BeanDefinitionBuilder beanDefinitionBuilder);

    private Map<String, DataSourceProperty> getDataSourceMap() {
        return dataSourceProperties.getDatasource();
    }

    private DefaultListableBeanFactory getDefaultListableBeanFactory() {
        return (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public HibernateTransactionManager transactionManager(HibernateTemplate hibernateTemplate) {
        HibernateTransactionManager tansactionManager = new HibernateTransactionManager();
        tansactionManager.setSessionFactory(hibernateTemplate.getSessionFactory());
        return tansactionManager;
    }
}
