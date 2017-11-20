package com.talanlabs.bean.mybatis.session.defaults;

import com.google.common.collect.Sets;
import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.bean.mybatis.annotation.NlsColumn;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.session.IBeanSqlSession;
import com.talanlabs.bean.mybatis.session.handler.INlsColumnHandler;
import com.talanlabs.bean.mybatis.session.meta.MetaBean;
import com.talanlabs.bean.mybatis.session.meta.MetaInfoBean;
import com.talanlabs.bean.mybatis.session.observer.ITriggerObserver;
import com.talanlabs.bean.mybatis.statement.StatementNameHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.*;

public class DefaultBeanSqlSession implements IBeanSqlSession {

    private final SqlSession sqlSession;
    private final BeanConfiguration beanConfiguration;
    private final Map<Class<?>, NlsBeanCache> nlsBeanCacheMap = Collections.synchronizedMap(new HashMap<>());

    public DefaultBeanSqlSession(SqlSession sqlSession) {
        super();

        this.sqlSession = sqlSession;
        this.beanConfiguration = (BeanConfiguration) sqlSession.getConfiguration();
    }

    @Override
    public <E> E findById(Class<E> beanClass, Object id) {
        return sqlSession.selectOne(StatementNameHelper.buildFindEntityByIdKey(beanClass), id);
    }

    @Override
    public <E> int insert(Class<E> beanClass, E bean) {
        if (bean == null) {
            return 0;
        }

        triggerBefore(ITriggerObserver.Type.Insert, bean);
        int res = sqlSession.insert(StatementNameHelper.buildInsertKey(beanClass), bean);

        mergeNlsBean(beanClass, bean,null);

        triggerAfter(ITriggerObserver.Type.Insert, bean);
        return res;
    }

    @Override
    public <E> int update(Class<E> beanClass, E bean, String... properties) {
        if (bean == null) {
            return 0;
        }

        Set<String> propertyNames = properties != null && properties.length > 0 ? Sets.newHashSet(properties) : null;

        Set<String> nlsPropertyNames = new HashSet<>();

        if (beanConfiguration.getNlsColumnHandler() != null) {
            INlsColumnHandler nlsColumnHandler = beanConfiguration.getNlsColumnHandler();

            NlsBeanCache nlsBeanCache = buildNlsBeanCache(beanClass);
            if (nlsBeanCache != null && nlsBeanCache.columnMap != null) {
                for (Map.Entry<String, String> entry : nlsBeanCache.columnMap.entrySet()) {
                    String propertyName = entry.getKey();
                    if ((propertyNames == null || propertyNames.contains(propertyName)) && nlsColumnHandler.isUpdateDefaultNlsColumn(beanClass, propertyName)) {
                        nlsPropertyNames.add(propertyName);
                    }
                }
            }
        }

        triggerBefore(ITriggerObserver.Type.Update, bean);

        int res = sqlSession.update(StatementNameHelper
                        .buildUpdateKey(beanClass, propertyNames != null ? propertyNames.toArray(new String[propertyNames.size()]) : null, nlsPropertyNames.toArray(new String[nlsPropertyNames.size()])),
                bean);

        mergeNlsBean(beanClass, bean, propertyNames);

        triggerAfter(ITriggerObserver.Type.Update, bean);
        return res;
    }

    @Override
    public <E> int delete(Class<E> beanClass, E bean) {
        if (bean == null) {
            return 0;
        }

        triggerBefore(ITriggerObserver.Type.Delete, bean);

        int res = sqlSession.delete(StatementNameHelper.buildDeleteKey(beanClass), bean);

        deleteNlsBean(beanClass, bean);

        triggerAfter(ITriggerObserver.Type.Delete, bean);
        return res;
    }

    private void triggerBefore(ITriggerObserver.Type type, Object bean) {
        if (sqlSession.getConfiguration() instanceof BeanConfiguration && beanConfiguration.getTriggerDispatcher() != null) {
            beanConfiguration.getTriggerDispatcher().triggerBefore(sqlSession, type, bean);
        }
    }

    private void triggerAfter(ITriggerObserver.Type type, Object bean) {
        if (sqlSession.getConfiguration() instanceof BeanConfiguration && beanConfiguration.getTriggerDispatcher() != null) {
            beanConfiguration.getTriggerDispatcher().triggerAfter(sqlSession, type, bean);
        }
    }

