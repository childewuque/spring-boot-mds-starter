package com.github.childewuque.mds.core.persistenceFramework.hibernate.generic;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/4/26 下午6:07
 * @modified By:
 */
@Repository("dao")
@ConditionalOnExpression(value = "'${spring.mds.persistence-framework}'.equals('hibernate')")
public class HibernateGenericDao extends AbsractGenericDaoHibernateImpl {

    public HibernateGenericDao(HibernateTemplate hibernateTemplate) {
        this.setHibernateTemplate(hibernateTemplate);
    }

    public void setNewHibernateTemplate(HibernateTemplate hibernateTemplate) {
        super.setHibernateTemplate(hibernateTemplate);

    }
}