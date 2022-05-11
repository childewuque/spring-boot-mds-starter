package com.github.childewuque.mds.core.persistenceFramework.hibernate.generic;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/4/26 下午6:15
 * @modified By:
 */

public abstract class AbsractGenericDaoHibernateImpl extends HibernateDaoSupport implements GenericDao {
    private Logger logger = LoggerFactory.getLogger(AbsractGenericDaoHibernateImpl.class);

    public AbsractGenericDaoHibernateImpl() {
    }

    public <P> P save(P entity) {
        this.getTemplate().save(entity);
        return entity;
    }

    public <P> void update(P entity) {
        this.getTemplate().update(entity);
    }

    public <P> void saveOrUpdate(P entity) {
        this.getTemplate().saveOrUpdate(entity);
    }

    public <P> void delete(P po) {
        this.getTemplate().delete(po);
    }

    public void delete(String deleteQuery) {
        this.update(deleteQuery);
    }

    public void delete(String deleteQuery, Map<String, Object> args) {
        this.update(deleteQuery, args);
    }

    public Object get(int id, String entity) {
        Object instance = this.getTemplate().get(entity, id);
        return instance;
    }

    public <P> P get(Class<P> aClass, Serializable id) {
        return this.getTemplate().get(aClass, id);
    }

    public Object get(String id, String entityName) {
        Object instance = this.getTemplate().get(entityName, id);
        return instance;
    }

    public HibernateTemplate getTemplate() {
        return this.getHibernateTemplate();
    }

    public List<Object> findByNamedParam(String queryString, String[] paramNames, Object[] values) {
        List<?> list = this.getTemplate().findByNamedParam(queryString, paramNames, values);
        if (list != null) {
            return (List<Object>) list;
        } else {
            return null;
        }

    }


    public Page getPage(String hsql, int page, int pagesize, HashMap<String, Object> params) {
        PageModel pageModel = new PageModel(page, pagesize);
        Long cnt = this.getResultCnt(hsql, params);
        pageModel.setCount(cnt.intValue());
        if (pageModel.getCount() == 0L) {
            return new Page(new ArrayList(), pageModel);
        } else {
            Query q = this.createQuery(hsql, params);
            if (pageModel.getPage() != -2 && pageModel.getPageSize() != 0) {
                q.setFirstResult(pageModel.getStartNo() - 1);
                q.setMaxResults(pageModel.getPageSize());
            }

            return new Page(q.list(), pageModel);
        }
    }

    public Page getPage(String hsql, int page, int pagesize, Long maxsize, HashMap<String, Object> params) {
        PageModel pageModel = new PageModel(page, pagesize);
        pageModel.setCount(maxsize.intValue());
        if (pageModel.getCount() == 0L) {
            return new Page(new ArrayList(), pageModel);
        } else {
            Query q = this.createQuery(hsql, params);
            if (pageModel.getPage() != -2 && pageModel.getPageSize() != 0) {
                q.setFirstResult(pageModel.getStartNo() - 1);
                q.setMaxResults(pageModel.getPageSize());
            }

            return new Page(q.list(), pageModel);
        }
    }

    public Page getPageJDBC(String sql, int page, int pagesize, Map<String, Object> params) {
        PageModel pageModel = new PageModel(page, pagesize);
        Query q = this.createSQLQuery(sql, params);
        if (pageModel.getPage() != -2 && pageModel.getPageSize() != 0) {
            q.setFirstResult(pageModel.getStartNo() - 1);
            q.setMaxResults(pageModel.getPageSize());
        }

        Long cnt = this.getResultCntJDBC(sql, params);
        pageModel.setCount(cnt.intValue());
        return pageModel.getCount() == 0L ? new Page(new ArrayList(), pageModel) : new Page(q.list(), pageModel);
    }

    public Query createSQLQuery(String hsql, Map<String, Object> params) {
        Query q = this.getSessionFactory().getCurrentSession().createSQLQuery(hsql);
        String[] keys = q.getNamedParameters();
        if (keys != null && params != null) {
            String[] var8 = keys;
            int var7 = keys.length;

            for (int var6 = 0; var6 < var7; ++var6) {
                String key = var8[var6];
                if (!params.containsKey(key)) {
                    throw new RuntimeException("没有设置参数" + key + "的值");
                }

                Object value = params.get(key);
                int flg = this.getParameterType(value);
                switch (flg) {
                    case 0:
                        q.setParameter(key, value);
                        break;
                    case 1:
                        q.setParameterList(key, (Collection) value);
                        break;
                    case 2:
                        q.setParameterList(key, (Object[]) value);
                }
            }
        }

        return q;
    }

