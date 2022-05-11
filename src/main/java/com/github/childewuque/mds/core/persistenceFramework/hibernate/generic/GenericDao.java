package com.github.childewuque.mds.core.persistenceFramework.hibernate.generic;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.HibernateTemplate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/4/26 下午6:30
 * @modified By:
 */
public interface GenericDao {
    SessionFactory getSessionFactory();

    <P> P save(P var1);

    <P> void update(P var1);

    <P> void saveOrUpdate(P var1);

    <P> void delete(P var1);

    void delete(String var1);

    void delete(String var1, Map<String, Object> var2);

    int update(String var1);

    int update(String var1, Map<String, Object> var2);

    int sqlUpdate(String var1);

    int sqlUpdate(String var1, Map<String, Object> var2);

    Object get(int var1, String var2);

    <P> P get(Class<P> var1, Serializable var2);

    Object get(String var1, String var2);

    HibernateTemplate getTemplate();

    List<Object> findByNamedParam(String var1, String[] var2, Object[] var3);

    Page getPage(String var1, int var2, int var3, HashMap<String, Object> var4);

    Page getPage(String var1, int var2, int var3, Long var4, HashMap<String, Object> var5);

    Page getPageJDBC(String var1, int var2, int var3, Map<String, Object> var4);

    Query createSQLQuery(String var1, Map<String, Object> var2);

    <P> List<P> sqlQuery(String var1, Map<String, Object> var2);

    <P> List<P> sqlQuery(String var1);

    Query createQuery(String var1, Map<String, Object> var2);

    <P> List<P> query(String var1, Map<String, Object> var2);

    <P> List<P> query(String var1);
}
