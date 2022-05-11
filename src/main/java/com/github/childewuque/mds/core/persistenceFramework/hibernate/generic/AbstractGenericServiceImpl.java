package com.github.childewuque.mds.core.persistenceFramework.hibernate.generic;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/4/26 下午6:45
 * @modified By:
 */
public abstract class AbstractGenericServiceImpl {
    @Autowired
    public GenericDao dao;

    public AbstractGenericServiceImpl() {
    }

    public GenericDao getDao() {
        return this.dao;
    }
}
