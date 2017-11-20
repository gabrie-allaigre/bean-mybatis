package com.talanlabs.bean.mybatis.statement;

import com.talanlabs.bean.mybatis.cache.CacheNameHelper;
import com.talanlabs.bean.mybatis.session.meta.MetaBean;
import com.talanlabs.bean.mybatis.session.meta.ValidationBeanException;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.session.factory.AbstractMappedStatementFactory;
import com.talanlabs.bean.mybatis.statement.sqlsource.DeleteSqlSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;

public class DeleteMappedStatementFactory extends AbstractMappedStatementFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteMappedStatementFactory.class);

    public DeleteMappedStatementFactory(BeanConfiguration beanConfiguration) {
        super(beanConfiguration);
    }

    @Override
    public boolean acceptKey(String key) {
        return StatementNameHelper.isDeleteKey(key);
    }

    @Override
    public MappedStatement createMappedStatement(String key) {
        if (StatementNameHelper.isDeleteKey(key)) {
            Class<?> beanClass = StatementNameHelper.extractBeanClassInDeleteKey(key);
            if (beanClass != null) {
                MetaBean metaBean = getBeanConfiguration().getMetaBean();

                try {
                    getBeanConfiguration().getMetaBean().validateBean(beanClass);
                } catch (ValidationBeanException e) {
                    throw new IllegalArgumentException("Failed to validate Bean=" + beanClass, e);
                }

                String idPropertyName = metaBean.getIdPropertyName(beanClass);
                if (StringUtils.isBlank(idPropertyName)) {
                    throw new IllegalArgumentException("Not found annotation Id for Bean=" + beanClass);
                }

                return createDeleteMappedStatement(key, beanClass);
            }
        }
        return null;
    }

    private MappedStatement createDeleteMappedStatement(String key, Class<?> beanClass) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Create delete for " + beanClass);
        }

        ResultMap inlineResultMap = new ResultMap.Builder(getBeanConfiguration(), key + "-Inline", Integer.class, new ArrayList<>(), null).build();

        MappedStatement.Builder msBuilder = new MappedStatement.Builder(getBeanConfiguration(), key, new DeleteSqlSource(getBeanConfiguration(), beanClass), SqlCommandType.DELETE);
        msBuilder.resultMaps(Collections.singletonList(inlineResultMap));
        Cache cache = getBeanConfiguration().getCache(CacheNameHelper.buildCacheKey(beanClass));
        msBuilder.flushCacheRequired(true);
        msBuilder.cache(cache);
        msBuilder.useCache(true);
        return msBuilder.build();
    }
}
