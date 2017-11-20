package com.talanlabs.bean.mybatis.statement.sqlsource;

import com.talanlabs.bean.mybatis.annotation.JoinTable;
import com.talanlabs.bean.mybatis.annotation.OrderBy;
import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.session.meta.BeanSqlResultHelper;
import com.talanlabs.bean.mybatis.session.meta.MetaBean;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FindBeansByJoinTableSqlSource implements SqlSource {

    private static final Logger LOG = LoggerFactory.getLogger(FindBeansByJoinTableSqlSource.class);

    private final BeanConfiguration beanConfiguration;
    private final boolean ignoreCancel;
    private final String canceledPropertyName;
    private final SqlSource sqlSource;
    private final Map<String, Object> additionalParameters;

    public FindBeansByJoinTableSqlSource(BeanConfiguration beanConfiguration, Class<?> beanClass, Class<?> sourceBeanClass, String[] sourceProperties, String[] targetProperties, JoinTable[] joins,
            boolean ignoreCancel, OrderBy[] orderBies) {
        super();

        this.beanConfiguration = beanConfiguration;

        MetaBean metaBean = beanConfiguration.getMetaBean();
        this.canceledPropertyName = metaBean.getCanceledPropertyName(beanClass);
        this.ignoreCancel = ignoreCancel && metaBean.getCanceledPropertyName(beanClass) != null;

        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(beanConfiguration);

        SqlResult sqlResult = buildFindBeansByJoinTable(beanClass, sourceBeanClass, sourceProperties, targetProperties, joins, orderBies);

        this.additionalParameters = new HashMap<>();
        this.additionalParameters.putAll(sqlResult.parameterMap);

        String sql = toStringFindBeansByJoinTable(beanClass, sqlResult);

        sqlSource = sqlSourceParser.parse(sql, Map.class, new HashMap<>());
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
        if (ignoreCancel) {
            boundSql.setAdditionalParameter(canceledPropertyName, false);
        }
        return boundSql;
    }

    private SqlResult buildFindBeansByJoinTable(Class<?> beanClass, Class<?> sourceBeanClass, String[] sourceProperties, String[] targetProperties, JoinTable[] joinTables, OrderBy[] orderBies) {
        MetaBean metaBean = beanConfiguration.getMetaBean();
        BeanSqlResultHelper beanSqlResultHelper = beanConfiguration.getBeanSqlResultHelper();

        SqlResult.Builder builder = SqlResult.newBuilder();

        builder.appendSqlResult(beanSqlResultHelper.computeOrderBy(beanClass, "t", orderBies != null ? Arrays.asList(orderBies) : null));

        builder.appendSqlResult(beanSqlResultHelper.computeSelect(beanClass, "t"));
        builder.appendSqlResult(beanSqlResultHelper.computeJoins(beanClass, sourceBeanClass, sourceProperties, targetProperties, joinTables));
        if (ignoreCancel) {
            builder.appendWhere(beanSqlResultHelper.buildSetColumn(beanClass, "t", canceledPropertyName));
        }
        return builder.build();
    }

    private String toStringFindBeansByJoinTable(Class<?> beanClass, SqlResult sqlResult) {
        BeanSqlResultHelper beanSqlResultHelper = beanConfiguration.getBeanSqlResultHelper();
        String sql = beanSqlResultHelper.buildSelectSql(beanClass, sqlResult, "t");
        if (LOG.isDebugEnabled()) {
            LOG.debug(sql);
        }
        return sql;
    }
}