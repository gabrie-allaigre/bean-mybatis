package com.talanlabs.bean.mybatis.statement;

import com.talanlabs.bean.mybatis.cache.CacheNameHelper;
import com.talanlabs.bean.mybatis.session.meta.MetaBean;
import com.talanlabs.bean.mybatis.session.meta.ValidationBeanException;
import com.talanlabs.bean.mybatis.resultmap.ResultMapNameHelper;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.session.factory.AbstractMappedStatementFactory;
import com.talanlabs.bean.mybatis.statement.sqlsource.DeleteBeansByPropertyNameSqlSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class DeleteEntityByIdMappedStatementFactory extends AbstractMappedStatementFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteEntityByIdMappedStatementFactory.class);

    public DeleteEntityByIdMappedStatementFactory(BeanConfiguration beanConfiguration) {
        super(beanConfiguration);
    }

    @Override
    public boolean acceptKey(String key) {
        return StatementNameHelper.isDeleteEntityByIdKey(key);
    }

    @Override
    public MappedStatement createMappedStatement(String key) {
        if (StatementNameHelper.isDeleteEntityByIdKey(key)) {
            Class<?> beanClass = StatementNameHelper.extractBeanClassInDeleteEntityByIdKey(key);
            if (beanClass != null) {
                MetaBean metaBean = getBeanConfiguration().getMetaBean();

                try {
                    metaBean.validateBean(beanClass);
                } catch (ValidationBeanException e) {
                    throw new IllegalArgumentException("Failed to validate Bean=" + beanClass, e);
                }

                String idPropertyName = metaBean.getIdPropertyName(beanClass);
                if (StringUtils.isBlank(idPropertyName)) {
                    throw new IllegalArgumentException("Not found annotation Id for Bean=" + beanClass);
                }

                return createDeleteEntityByIdMappedStatement(key, beanClass);
            }
        }
        return null;
    }

    private MappedStatement createDeleteEntityByIdMappedStatement(String key, Class<?> beanClass) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Create deleteEntityById for " + beanClass);
        }

        MetaBean metaBean = getBeanConfiguration().getMetaBean();

        ResultMap inlineResultMap = getBeanConfiguration().getResultMap(ResultMapNameHelper.buildResultMapKey(beanClass));

        String idPropertyName = metaBean.getIdPropertyName(beanClass);

        MappedStatement.Builder msBuilder = new MappedStatement.Builder(getBeanConfiguration(), key,
                new DeleteBeansByPropertyNameSqlSource(getBeanConfiguration(), beanClass, idPropertyName), SqlCommandType.SELECT);

        msBuilder.resultMaps(Collections.singletonList(inlineResultMap));
        Cache cache = getBeanConfiguration().getCache(CacheNameHelper.buildCacheKey(beanClass));
        msBuilder.flushCacheRequired(true);
        msBuilder.cache(cache);
        msBuilder.useCache(true);
        return msBuilder.build();
    }
}
