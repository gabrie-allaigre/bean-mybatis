package com.talanlabs.bean.mybatis.statement;

import com.talanlabs.bean.mybatis.annotation.NlsColumn;
import com.talanlabs.bean.mybatis.session.meta.MetaBean;
import com.talanlabs.bean.mybatis.session.meta.MetaInfoBean;
import com.talanlabs.bean.mybatis.session.meta.ValidationBeanException;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.session.factory.AbstractMappedStatementFactory;
import com.talanlabs.bean.mybatis.statement.sqlsource.FindNlsColumnSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class FindNlsColumnMappedStatementFactory extends AbstractMappedStatementFactory {

    private static final Logger LOG = LoggerFactory.getLogger(FindNlsColumnMappedStatementFactory.class);

    public FindNlsColumnMappedStatementFactory(BeanConfiguration beanConfiguration) {
        super(beanConfiguration);
    }

    @Override
    public boolean acceptKey(String key) {
        return StatementNameHelper.isFindNlsColumnKey(key);
    }

    @Override
    public MappedStatement createMappedStatement(String key) {
        if (StatementNameHelper.isFindNlsColumnKey(key)) {
            Class<?> beanClass = StatementNameHelper.extractBeanClassInFindNlsColumnKey(key);
            String propertyName = StatementNameHelper.extractPropertyNameInFindNlsColumnByKey(key);
            if (beanClass != null) {
                MetaBean metaBean = getBeanConfiguration().getMetaBean();
                MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

                try {
                    metaBean.validateBean(beanClass);
                } catch (ValidationBeanException e) {
                    throw new IllegalArgumentException("Failed to validate Bean=" + beanClass, e);
                }
                Set<String> all = metaInfoBean.getPropertyNames();
                if (!all.contains(propertyName)) {
                    throw new IllegalArgumentException("Not found property in Bean=" + beanClass + " property=" + propertyName);
                } else if (!metaInfoBean.isPropertyAnnotationPresent(propertyName, NlsColumn.class)) {
                    throw new IllegalArgumentException("Failed property is not NlsColumn in Bean=" + beanClass + " property=" + propertyName);
                }

                return createFindNlsMappedStatement(key, beanClass, propertyName);
            }
        }
        return null;
    }

    private MappedStatement createFindNlsMappedStatement(String key, Class<?> beanClass, String propertyName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Create find nls for " + beanClass);
        }

        MetaBean metaBean = getBeanConfiguration().getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

        ResultMap.Builder inlineResultMapBuilder = new ResultMap.Builder(getBeanConfiguration(), key + "-Inline", metaInfoBean.getPropertyClass(propertyName), new ArrayList<>(), null);

        MappedStatement.Builder msBuilder = new MappedStatement.Builder(getBeanConfiguration(), key, new FindNlsColumnSqlSource(getBeanConfiguration(), beanClass, propertyName),
                SqlCommandType.SELECT);
        msBuilder.resultMaps(Collections.singletonList(inlineResultMapBuilder.build()));
        msBuilder.flushCacheRequired(false);
        msBuilder.cache(null);
        msBuilder.useCache(false);
        return msBuilder.build();
    }
}
