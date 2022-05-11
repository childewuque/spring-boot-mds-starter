package com.github.childewuque.mds.core.persistenceFramework.jpa;

import com.github.childewuque.mds.core.config.DataSourceProperties;
import com.github.childewuque.mds.core.config.DataSourceProperty;
import com.github.childewuque.mds.core.config.MDSConstant;
import com.github.childewuque.mds.core.persistenceFramework.MDSDatasourceFactory;
import com.github.childewuque.mds.core.persistenceFramework.jpa.base.EnableJpaRepositoriesData;
import com.github.childewuque.mds.core.persistenceFramework.jpa.base.JpaMDSTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension;
import org.springframework.data.repository.config.AnnotationRepositoryConfigurationSource;
import org.springframework.data.repository.config.RepositoryConfigurationDelegate;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;
import org.springframework.data.repository.config.RepositoryConfigurationUtils;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/4/15 下午3:09
 * @modified By:
 */
@SuppressWarnings("all")
@EnableConfigurationProperties({JpaProperties.class})
public abstract class JpaMDSAbastractConfig extends MDSDatasourceFactory {
    @Autowired
    private ApplicationContext ctx;
    @Autowired
    private Environment environment;
    @Autowired
    private DataSourceProperties dataSourceProperties;
    @Autowired
    private JpaProperties jpaProperties;

    //    @Value("${spring.jpa.hibernate.mysql.repo-packages:}")
//    String[] mysqlRepoPackages;
    @Value("${spring.jpa.hibernate.mysql.model-packages:}")
    String[] mysqlModelPackages;
    @Value("${spring.jpa.hibernate.mysql.dialect:}")
    String mysqlDialect;
    @Value("${spring.jpa.hibernate.mysql.enabled:false}")
    boolean mysqlEnabled;

    //    @Value("${spring.jpa.hibernate.oracle.repo-packages:}")
//    String[] oracleRepoPackages;
    @Value("${spring.jpa.hibernate.oracle.model-packages:}")
    String[] oracleModelPackages;
    @Value("${spring.jpa.hibernate.oracle.dialect:}")
    String oracleDialect;
    @Value("${spring.jpa.hibernate.oracle.enabled:false}")
    boolean oracleEnabled;

    @Bean
    @ConditionalOnMissingBean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) throws Exception {
        Map<String, DataSourceProperty> dsStrMap = getDataSourceMap();
        LocalContainerEntityManagerFactoryBean defaultFB = null;
        for (Map.Entry<String, DataSourceProperty> entry : dsStrMap.entrySet()) {
            try {
                String dialect = null;
                String[] modelPackages = null;
//                String[] repoPackages = null;
                if (entry.getValue().getDriverClassName().contains("mysql")) {
                    if (!mysqlEnabled) {
                        continue;
                    }
                    dialect = mysqlDialect;
                    modelPackages = mysqlModelPackages;
//                    repoPackages = mysqlRepoPackages;
                } else if (entry.getValue().getDriverClassName().contains("oracle")) {
                    if (!oracleEnabled) {
                        continue;
                    }
                    dialect = oracleDialect;
                    modelPackages = oracleModelPackages;
//                    repoPackages = oracleRepoPackages;
                }

                String fbName = entry.getKey() + MDSConstant.HIBERNATE_FACTORYBEAN_SUFFIX;
                String tmName = entry.getKey() + MDSConstant.HIBERNATE_TRANSACTIONMANAGER_SUFFIX;

                com.github.childewuque.mds.core.aop.MDSDataSourceContextHolder.push(entry.getKey());


                LocalContainerEntityManagerFactoryBean fb = new LocalContainerEntityManagerFactoryBean();
                fb.setDataSource(dataSource);
                fb.setPackagesToScan(modelPackages);
                HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//                vendorAdapter.setGenerateDdl(jpaProperties.isGenerateDdl());
//                vendorAdapter.setShowSql(jpaProperties.isShowSql());
                fb.setJpaVendorAdapter(vendorAdapter);

                Map<String, String> properties = jpaProperties.getProperties();
                properties.put("hibernate.dialect", dialect);
                fb.setJpaPropertyMap(properties);
                addEntityManagerFactoryBeanProperties(entry.getKey(), fb);
                getDefaultListableBeanFactory().registerSingleton(fbName, fb);
                fb.afterPropertiesSet();

//                Object fbObjectTest = ctx.getBean(fbName);
//                EntityManagerFactory emf = (EntityManagerFactory)ctx.getBean(fbName);
//                BeanDefinitionBuilder jtmBDB = BeanDefinitionBuilder.genericBeanDefinition(JpaMDSTransactionManager.class);
//                jtmBDB.addPropertyValue("entityManagerFactory", emf);
                if (entry.getKey().equals(dataSourceProperties.getDefaultDatasource())) {
                    defaultFB = fb;
//                    jtmBDB.setPrimary(true);
                }
//                getDefaultListableBeanFactory().registerBeanDefinition(tmName, jtmBDB.getBeanDefinition());

//                JpaTransactionManager tmObject = (JpaTransactionManager)ctx.getBean(tmName);

                Map<String, Object> repoMap = new HashMap<String, Object>();
//                repoMap.put("escapeCharacter", '\\');
                repoMap.put("basePackages", entry.getValue().getRepoPackages());
                repoMap.put("entityManagerFactoryRef", fbName);
//                repoMap.put("transactionManagerRef", tmName);
                addJpaRepositoriesProperties(entry.getKey(), repoMap);
                AnnotationMetadata enableJpaRepositoriesData = new EnableJpaRepositoriesData(this.getClass().getClassLoader(), repoMap);
                AnnotationRepositoryConfigurationSource configurationSource = new AnnotationRepositoryConfigurationSource(
                        enableJpaRepositoriesData, EnableJpaRepositories.class, ctx, environment, getDefaultListableBeanFactory()
                );
                RepositoryConfigurationExtension extension = new JpaRepositoryConfigExtension();
                RepositoryConfigurationUtils.exposeRegistration(extension, getDefaultListableBeanFactory(), configurationSource);
                RepositoryConfigurationDelegate delegate = new RepositoryConfigurationDelegate(configurationSource, ctx, environment);
                delegate.registerRepositoriesIn(getDefaultListableBeanFactory(), extension);

            } finally {
                com.github.childewuque.mds.core.aop.MDSDataSourceContextHolder.poll();
            }
        }
        return defaultFB;
    }


    private Map<String, DataSourceProperty> getDataSourceMap() {
        return dataSourceProperties.getDatasource();
    }

    private DefaultListableBeanFactory getDefaultListableBeanFactory() {
        return (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();
    }

    public abstract void addEntityManagerFactoryBeanProperties(final String ds, final LocalContainerEntityManagerFactoryBean fb);

    public abstract void addJpaRepositoriesProperties(final String ds, final Map<String, Object> repoMap);

    @Bean
    @ConditionalOnMissingBean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaMDSTransactionManager transactionManager
                = new JpaMDSTransactionManager(ctx);
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    @Bean
    @ConditionalOnMissingBean
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }
}
