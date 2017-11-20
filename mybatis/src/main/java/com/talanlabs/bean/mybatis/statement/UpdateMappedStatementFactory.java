package com.talanlabs.bean.mybatis.statement;

import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.NlsColumn;
import com.talanlabs.bean.mybatis.cache.CacheNameHelper;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.session.factory.AbstractMappedStatementFactory;
import com.talanlabs.bean.mybatis.session.meta.MetaBean;
import com.talanlabs.bean.mybatis.session.meta.MetaInfoBean;
import com.talanlabs.bean.mybatis.session.meta.ValidationBeanException;
import com.talanlabs.bean.mybatis.statement.sqlsource.UpdateSqlSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class UpdateMappedStatementFactory extends AbstractMappedStatementFactory {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateMappedStatementFactory.class);

    public UpdateMappedStatementFactory(BeanConfiguration beanConfiguration) {
        super(beanConfiguration);
    }

    @Override
    public boolean acceptKey(String key) {
        return StatementNameHelper.isUpdateKey(key);
    }

    @Override
    public MappedStatement createMappedStatement(String key) {
        if (StatementNameHelper.isUpdateKey(key)) {
            Class<?> beanClass = StatementNameHelper.extractBeanClassInUpdateKey(key);
            String[] properties = StatementNameHelper.extractPropertiesInUpdateKey(key);
            String[] nlsProperties = StatementNameHelper.extractNlsPropertiesInUpdateKey(key);
            if (beanClass != null && properties != null && nlsProperties != null) {
                MetaBean metaBean = getBeanConfiguration().getMetaBean();
                MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

                try {
                    metaBean.validateBean(beanClass);
                } catch (ValidationBeanException e) {
                    throw new IllegalArgumentException("Failed to validate Bean=" + beanClass, e);
                }
                String idPropertyName = metaBean.getIdPropertyName(beanClass);
                if (StringUtils.isBlank(idPropertyName)) {
                    throw new IllegalArgumentException("Not found annotation Id for Bean=" + beanClass);
                }
                Set<String> all = metaInfoBean.getPropertyNames();
                for (String property : properties) {
                    if (!all.contains(property)) {
                        throw new IllegalArgumentException("Not found property in Bean=" + beanClass + " property=" + property);
                    } else if (!metaInfoBean.isPropertyAnnotationPresent(property, Column.class) && !metaInfoBean.isPropertyAnnotationPresent(property, NlsColumn.class)) {
                        throw new IllegalArgumentException("Failed property is not Column or NlsColumn in Bean=" + beanClass + " property=" + property);
                    }
                }
                for (String nlsProperty : nlsProperties) {
                    if (!all.contains(nlsProperty)) {
                        throw new IllegalArgumentException("Not found property in Bean=" + beanClass + " property=" + nlsProperty);
                    } else if (!metaInfoBean.isPropertyAnnotationPresent(nlsProperty, NlsColumn.class)) {
                        throw new IllegalArgumentException("Failed property is not NlsColumn in Bean=" + beanClass + " property=" + nlsProperty);
                    }
                }

                return createUpdateMappedStatement(key, beanClass,properties, nlsProperties);
            }
        }
        return null;
    }

    private MappedStatement createUpdateMappedStatement(String key, Class<?> beanClass, String[] properties, String[] nlsProperties) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Create update for " + beanClass);
        }

        MetaBean metaBean = getBeanConfiguration().getMetaBean();

        ResultMap inlineResultMap = new ResultMap.Builder(getBeanConfiguration(), key + "-Inline", Integer.class, new ArrayList<>(), null).build();
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(getBeanConfiguration(), key, new UpdateSqlSource(getBeanConfiguration(), beanClass,properties, nlsProperties), SqlCommandType.UPDATE);
        msBuilder.resultMaps(Collections.singletonList(inlineResultMap));

        String versionPropertyName = metaBean.getVersionPropertyName(beanClass);
        if (versionPropertyName != null) {
            msBuilder.keyGenerator(new VersionKeyGenerator(beanClass, versionPropertyName));
        }

        Cache cache = getBeanConfiguration().getCache(CacheNameHelper.buildCacheKey(beanClass));
        msBuilder.flushCacheRequired(true);
        msBuilder.cache(cache);
        msBuilder.useCache(true);

        return msBuilder.build();
    }

    private class VersionKeyGenerator implements KeyGenerator {

        private final Class<?> beanClass;
        private final String versionPropertyName;

        public VersionKeyGenerator(Class<?> beanClass, String versionPropertyName) {
            super();

            this.beanClass = beanClass;
            this.versionPropertyName = versionPropertyName;
        }

        @Override
        public void processBefore(Executor executor, MappedStatement mappedStatement, Statement statement, Object parameter) {
        }

        @Override
        public void processAfter(Executor executor, MappedStatement mappedStatement, Statement statement, Object parameter) {
            MetaBean metaBean = getBeanConfiguration().getMetaBean();
            MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

            Object value = metaInfoBean.getPropertyValue(parameter, versionPropertyName);
            if (value instanceof Integer) {
                metaInfoBean.setPropertyValue(parameter, versionPropertyName, (Integer) value + 1);
            } else if (value instanceof Long) {
                metaInfoBean.setPropertyValue(parameter, versionPropertyName, (Long) value + 1L);
            }
        }
    }
}
