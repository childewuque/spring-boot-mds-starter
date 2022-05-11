package com.github.childewuque.mds.core.aop;

import com.github.childewuque.mds.core.annotation.DS;
import com.github.childewuque.mds.core.config.DataSourceProperties;
import com.github.childewuque.mds.core.config.MDSConstant;
import com.github.childewuque.mds.core.datasource.MDSRoutingDataSource;
import com.github.childewuque.mds.core.persistenceFramework.hibernate.generic.HibernateGenericDao;
import com.github.childewuque.mds.core.persistenceFramework.jpa.base.JpaMDSTransactionManager;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.lang.reflect.Method;

/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/4/6 下午6:11
 * @modified By:
 */
@Configuration
public class MDSAOP {
    private static Logger logger = LoggerFactory.getLogger(MDSAOP.class);
    @Autowired
    private DataSourceProperties dataSourceProperties;
    @Autowired
    private ApplicationContext ctx;
    @Autowired
    DataSource dataSource;

    @Bean
    public Advisor dataSourceAdvisor() {
        Class clazz = DS.class;
        Pointcut cpc = new AnnotationMatchingPointcut(clazz, true);
        Pointcut mpc = AnnotationMatchingPointcut.forMethodAnnotation(clazz);
        ComposablePointcut composablePointcut = new ComposablePointcut(cpc);
        composablePointcut.union(mpc);
        Advice advice = new MethodAroundAdvice();
        DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor(composablePointcut, advice);
        defaultPointcutAdvisor.setOrder(Integer.MIN_VALUE);
        return defaultPointcutAdvisor;
    }


    public class MethodAroundAdvice implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            boolean needPoll = false;
            try {
                Method method = invocation.getMethod();
                DS ds = method.getAnnotation(DS.class);
                String theDs = null;
                if (ds != null) {
                    theDs = ds.value();
                } else {
                    Class clazz = method.getDeclaringClass();
                    DS dsClass = (DS) clazz.getAnnotation(DS.class);
                    if (dsClass != null) {
                        theDs = dsClass.value();
                    } else {
                        Class subClazz = invocation.getThis().getClass();
                        DS dsSubClass = (DS) subClazz.getAnnotation(DS.class);
                        if (dsSubClass != null) {
                            theDs = dsSubClass.value();
                        }
                    }
                }

                if (StringUtils.isNotBlank(theDs)) {
                    MDSRoutingDataSource mdsRoutingDataSource = (MDSRoutingDataSource) dataSource;
                    if (!mdsRoutingDataSource.getGroupDataSources().isEmpty() && mdsRoutingDataSource.getGroupDataSources().containsKey(theDs)) {
                        theDs = mdsRoutingDataSource.getGroupDataSources().get(theDs).determineDataSourceName();
                    }
                    MDSDataSourceContextHolder.push(theDs);
                    needPoll = true;
                }
                determineSF(theDs);
                Object object = invocation.proceed();
                return object;
            } finally {
                if (needPoll) {
                    MDSDataSourceContextHolder.poll();
                }
            }
        }
    }

    private void determineSF(String dsStr) {
        try {
             if (dataSourceProperties.getPersistenceFramework().equals("jpa")) {
                if (StringUtils.isNotBlank(dsStr)) {
                    String fbName = dsStr + MDSConstant.HIBERNATE_FACTORYBEAN_SUFFIX;
                    JpaMDSTransactionManager jpaMDSTransactionManager = (JpaMDSTransactionManager) ctx.getBean(AbstractPlatformTransactionManager.class);
                    EntityManagerFactory sf = (EntityManagerFactory) ctx.getBean(fbName);
                    jpaMDSTransactionManager.setEntityManagerFactory(sf);
                }
            }
        } catch (Throwable e) {
            logger.info(MDSRoutingDataSource.MDS_LOG_PREFIX + " determineSF failed,dsStr={}", dsStr);
        }
    }
}