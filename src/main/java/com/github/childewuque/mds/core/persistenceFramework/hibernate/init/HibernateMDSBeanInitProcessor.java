package com.github.childewuque.mds.core.persistenceFramework.hibernate.init;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.transaction.TransactionManager;

/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/6/16 上午10:48
 * @modified By:
 */
public class HibernateMDSBeanInitProcessor extends InstantiationAwareBeanPostProcessorAdapter implements BeanFactoryAware {

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
            throw new IllegalArgumentException(
                    "AutowiredAnnotationBeanPostProcessor requires a ConfigurableListableBeanFactory: " + beanFactory);
        }
        ConfigurableListableBeanFactory clbf = (ConfigurableListableBeanFactory) beanFactory;
//        clbf.getBean(HibernateGenericDao.class);
        clbf.getBean(TransactionManager.class);
    }
}

