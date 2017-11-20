package com.talanlabs.bean.mybatis.statement.sqlsource;

import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.session.meta.BeanSqlResultHelper;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class DeleteBeansByPropertyNameSqlSource implements SqlSource {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteBeansByPropertyNameSqlSource.class);

    private final BeanConfiguration beanConfiguration;

    private final SqlSource sqlSource;

    public DeleteBeansByPropertyNameSqlSource(BeanConfiguration beanConfiguration, Class<?> beanClass, String... propertyNames) {
        super();

        this.beanConfiguration = beanConfiguration;

        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(beanConfiguration);

        String sql = buildDeleteBeansByPropertyName(beanClass, propertyNames);
        sqlSource = sqlSourceParser.parse(sql, Map.class, new HashMap<>());
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return sqlSource.getBoundSql(parameterObject);
    }

    private String buildDeleteBeansByPropertyName(Class<?> beanClass, String... propertyNames) {
        BeanSqlResultHelper beanSqlResultHelper = beanConfiguration.getBeanSqlResultHelper();

        SqlResult sqlResult = beanSqlResultHelper.computeWhere(beanClass, "t", propertyNames);

        String sql = beanSqlResultHelper.buildDeleteSql(beanClass, sqlResult, "t");
        if (LOG.isDebugEnabled()) {
            LOG.debug(sql);
        }
        return sql;
    }
}