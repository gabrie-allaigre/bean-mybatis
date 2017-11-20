package com.talanlabs.bean.mybatis.statement;

import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.cache.CacheNameHelper;
import com.talanlabs.bean.mybatis.session.meta.MetaBean;
import com.talanlabs.bean.mybatis.session.meta.MetaInfoBean;
import com.talanlabs.bean.mybatis.session.meta.ValidationBeanException;
import com.talanlabs.bean.mybatis.resultmap.ResultMapNameHelper;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.session.factory.AbstractMappedStatementFactory;
import com.talanlabs.bean.mybatis.statement.sqlsource.DeleteBeansByPropertyNameSqlSource;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Set;

public class DeleteBeansByMappedStatementFactory extends AbstractMappedStatementFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteBeansByMappedStatementFactory.class);

    public DeleteBeansByMappedStatementFactory(BeanConfiguration beanConfiguration) {
        super(beanConfiguration);
    }

    @Override
    public boolean acceptKey(String key) {
        return StatementNameHelper.isDeleteBeansByKey(key);
    }

    @Override
    public MappedStatement createMappedStatement(String key) {
        if (StatementNameHelper.isDeleteBeansByKey(key)) {
            Class<?> beanClass = StatementNameHelper.extractBeanClassInDeleteBeansByKey(key);
            String[] propertyNames = StatementNameHelper.extractPropertyNamesInDeleteBeansByKey(key);
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

                return createDeleteBeansByMappedStatement(key, beanClass, propertyNames);
            }
        }
        return null;
    }

    private MappedStatement createDeleteBeansByMappedStatement(String key, Class<?> beanClass, String... propertyNames) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Create deleteBeansBy for " + beanClass);
        }

        ResultMap inlineResultMap = getBeanConfiguration().getResultMap(ResultMapNameHelper.buildResultMapKey(beanClass));

        MappedStatement.Builder msBuilder = new MappedStatement.Builder(getBeanConfiguration(), key,
                new DeleteBeansByPropertyNameSqlSource(getBeanConfiguration(), beanClass, propertyNames), SqlCommandType.SELECT);
        msBuilder.resultMaps(Collections.singletonList(inlineResultMap));
        Cache cache = getBeanConfiguration().getCache(CacheNameHelper.buildCacheKey(beanClass));
        msBuilder.flushCacheRequired(true);
        msBuilder.cache(cache);
        msBuilder.useCache(true);
        return msBuilder.build();
    }
}
