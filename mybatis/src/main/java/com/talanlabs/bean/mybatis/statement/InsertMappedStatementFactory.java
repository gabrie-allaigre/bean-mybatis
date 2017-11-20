package com.talanlabs.bean.mybatis.statement;

import com.talanlabs.bean.mybatis.annotation.Id;
import com.talanlabs.bean.mybatis.cache.CacheNameHelper;
import com.talanlabs.bean.mybatis.session.meta.MetaBean;
import com.talanlabs.bean.mybatis.session.meta.ValidationBeanException;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.session.factory.AbstractMappedStatementFactory;
import com.talanlabs.bean.mybatis.statement.sqlsource.InsertSqlSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;

public class InsertMappedStatementFactory extends AbstractMappedStatementFactory {

    private static final Logger LOG = LoggerFactory.getLogger(InsertMappedStatementFactory.class);

    public InsertMappedStatementFactory(BeanConfiguration beanConfiguration) {
        super(beanConfiguration);
    }

    @Override
    public boolean acceptKey(String key) {
        return StatementNameHelper.isInsertKey(key);
    }

    @Override
    public MappedStatement createMappedStatement(String key) {
        if (StatementNameHelper.isInsertKey(key)) {
            Class<?> beanClass = StatementNameHelper.extractBeanClassInInsertKey(key);
            if (beanClass != null) {
                try {
                    getBeanConfiguration().getMetaBean().validateBean(beanClass);
                } catch (ValidationBeanException e) {
                    throw new IllegalArgumentException("Failed to validate Bean=" + beanClass, e);
                }

                return createInsertMappedStatement(key, beanClass);
            }
        }
        return null;
    }

    private MappedStatement createInsertMappedStatement(String key, Class<?> beanClass) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Create insert for " + beanClass);
        }

        MetaBean metaBean = getBeanConfiguration().getMetaBean();

        ResultMap inlineResultMap = new ResultMap.Builder(getBeanConfiguration(), key + "-Inline", Integer.class, new ArrayList<>(), null).build();
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(getBeanConfiguration(), key, new InsertSqlSource(getBeanConfiguration(), beanClass), SqlCommandType.INSERT);
        msBuilder.resultMaps(Collections.singletonList(inlineResultMap));

        String idPropertyName = metaBean.getIdPropertyName(beanClass);
        if (idPropertyName != null) {
            Id id = metaBean.forBeanClass(beanClass).getPropertyAnnotation(idPropertyName, Id.class);

            if (StringUtils.isNotBlank(id.keyGeneratorId())) {
                KeyGenerator keyGenerator = getBeanConfiguration().getKeyGenerator(id.keyGeneratorId());
                if (keyGenerator == null) {
                    throw new IllegalArgumentException("Not found key generator for Bean=" + beanClass + " for Id=" + idPropertyName);
                }
                msBuilder.keyGenerator(keyGenerator);
            } else {
                msBuilder.keyGenerator(buildKeyGenerator(key, beanClass, idPropertyName, id.keyGeneratorClass()));
            }
        }

        Cache cache = getBeanConfiguration().getCache(CacheNameHelper.buildCacheKey(beanClass));
        msBuilder.flushCacheRequired(true);
        msBuilder.cache(cache);
        msBuilder.useCache(true);

        return msBuilder.build();
    }

    private <F extends KeyGenerator> F buildKeyGenerator(String key, Class<?> beanClass, String idPropertyName, Class<F> keyGeneratorClass) {
        try {
            Constructor<F> constructor = ConstructorUtils.getAccessibleConstructor(keyGeneratorClass, Configuration.class, String.class, Class.class, String.class);
            if (constructor != null) {
                return constructor.newInstance(getBeanConfiguration(), key, beanClass, idPropertyName);
            }
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new IllegalArgumentException("Not instancy key generator for Bean=" + beanClass + " for Id=" + idPropertyName);
        }
        try {
            return keyGeneratorClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("Not instancy key generator for Bean=" + beanClass + " for Id=" + idPropertyName);
        }
    }
}