    private void mergeNlsBean(Class<?> beanClass, Object bean, Set<String> propertyNames) {
        if (beanConfiguration.getNlsColumnHandler() == null) {
            return;
        }
        INlsColumnHandler nlsColumnHandler = beanConfiguration.getNlsColumnHandler();

        NlsBeanCache nlsBeanCache = nlsBeanCacheMap.get(beanClass);
        if (nlsBeanCache == null) {
            nlsBeanCache = buildNlsBeanCache(beanClass);
            nlsBeanCacheMap.put(beanClass, nlsBeanCache);
        }

        if (nlsBeanCache == null || nlsBeanCache.columnMap == null) {
            return;
        }
        MetaInfoBean metaInfoBean = beanConfiguration.getMetaBean().forBeanClass(beanClass);

        for (Map.Entry<String, String> entry : nlsBeanCache.columnMap.entrySet()) {
            String propertyName = entry.getKey();
            if (propertyNames == null || propertyNames.isEmpty() || propertyNames.contains(propertyName)) {
                String mergeId = nlsColumnHandler.getMergeNlsColumnId(beanClass, propertyName);
                if (StringUtils.isNotBlank(mergeId)) {
                    Map<String, Object> parameter = new HashMap<>();
                    parameter.put("tableName", nlsBeanCache.tableName);
                    parameter.put("columnName", entry.getValue());
                    parameter.put("id", metaInfoBean.getPropertyValue(bean, nlsBeanCache.idName));
                    parameter.put("meaning", metaInfoBean.getPropertyValue(bean, propertyName));

                    Map<String, Object> additionalParameter = nlsColumnHandler.getAdditionalParameter(beanClass, propertyName);
                    if (additionalParameter != null) {
                        parameter.putAll(additionalParameter);
                    }

                    sqlSession.update(mergeId, parameter);
                }
            }
        }
    }

    private <E> void deleteNlsBean(Class<E> beanClass, E bean) {
        if (beanConfiguration.getNlsColumnHandler() != null) {
            INlsColumnHandler nlsColumnHandler = beanConfiguration.getNlsColumnHandler();

            NlsBeanCache nlsBeanCache = nlsBeanCacheMap.get(beanClass);
            if (nlsBeanCache == null) {
                nlsBeanCache = buildNlsBeanCache(beanClass);
                nlsBeanCacheMap.put(beanClass, nlsBeanCache);
            }

            if (nlsBeanCache != null && nlsBeanCache.columnMap != null) {
                MetaInfoBean metaInfoBean = beanConfiguration.getMetaBean().forBeanClass(beanClass);

                String deleteId = nlsColumnHandler.getDeleteNlsColumnsId(beanClass);
                if (StringUtils.isNotBlank(deleteId)) {
                    Map<String, Object> parameter = new HashMap<>();
                    parameter.put("tableName", nlsBeanCache.tableName);
                    parameter.put("id", metaInfoBean.getPropertyValue(bean, nlsBeanCache.idName));

                    sqlSession.delete(deleteId, parameter);
                }
            }
        }
    }

    private <E> NlsBeanCache buildNlsBeanCache(Class<E> beanClass) {
        NlsBeanCache res = new NlsBeanCache();

        MetaBean metaBean = beanConfiguration.getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

        Entity entity = beanClass.getAnnotation(Entity.class);
        res.tableName = entity.name();

        String idPropertyName = metaBean.getIdPropertyName(beanClass);
        if (idPropertyName == null) {
            throw new IllegalArgumentException("Not found annotation Id for bean=" + beanClass);
        }
        res.idName = idPropertyName;

        Set<String> propertyNames = metaBean.getPropertyNamesWithNlsColumn(beanClass);
        if (propertyNames != null && !propertyNames.isEmpty()) {
            res.columnMap = new HashMap<>(propertyNames.size());
            for (String propertyName : propertyNames) {
                NlsColumn nlsColumn = metaInfoBean.getPropertyAnnotation(propertyName, NlsColumn.class);
                res.columnMap.put(propertyName, nlsColumn.name());
            }
        }
        return res;
    }

    private class NlsBeanCache {

        String tableName = null;

        String idName = null;

        Map<String, String> columnMap = null;

    }
}
