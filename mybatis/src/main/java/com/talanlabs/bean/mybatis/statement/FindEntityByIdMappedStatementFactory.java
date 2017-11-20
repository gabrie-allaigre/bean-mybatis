package com.talanlabs.bean.mybatis.statement;

import com.talanlabs.bean.mybatis.cache.CacheNameHelper;
import com.talanlabs.bean.mybatis.session.meta.MetaBean;
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

public class FindEntityByIdMappedStatementFactory extends AbstractMappedStatementFactory {

    private static final Logger LOG = LoggerFactory.getLogger(FindEntityByIdMappedStatementFactory.class);

    public FindEntityByIdMappedStatementFactory(BeanConfiguration beanConfiguration) {
        super(beanConfiguration);
    }

    @Override
    public boolean acceptKey(String key) {
        return StatementNameHelper.isFindEntityByIdKey(key);
    }

    @Override
    public MappedStatement createMappedStatement(String key) {
        if (StatementNameHelper.isFindEntityByIdKey(key)) {
            Class<?> beanClass = StatementNameHelper.extractBeanClassInFindEntityByIdKey(key);
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

                return createFindEntityByIdMappedStatement(key, beanClass);
            }
        }
        return null;
    }

    private MappedStatement createFindEntityByIdMappedStatement(String key, Class<?> beanClass) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Create findEntityById for " + beanClass);
        }

        MetaBean metaBean = getBeanConfiguration().getMetaBean();

        ResultMap inlineResultMap = getBeanConfiguration().getResultMap(ResultMapNameHelper.buildNestedResultMapKey(beanClass));

        String idPropertyName = metaBean.getIdPropertyName(beanClass);

        MappedStatement.Builder msBuilder = new MappedStatement.Builder(getBeanConfiguration(), key,
                new FindBeansByPropertyNameSqlSource(getBeanConfiguration(), beanClass, false, new String[]{idPropertyName}, null), SqlCommandType.SELECT);

        msBuilder.resultMaps(Collections.singletonList(inlineResultMap));
        Cache cache = getBeanConfiguration().getCache(CacheNameHelper.buildCacheKey(beanClass));
        msBuilder.flushCacheRequired(false);
        msBuilder.cache(cache);
        msBuilder.useCache(true);
        return msBuilder.build();
    }
}
