package com.github.childewuque.mds.core.persistenceFramework.jpa.base;

import org.springframework.context.ApplicationContext;
import org.springframework.core.NamedThreadLocal;
import org.springframework.lang.Nullable;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.persistence.EntityManagerFactory;
import java.util.Deque;

/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/6/9 上午11:00
 * @modified By:
 */
public class JpaMDSTransactionManager extends JpaTransactionManager {
    private NamedThreadLocal<EntityManagerFactory> emfNTL = new NamedThreadLocal<>("emfNTL");
    private ApplicationContext ctx;

    public JpaMDSTransactionManager(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void setEntityManagerFactory(@Nullable EntityManagerFactory emf) {
        emfNTL.set(emf);
    }

    @Nullable
    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        EntityManagerFactory emf = emfNTL.get();
        if (emf != null) {
            return emf;
        }
        Object emfObject = ctx.getBean("entityManagerFactory");
        if (emfObject != null) {
            emf = (EntityManagerFactory) emfObject;
        }
        return emf;
    }
}
