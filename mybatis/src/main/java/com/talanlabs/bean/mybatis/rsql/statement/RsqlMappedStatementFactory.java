package com.talanlabs.bean.mybatis.rsql.statement;

import com.talanlabs.bean.mybatis.cache.CacheNameHelper;
import com.talanlabs.bean.mybatis.session.meta.ValidationBeanException;
import com.talanlabs.bean.mybatis.resultmap.ResultMapNameHelper;
import com.talanlabs.bean.mybatis.rsql.statement.sqlsource.RsqlSqlSource;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.session.factory.AbstractMappedStatementFactory;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class RsqlMappedStatementFactory extends AbstractMappedStatementFactory {

    private static final Logger LOG = LoggerFactory.getLogger(RsqlMappedStatementFactory.class);

    public RsqlMappedStatementFactory(BeanConfiguration beanConfiguration) {
        super(beanConfiguration);
    }

    @Override
    public boolean acceptKey(String key) {
        return RsqlStatementNameHelper.isRsqlKey(key);
    }

    @Override
    public MappedStatement createMappedStatement(String key) {
        if (RsqlStatementNameHelper.isRsqlKey(key)) {
            Class<?> beanClass = RsqlStatementNameHelper.extractBeanClassInRsqlKey(key);
            if (beanClass != null) {
                try {
                    getBeanConfiguration().getMetaBean().validateBean(beanClass);
                } catch (ValidationBeanException e) {
                    throw new IllegalArgumentException("Failed to validate Bean=" + beanClass, e);
                }

                return createRsqlMappedStatement(key, beanClass);
            }
        }
        return null;
    }

    private MappedStatement createRsqlMappedStatement(String key, Class<?> beanClass) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Create rsql for " + beanClass);
        }

        ResultMap inlineResultMap = getBeanConfiguration().getResultMap(ResultMapNameHelper.buildNestedResultMapKey(beanClass));

        MappedStatement.Builder msBuilder = new MappedStatement.Builder(getBeanConfiguration(), key, new RsqlSqlSource(getBeanConfiguration(), beanClass),
                SqlCommandType.SELECT);

        msBuilder.resultMaps(Collections.singletonList(inlineResultMap));
        Cache cache = getBeanConfiguration().getCache(CacheNameHelper.buildCacheKey(beanClass));
        msBuilder.flushCacheRequired(false);
        msBuilder.cache(cache);
        msBuilder.useCache(true);
        return msBuilder.build();
    }
}
