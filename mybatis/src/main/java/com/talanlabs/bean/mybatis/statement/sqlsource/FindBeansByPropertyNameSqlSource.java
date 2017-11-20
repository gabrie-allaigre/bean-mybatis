package com.talanlabs.bean.mybatis.statement.sqlsource;

import com.talanlabs.bean.mybatis.annotation.OrderBy;
import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.session.meta.BeanSqlResultHelper;
import com.talanlabs.bean.mybatis.session.meta.MetaBean;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FindBeansByPropertyNameSqlSource implements SqlSource {

    private static final Logger LOG = LoggerFactory.getLogger(FindBeansByPropertyNameSqlSource.class);

    private final BeanConfiguration beanConfiguration;
    private final boolean ignoreCancel;
    private final String canceledPropertyName;
    private final SqlSource sqlSource;
    private final Map<String, Object> additionalParameters;

    public FindBeansByPropertyNameSqlSource(BeanConfiguration beanConfiguration, Class<?> beanClass, boolean ignoreCancel, String[] propertyNames, OrderBy[] orderBies) {
        super();

        this.beanConfiguration = beanConfiguration;

        MetaBean metaBean = beanConfiguration.getMetaBean();

        this.canceledPropertyName = metaBean.getCanceledPropertyName(beanClass);
        this.ignoreCancel = ignoreCancel && StringUtils.isNotBlank(canceledPropertyName);

        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(beanConfiguration);

        SqlResult sqlResult = buildFindsByPropertyName(beanClass, propertyNames, orderBies);

        this.additionalParameters = new HashMap<>();
        this.additionalParameters.putAll(sqlResult.parameterMap);

        String sql = toStringFindsByPropertyName(beanClass, sqlResult);
        this.sqlSource = sqlSourceParser.parse(sql, Map.class, new HashMap<>());
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
        if (ignoreCancel) {
            boundSql.setAdditionalParameter(canceledPropertyName, false);
        }
        if (!this.additionalParameters.isEmpty()) {
            this.additionalParameters.forEach(boundSql::setAdditionalParameter);
        }
        return boundSql;
    }

    private SqlResult buildFindsByPropertyName(Class<?> beanClass, String[] propertyNames, OrderBy[] orderBies) {
        MetaBean metaBean = beanConfiguration.getMetaBean();
        BeanSqlResultHelper beanSqlResultHelper = beanConfiguration.getBeanSqlResultHelper();

        SqlResult.Builder builder = SqlResult.newBuilder();

        builder.appendSqlResult(beanSqlResultHelper.computeOrderBy(beanClass, "t", orderBies != null ? Arrays.asList(orderBies) : null));

        builder.appendSqlResult(beanSqlResultHelper.computeSelect(beanClass, "t"));
        builder.appendSqlResult(beanSqlResultHelper.computeWhere(beanClass, "t", propertyNames));
        if (ignoreCancel) {
            builder.appendWhere(beanSqlResultHelper.buildSetColumn(beanClass, "t", canceledPropertyName));
        }

        return builder.build();
    }

    private String toStringFindsByPropertyName(Class<?> beanClass, SqlResult sqlResult) {
        BeanSqlResultHelper beanSqlResultHelper = beanConfiguration.getBeanSqlResultHelper();
        String sql = beanSqlResultHelper.buildSelectSql(beanClass, sqlResult, "t");
        if (LOG.isDebugEnabled()) {
            LOG.debug(sql);
        }
        return sql;
    }
}