package com.talanlabs.bean.mybatis.statement;

import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.OrderBy;
import com.talanlabs.bean.mybatis.cache.CacheNameHelper;
import com.talanlabs.bean.mybatis.session.meta.MetaBean;
import com.talanlabs.bean.mybatis.session.meta.MetaInfoBean;
import com.talanlabs.bean.mybatis.session.meta.ValidationBeanException;
import com.talanlabs.bean.mybatis.resultmap.ResultMapNameHelper;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.session.factory.AbstractMappedStatementFactory;
import com.talanlabs.bean.mybatis.statement.sqlsource.FindBeansByPropertyNameSqlSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Set;

public class FindBeansByMappedStatementFactory extends AbstractMappedStatementFactory {

    private static final Logger LOG = LoggerFactory.getLogger(FindBeansByMappedStatementFactory.class);

    public FindBeansByMappedStatementFactory(BeanConfiguration beanConfiguration) {
        super(beanConfiguration);
    }

    @Override
    public boolean acceptKey(String key) {
        return StatementNameHelper.isFindBeansByKey(key);
    }

    @Override
    public MappedStatement createMappedStatement(String key) {
        if (StatementNameHelper.isFindBeansByKey(key)) {
            Class<?> beanClass = StatementNameHelper.extractBeanClassInFindBeansByKey(key);
            String[] propertyNames = StatementNameHelper.extractPropertyNamesInFindBeansByKey(key);
            boolean ignoreCancel = StatementNameHelper.isIgnoreCancelInFindBeansByKey(key);
            OrderBy[] orderBies = StatementNameHelper.extractOrderBiesInFindBeansByKey(key);
            if (beanClass != null && propertyNames != null && propertyNames.length > 0) {
                MetaBean metaBean = getBeanConfiguration().getMetaBean();
                MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

                try {
                    metaBean.validateBean(beanClass);
                } catch (ValidationBeanException e) {
                    throw new IllegalArgumentException("Failed to validate Bean=" + beanClass, e);
                }
                Set<String> all = metaInfoBean.getPropertyNames();
                for (String propertyName : propertyNames) {
                    if (!all.contains(propertyName)) {
                        throw new IllegalArgumentException("Not found property in Bean=" + beanClass + " property=" + propertyName);
                    } else if (!metaInfoBean.isPropertyAnnotationPresent(propertyName, Column.class)) {
                        throw new IllegalArgumentException("Failed property is not Column in Bean=" + beanClass + " property=" + propertyName);
                    }
                }

                String canceledPropertyName = metaBean.getCanceledPropertyName(beanClass);
                if (ignoreCancel && StringUtils.isBlank(canceledPropertyName)) {
                    throw new IllegalArgumentException("Not found canceled property in Bean=" + beanClass);
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

                return createFindBeansByMappedStatement(key, beanClass, ignoreCancel, propertyNames, orderBies);
            }
        }
        return null;
    }

    private MappedStatement createFindBeansByMappedStatement(String key, Class<?> beanClass, boolean ignoreCancel,
                                                             String[] propertyNames, OrderBy[] orderBies) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Create findBeansBy for " + beanClass);
        }

        ResultMap inlineResultMap = getBeanConfiguration().getResultMap(ResultMapNameHelper.buildNestedResultMapKey(beanClass));

        MappedStatement.Builder msBuilder = new MappedStatement.Builder(getBeanConfiguration(), key,
                new FindBeansByPropertyNameSqlSource(getBeanConfiguration(), beanClass, ignoreCancel, propertyNames, orderBies), SqlCommandType.SELECT);
        msBuilder.resultMaps(Collections.singletonList(inlineResultMap));
        Cache cache = getBeanConfiguration().getCache(CacheNameHelper.buildCacheKey(beanClass));
        msBuilder.flushCacheRequired(false);
        msBuilder.cache(cache);
        msBuilder.useCache(true);
        return msBuilder.build();
    }
}
