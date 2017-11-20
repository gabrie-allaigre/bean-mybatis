package com.talanlabs.bean.mybatis.rsql.statement.sqlsource;

import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.rsql.configuration.IRsqlConfiguration;
import com.talanlabs.bean.mybatis.session.context.SqlContext;
import com.talanlabs.bean.mybatis.rsql.statement.Request;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.session.meta.BeanSqlResultHelper;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class CountRsqlSqlSource extends AbstractRsqlSqlSource {

    private static final Logger LOG = LoggerFactory.getLogger(CountRsqlSqlSource.class);

    public CountRsqlSqlSource(BeanConfiguration beanConfiguration, Class<?> beanClass) {
        super(beanConfiguration, beanClass);
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        BeanSqlResultHelper beanSqlResultHelper = beanConfiguration.getBeanSqlResultHelper();

        IRsqlConfiguration rsqlConfiguration = beanConfiguration.getRsqlConfiguration();

        String rsql;
        Request.ICustomRequest customRequest = null;
        String sort = null;
        Request.ICustomSort customSortLeft = null;
        Request.ICustomSort customSortRight = null;
        if (parameterObject instanceof Request) {
            Request request = (Request) parameterObject;
            rsql = request.getRsql();
            customRequest = request.getCustomRequest();
            sort = request.getSort();
            customSortLeft = request.getCustomSortLeft();
            customSortRight = request.getCustomSortRight();
        } else {
            rsql = (String) parameterObject;
        }

        Map<String, Object> additionalParameters = new HashMap<>();
        SqlContext context = SqlContext.newBulder().build();

        SqlResult.Builder builder = SqlResult.newBuilder();
        builder.appendSelect("count(0)");
        builder.appendSqlResult(buildSqlFromWhereOrderBy(rsql, customRequest, sort, customSortLeft, customSortRight, context));
        SqlResult sqlResult = builder.build();

        if (sqlResult.parameterMap != null) {
            additionalParameters.putAll(sqlResult.parameterMap);
        }

        String sql = beanSqlResultHelper.buildSelectSql(beanClass, sqlResult, context.getDefaultTableName());
        if (LOG.isDebugEnabled()) {
            LOG.debug(sql);
        }

        SqlSource sqlSource = sqlSourceParser.parse(sql, String.class, additionalParameters);
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
        additionalParameters.forEach(boundSql::setAdditionalParameter);
        return boundSql;
    }
}
