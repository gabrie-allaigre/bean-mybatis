package com.talanlabs.bean.mybatis.rsql.statement.sqlsource;

import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.rsql.configuration.IRsqlConfiguration;
import com.talanlabs.bean.mybatis.session.context.SqlContext;
import com.talanlabs.bean.mybatis.rsql.statement.IPageStatementFactory;
import com.talanlabs.bean.mybatis.rsql.statement.Request;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.session.meta.BeanSqlResultHelper;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class RsqlSqlSource extends AbstractRsqlSqlSource {

    private static final Logger LOG = LoggerFactory.getLogger(RsqlSqlSource.class);

    public RsqlSqlSource(BeanConfiguration beanConfiguration, Class<?> beanClass) {
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
        Request.Rows rows = null;
        if (parameterObject instanceof Request) {
            Request request = (Request) parameterObject;
            rsql = request.getRsql();
            customRequest = request.getCustomRequest();
            sort = request.getSort();
            customSortLeft = request.getCustomSortLeft();
            customSortRight = request.getCustomSortRight();
            rows = request.getRows();
        } else {
            rsql = (String) parameterObject;
        }

        Map<String, Object> additionalParameters = new HashMap<>();
        SqlContext context = SqlContext.newBulder().build();

        String sql;
        if (rows != null) {
            IPageStatementFactory pageStatementFactory = rsqlConfiguration.getPageStatementFactory();
            if (pageStatementFactory == null) {
                throw new IllegalArgumentException("Failed to build SQL for bean=" + beanClass + " rows is not null but PageStatementFactory not found");
            }

            SqlResult sqlResult1 = beanSqlResultHelper.computeSelect(beanClass, context.getDefaultTableName());
            SqlResult sqlResult2 = buildSqlFromWhereOrderBy(rsql, customRequest, sort, customSortLeft, customSortRight, context);

            if (sqlResult1.parameterMap != null) {
                additionalParameters.putAll(sqlResult1.parameterMap);
            }
            if (sqlResult2.parameterMap != null) {
                additionalParameters.putAll(sqlResult2.parameterMap);
            }

            sql = pageStatementFactory.buildPageSql(beanClass, sqlResult1, sqlResult2, rows, additionalParameters, context);
        } else {
            SqlResult.Builder builder = SqlResult.newBuilder();
            builder.appendSqlResult(beanSqlResultHelper.computeSelect(beanClass, context.getDefaultTableName()));
            builder.appendSqlResult(buildSqlFromWhereOrderBy(rsql, customRequest, sort, customSortLeft, customSortRight, context));
            SqlResult sqlResult = builder.build();

            if (sqlResult.parameterMap != null) {
                additionalParameters.putAll(sqlResult.parameterMap);
            }

            sql = beanSqlResultHelper.buildSelectSql(beanClass, sqlResult, context.getDefaultTableName());
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug(sql);
        }

        SqlSource sqlSource = sqlSourceParser.parse(sql, String.class, additionalParameters);
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
        additionalParameters.forEach(boundSql::setAdditionalParameter);
        return boundSql;
    }
}