    public <P> List<P> sqlQuery(String hsql, Map<String, Object> params) {
        Query q = this.createSQLQuery(hsql, params);
        return q.list();
    }

    public <P> List<P> sqlQuery(String sql) {
        Query q = this.getSessionFactory().getCurrentSession().createSQLQuery(sql);
        return q.list();
    }

    public Query createQuery(String hsql, Map<String, Object> params) {
        Query q = this.getSessionFactory().getCurrentSession().createQuery(hsql);
        String[] keys = q.getNamedParameters();
        if (keys != null && params != null) {
            String[] var8 = keys;
            int var7 = keys.length;

            for (int var6 = 0; var6 < var7; ++var6) {
                String key = var8[var6];
                if (!params.containsKey(key)) {
                    throw new RuntimeException("没有设置参数" + key + "的值");
                }

                Object value = params.get(key);
                int flg = this.getParameterType(value);
                switch (flg) {
                    case 0:
                        q.setParameter(key, value);
                        break;
                    case 1:
                        q.setParameterList(key, (Collection) value);
                        break;
                    case 2:
                        q.setParameterList(key, (Object[]) value);
                }
            }
        }

        return q;
    }

    public <P> List<P> query(String hsql, Map<String, Object> params) {
        Query q = this.createQuery(hsql, params);
        return q.list();
    }

    public <P> List<P> query(String hsql) {
        Query q = this.getSessionFactory().getCurrentSession().createQuery(hsql);
        return q.list();
    }

    private Long getResultCnt(String hsql, HashMap<String, Object> params) {
        String newSql = this.delOrderbySQL(hsql);
        int idx = newSql.toUpperCase().indexOf("FROM ");
        newSql = "select count(*) " + newSql.substring(idx);
        Query q = this.createQuery(newSql, params);
        return (Long) q.iterate().next();
    }

    private Long getResultCntJDBC(String sql, Map<String, Object> params) {
        Boolean havegroup = false;
        if (params != null && params.containsKey("havegroup") && params.get("havegroup") != null) {
            havegroup = (Boolean) params.get("havegroup");
        }

        if (havegroup) {
            Query q = this.createSQLQuery(sql, params);
            return new Long((long) q.list().size());
        } else {
            String newSql = this.delOrderbySQL(sql);
            newSql = this.modGroupBySql(newSql);
            int idx = newSql.toUpperCase().indexOf("FROM ");
            newSql = "select count(*) " + newSql.substring(idx);
            Query q = this.createSQLQuery(newSql, params);
//            return ((BigDecimal) q.uniqueResult()).toBigInteger().longValue();

            Object o = q.uniqueResult();
            int n = 0;
            try {
                Method m = o.getClass().getMethod("intValue", (Class[])null);
                n = (Integer)m.invoke(o, (Object[])null);
            } catch (Exception var10) {
                logger.error("",var10);
            }
            return (long)n;
        }
    }

    private String delOrderbySQL(String queryString) {
        String result = queryString;
        int idx = queryString.indexOf("order by");
        if (idx > 0) {
            result = queryString.substring(0, idx);
        }

        return result;
    }

    private String modGroupBySql(String queryString) {
        String result = queryString;
        int idx = queryString.indexOf("group by");
        if (idx > 0) {
            result = "select * from (" + queryString + ") t";
        }

        return result;
    }

    private int getParameterType(Object param) {
        if (param == null) {
            return 0;
        } else if (param.getClass().isArray()) {
            return 2;
        } else {
            Class[] clss = param.getClass().getInterfaces();
            if (clss == null) {
                return 0;
            } else {
                Class[] var6 = clss;
                int var5 = clss.length;

                for (int var4 = 0; var4 < var5; ++var4) {
                    Class cls = var6[var4];
                    if (cls == Collection.class) {
                        return 1;
                    }
                }

                return 0;
            }
        }
    }

    public int update(String statement) {
        Query query = this.createQuery(statement, (Map) null);
        return query.executeUpdate();
    }

    public int update(String statement, Map<String, Object> params) {
        Query query = this.createQuery(statement, params);
        return query.executeUpdate();
    }

    public int sqlUpdate(String statement) {
        Query query = this.createSQLQuery(statement, (Map) null);
        return query.executeUpdate();
    }

    public int sqlUpdate(String statement, Map<String, Object> params) {
        Query query = this.createSQLQuery(statement, params);
        return query.executeUpdate();
    }
}