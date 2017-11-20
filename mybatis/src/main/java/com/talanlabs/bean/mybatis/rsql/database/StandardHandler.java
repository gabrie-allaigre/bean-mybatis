package com.talanlabs.bean.mybatis.rsql.database;

import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.session.context.SqlContext;
import com.talanlabs.bean.mybatis.rsql.statement.IPageStatementFactory;
import com.talanlabs.bean.mybatis.rsql.statement.Request;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.session.meta.BeanSqlResultHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;
import java.util.stream.Collectors;

public class StandardHandler implements IPageStatementFactory {

    private final BeanConfiguration beanConfiguration;

    public StandardHandler(BeanConfiguration beanConfiguration) {
        super();

        this.beanConfiguration = beanConfiguration;
    }

    @Override
    public String buildPageSql(Class<?> beanClass, SqlResult selectSqlResult, SqlResult whereSqlResult, Request.Rows rows, Map<String, Object> additionalParameters, SqlContext context) {
        BeanSqlResultHelper beanSqlResultHelper = beanConfiguration.getBeanSqlResultHelper();
        
        SQL sqlBuilder = new SQL();

        String a = StringUtils.isBlank(context.getDefaultTableName()) ? "t" : context.getDefaultTableName();
        if (!selectSqlResult.selects.isEmpty()) {
            sqlBuilder.SELECT(selectSqlResult.selects.stream().collect(Collectors.joining(",")));
        } else {
            sqlBuilder.SELECT(a + ".*");
        }

        String subSql = buildSubSql(beanClass, whereSqlResult, rows, additionalParameters, context);

        sqlBuilder.FROM("(" + subSql + ") " + a);

        beanSqlResultHelper.addJoinSql(sqlBuilder, selectSqlResult);

        if (!selectSqlResult.orderBies.isEmpty()) {
            selectSqlResult.orderBies.forEach(sqlBuilder::ORDER_BY);
        }
        return sqlBuilder.toString();
    }

    private String buildSubSql(Class<?> beanClass, SqlResult whereSqlResult, Request.Rows rows, Map<String, Object> additionalParameters, SqlContext context) {
        BeanSqlResultHelper beanSqlResultHelper = beanConfiguration.getBeanSqlResultHelper();
        
        String sql = beanSqlResultHelper.buildSelectSql(beanClass, whereSqlResult, context.getDefaultTableName());
        String limitParam = context.getNewParamName();
        String offsetParam = context.getNewParamName();
        additionalParameters.put(limitParam, rows.limit);
        additionalParameters.put(offsetParam, rows.offset);
        sql += " LIMIT #{" + limitParam + ",javaType=long} OFFSET #{" + offsetParam + ",javaType=long}";
        return sql;
    }
}
