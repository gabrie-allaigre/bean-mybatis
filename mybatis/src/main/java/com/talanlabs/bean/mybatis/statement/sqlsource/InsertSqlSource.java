package com.talanlabs.bean.mybatis.statement.sqlsource;

import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.bean.mybatis.annotation.NlsColumn;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.session.meta.BeanSqlResultHelper;
import com.talanlabs.bean.mybatis.session.meta.MetaBean;
import com.talanlabs.bean.mybatis.session.meta.MetaInfoBean;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsertSqlSource implements SqlSource {

    private static final Logger LOG = LoggerFactory.getLogger(InsertSqlSource.class);

    private final BeanConfiguration beanConfiguration;
    private final Class<?> beanClass;
    private final SqlSourceBuilder sqlSourceParser;

    public InsertSqlSource(BeanConfiguration beanConfiguration, Class<?> beanClass) {
        super();

        this.beanConfiguration = beanConfiguration;
        this.beanClass = beanClass;
        this.sqlSourceParser = new SqlSourceBuilder(beanConfiguration);
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        SqlSource sqlSource = createSqlSource(parameterObject);
        return sqlSource.getBoundSql(parameterObject);
    }

    @SuppressWarnings("unchecked")
    private SqlSource createSqlSource(Object parameterObject) {
        try {
            String sql = buildInsert(parameterObject);
            Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
            return sqlSourceParser.parse(sql, parameterType, null);
        } catch (Exception e) {
            throw new BuilderException("Error invoking Count method for Insert Cause: " + e, e);
        }
    }

    private String buildInsert(Object bean) {
        MetaBean metaBean = beanConfiguration.getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

        BeanSqlResultHelper beanSqlResultHelper = beanConfiguration.getBeanSqlResultHelper();

        Entity entity = beanClass.getAnnotation(Entity.class);

        String versionPropertyName = metaBean.getVersionPropertyName(beanClass);
        if (versionPropertyName != null) {
            metaInfoBean.setPropertyValue(bean, versionPropertyName, 0);
        }

        SQL sqlBuilder = new SQL();
        sqlBuilder.INSERT_INTO(entity.name());

        metaInfoBean.getPropertyNames().stream().filter(propertyName -> metaInfoBean.getPropertyValue(bean, propertyName) != null).forEach(propertyName -> {
            Column column = metaInfoBean.getPropertyAnnotation(propertyName, Column.class);
            if (column != null) {
                sqlBuilder.VALUES(column.name(), beanSqlResultHelper.buildColumn(beanClass, propertyName, propertyName));
            } else {
                NlsColumn nlsColumn = metaInfoBean.getPropertyAnnotation(propertyName, NlsColumn.class);
                if (nlsColumn != null) {
                    sqlBuilder.VALUES(nlsColumn.name(), beanSqlResultHelper.buildNlsColumn(beanClass, propertyName, propertyName));
                }
            }
        });
        String sql = sqlBuilder.toString();
        if (LOG.isDebugEnabled()) {
            LOG.debug(sql);
        }
        return sql;
    }

}
