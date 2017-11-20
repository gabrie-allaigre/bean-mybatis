package com.talanlabs.bean.mybatis.statement;

import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.JoinTable;
import com.talanlabs.bean.mybatis.annotation.OrderBy;
import com.talanlabs.bean.mybatis.cache.CacheNameHelper;
import com.talanlabs.bean.mybatis.session.meta.MetaBean;
import com.talanlabs.bean.mybatis.session.meta.MetaInfoBean;
import com.talanlabs.bean.mybatis.session.meta.ValidationBeanException;
import com.talanlabs.bean.mybatis.resultmap.ResultMapNameHelper;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.session.factory.AbstractMappedStatementFactory;
import com.talanlabs.bean.mybatis.statement.sqlsource.FindBeansByJoinTableSqlSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Set;

public class FindBeansByJoinTableMappedStatementFactory extends AbstractMappedStatementFactory {

    private static final Logger LOG = LoggerFactory.getLogger(FindBeansByJoinTableMappedStatementFactory.class);

    public FindBeansByJoinTableMappedStatementFactory(BeanConfiguration beanConfiguration) {
        super(beanConfiguration);
    }

    @Override
    public boolean acceptKey(String key) {
        return StatementNameHelper.isFindBeansByJoinTableKey(key);
    }

    @Override
    public MappedStatement createMappedStatement(String key) {
        if (StatementNameHelper.isFindBeansByJoinTableKey(key)) {
            Class<?> beanClass = StatementNameHelper.extractBeanClassInFindBeansByJoinTableKey(key);
            Class<?> sourceBeanClass = StatementNameHelper.extractSourceBeanClassInFindBeansByJoinTableKey(key);
            String[] sourceProperties = StatementNameHelper.extractSourcePropertiesInFindBeansByJoinTableKey(key);
            String[] targetProperties = StatementNameHelper.extractTargetPropertiesInFindBeansByJoinTableKey(key);
            JoinTable[] joins = StatementNameHelper.extractJoinInFindBeansByJoinTableKey(key);
            boolean ignoreCancel = StatementNameHelper.isIgnoreCancelInFindBeansByJoinTableKey(key);
            OrderBy[] orderBies = StatementNameHelper.extractOrderBiesInFindBeansByJoinTableKey(key);
            if (beanClass != null && sourceBeanClass != null && sourceProperties != null && sourceProperties.length > 0 && targetProperties != null && targetProperties.length > 0
                    && joins != null && joins.length > 0) {
                MetaBean metaBean = getBeanConfiguration().getMetaBean();
                MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);
                MetaInfoBean sourceMetaInfoBean = metaBean.forBeanClass(sourceBeanClass);

                try {
                    metaBean.validateBean(beanClass);
                } catch (ValidationBeanException e) {
                    throw new IllegalArgumentException("Failed to validate Bean=" + beanClass, e);
                }
                try {
                    metaBean.validateBean(sourceBeanClass);
                } catch (ValidationBeanException e) {
                    throw new IllegalArgumentException("Failed to validate Bean=" + sourceBeanClass, e);
                }
                Set<String> all = metaInfoBean.getPropertyNames();
                for (String propertyName : targetProperties) {
                    if (!all.contains(propertyName)) {
                        throw new IllegalArgumentException("Not found property in Bean=" + beanClass + " property=" + propertyName);
                    } else if (!metaInfoBean.isPropertyAnnotationPresent(propertyName, Column.class)) {
                        throw new IllegalArgumentException("Failed property is not Column in Bean=" + beanClass + " property=" + propertyName);
                    }
                }
                Set<String> sourceAll = sourceMetaInfoBean.getPropertyNames();
                for (String propertyName : sourceProperties) {
                    if (!sourceAll.contains(propertyName)) {
                        throw new IllegalArgumentException("Not found property in Bean=" + sourceBeanClass + " property=" + propertyName);
                    } else if (!sourceMetaInfoBean.isPropertyAnnotationPresent(propertyName, Column.class)) {
                        throw new IllegalArgumentException("Failed property is not Column in Bean=" + sourceBeanClass + " property=" + propertyName);
                    }
                }

                String canceledPropertyName = metaBean.getCanceledPropertyName(beanClass);
                if (ignoreCancel && StringUtils.isBlank(canceledPropertyName)) {
                    throw new IllegalArgumentException("Not found canceled property in Bean=" + beanClass);
                }

                int i = 0;
                for (JoinTable joinTable : joins) {
                    if (StringUtils.isBlank(joinTable.name())) {
                        throw new IllegalArgumentException("JoinTable is empty for Bean=" + sourceBeanClass + " and Bean=" + beanClass);
                    }
                    if (joinTable.left().length == 0) {
                        throw new IllegalArgumentException("JoinTable join=" + joinTable.name() + " left is empty for Bean=" + sourceBeanClass + " and Bean=" + beanClass);
                    }
                    if (joinTable.right().length == 0) {
                        throw new IllegalArgumentException("JoinTable join=" + joinTable.name() + " right is empty for Bean=" + sourceBeanClass + " and Bean=" + beanClass);
                    }

                    if (i == 0) {
                        if (sourceProperties.length != joinTable.left().length) {
                            throw new IllegalArgumentException("JoinTable join=" + joinTable.name() + " left different size propertySource for Bean=" + sourceBeanClass + " and Bean=" + beanClass);
                        }
                    }
                    if (i == joins.length - 1) {
                        if (targetProperties.length != joinTable.right().length) {
                            throw new IllegalArgumentException("JoinTable join=" + joinTable.name() + " right different size propertyTarget for Bean=" + sourceBeanClass + " and Bean=" + beanClass);
                        }
                    }
                    if (i > 0 && i < joins.length) {
                        if (joins[i - 1].right().length != joinTable.left().length) {
                            throw new IllegalArgumentException("JoinTable join=" + joinTable.name() + " left different size with previous join for Bean=" + sourceBeanClass + " and Bean=" + beanClass);
                        }
                    }

                    i++;
                }

                if (orderBies != null) {
                    for (OrderBy orderBy : orderBies) {
                        if (!all.contains(orderBy.value())) {
                            throw new IllegalArgumentException("Not found property in Bean=" + beanClass + " property=" + orderBy.value());
                        } else if (!metaInfoBean.isPropertyAnnotationPresent(orderBy.value(), Column.class)) {
                            throw new IllegalArgumentException("Failed property is not Column in Bean=" + beanClass + " property=" + orderBy.value());
                        }
                    }
                }

                return createFindBeansByJoinTableMappedStatement(key, beanClass, sourceBeanClass, sourceProperties, targetProperties, joins, ignoreCancel,
                        orderBies);
            }
        }
        return null;
    }

    private MappedStatement createFindBeansByJoinTableMappedStatement(String key, Class<?> beanClass, Class<?> sourceBeanClass, String[] sourceProperties, String[] targetProperties, JoinTable[] joins, boolean ignoreCancel, OrderBy[] orderBies) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Create findBeansByJoinTable for " + beanClass);
        }

        ResultMap inlineResultMap = getBeanConfiguration().getResultMap(ResultMapNameHelper.buildNestedResultMapKey(beanClass));

        MappedStatement.Builder msBuilder = new MappedStatement.Builder(getBeanConfiguration(), key,
                new FindBeansByJoinTableSqlSource(getBeanConfiguration(), beanClass, sourceBeanClass, sourceProperties, targetProperties, joins, ignoreCancel, orderBies),
                SqlCommandType.SELECT);
        msBuilder.resultMaps(Collections.singletonList(inlineResultMap));
        Cache cache = getBeanConfiguration().getCache(CacheNameHelper.buildCacheKey(beanClass));
        msBuilder.flushCacheRequired(false);
        msBuilder.cache(cache);
        msBuilder.useCache(true);
        return msBuilder.build();
    }
}
