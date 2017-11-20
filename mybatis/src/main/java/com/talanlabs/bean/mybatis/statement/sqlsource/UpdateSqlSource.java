package com.talanlabs.bean.mybatis.statement.sqlsource;

import com.google.common.collect.Sets;
import com.talanlabs.bean.mybatis.annotation.*;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.session.meta.BeanSqlResultHelper;
import com.talanlabs.bean.mybatis.session.meta.MetaBean;
import com.talanlabs.bean.mybatis.session.meta.MetaInfoBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class UpdateSqlSource implements SqlSource {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateSqlSource.class);

    private final BeanConfiguration beanConfiguration;

    private final SqlSource sqlSource;

    public UpdateSqlSource(BeanConfiguration beanConfiguration, Class<?> beanClass, String[] properties, String[] nlsProperties) {
        super();

        this.beanConfiguration = beanConfiguration;

        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(beanConfiguration);

        String sql = buildUpdate(beanClass, Sets.newHashSet(properties), Sets.newHashSet(nlsProperties));
        sqlSource = sqlSourceParser.parse(sql, beanClass, null);
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return sqlSource.getBoundSql(parameterObject);
    }

    private String buildUpdate(Class<?> beanClass, Set<String> properties, Set<String> nlsProperties) {
        MetaBean metaBean = beanConfiguration.getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

        BeanSqlResultHelper beanSqlResultHelper = beanConfiguration.getBeanSqlResultHelper();

        Entity entity = beanClass.getAnnotation(Entity.class);

        Set<String> propertyNames = properties.isEmpty() ? metaInfoBean.getPropertyNames() : properties;

        SQL sqlBuilder = new SQL();
        sqlBuilder.UPDATE(entity.name());
        for (String propertyName : propertyNames) {
            if (!metaInfoBean.isPropertyAnnotationPresent(propertyName, Id.class) && !metaInfoBean.isPropertyAnnotationPresent(propertyName, Version.class)) {
                Column column = metaInfoBean.getPropertyAnnotation(propertyName, Column.class);
                if (column != null) {
                    sqlBuilder.SET(beanSqlResultHelper.buildSetColumn(beanClass, "", propertyName));
                } else {
                    NlsColumn nlsColumn = metaInfoBean.getPropertyAnnotation(propertyName, NlsColumn.class);
                    if (nlsColumn != null && nlsProperties.contains(propertyName)) {
                        sqlBuilder.SET(beanSqlResultHelper.buildSetNlsColumn(beanClass, propertyName));
                    }
                }
            }
        }

        String versionPropertyName = metaBean.getVersionPropertyName(beanClass);
        if (StringUtils.isNotBlank(versionPropertyName)) {
            sqlBuilder.SET(beanSqlResultHelper.buildSetColumn(beanClass, "", versionPropertyName) + " + 1");
        }

        String idPropertyName = metaBean.getIdPropertyName(beanClass);
        sqlBuilder.WHERE(beanSqlResultHelper.buildSetColumn(beanClass, "", idPropertyName));

        if (StringUtils.isNotBlank(versionPropertyName)) {
            sqlBuilder.WHERE(beanSqlResultHelper.buildSetColumn(beanClass, "", versionPropertyName));
        }

        String sql = sqlBuilder.toString();
        if (LOG.isDebugEnabled()) {
            LOG.debug(sql);
        }
        return sql;
    }

}
