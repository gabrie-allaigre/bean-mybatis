package com.talanlabs.bean.mybatis.rsql.statement;


import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.session.context.SqlContext;

import java.util.Map;

public interface IPageStatementFactory {

    /**
     * Build page statement
     *
     * @param beanClass            current bean
     * @param selectSqlResult      select sql result
     * @param whereSqlResult       where sql result
     * @param rows                 current rows
     * @param additionalParameters params
     * @param context              engine context
     * @return a sql statement
     */
    String buildPageSql(Class<?> beanClass, SqlResult selectSqlResult, SqlResult whereSqlResult, Request.Rows rows, Map<String, Object> additionalParameters, SqlContext context);

}
