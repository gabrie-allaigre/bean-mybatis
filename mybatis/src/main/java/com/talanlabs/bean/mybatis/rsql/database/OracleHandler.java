package com.talanlabs.bean.mybatis.rsql.database;

import com.talanlabs.bean.mybatis.annotation.Entity;
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

public class OracleHandler implements IPageStatementFactory {

    private final BeanConfiguration beanConfiguration;

    public OracleHandler(BeanConfiguration beanConfiguration) {
        super();

        this.beanConfiguration = beanConfiguration;
    }

    @Override
    public String buildPageSql(Class<?> beanClass, SqlResult selectSqlResult, SqlResult whereSqlResult, Request.Rows rows, Map<String, Object> additionalParameters, SqlContext context) {
        BeanSqlResultHelper beanSqlResultHelper = beanConfiguration.getBeanSqlResultHelper();

        SQL sqlBuilder = new SQL();

        sqlBuilder.SELECT("i.rn");

        String a = StringUtils.isBlank(context.getDefaultTableName()) ? "t" : context.getDefaultTableName();
        if (!selectSqlResult.selects.isEmpty()) {
            sqlBuilder.SELECT(selectSqlResult.selects.stream().collect(Collectors.joining(",")));
        } else {
            sqlBuilder.SELECT(a + ".*");
        }

        String subSql = buildSubSql(beanClass, whereSqlResult, rows, additionalParameters, context);

        Entity entity = beanClass.getAnnotation(Entity.class);

        sqlBuilder.FROM("(" + subSql + ") i, " + entity.name() + " " + a);

        beanSqlResultHelper.addJoinSql(sqlBuilder, selectSqlResult);

        sqlBuilder.ORDER_BY("i.rn");
        if (!selectSqlResult.orderBies.isEmpty()) {
            selectSqlResult.orderBies.forEach(sqlBuilder::ORDER_BY);
        }
        return sqlBuilder.toString();
        /*
        return "SELECT i.rn, t.* " + "FROM (SELECT i.* " + "FROM (SELECT i.*, ROWNUM AS rn " + "FROM (SELECT ROWID AS a_rowid " + sqlFromWhereOrderBy + ") i " + "WHERE ROWNUM <= #{" + lastParam
                + ",javaType=long} " + ") i " + "WHERE rn >= #{" + firstParam + ",javaType=long} " + ") i " + ", " + entity.name() + " t " + "WHERE i.a_rowid = t.ROWID " + "ORDER BY rn";*/
    }

    private String buildSubSql(Class<?> beanClass, SqlResult whereSqlResult, Request.Rows rows, Map<String, Object> additionalParameters, SqlContext context) {
        BeanSqlResultHelper beanSqlResultHelper = beanConfiguration.getBeanSqlResultHelper();

        SqlResult.Builder builder = SqlResult.newBuilder();
        builder.appendSelect("ROWID AS a_rowid");
        builder.appendSqlResult(whereSqlResult);

        String sql = beanSqlResultHelper.buildSelectSql(beanClass, builder.build(), context.getDefaultTableName());

        long first = rows.offset + 1;
        long last = rows.offset + 1 + rows.limit;

        String firstParam = context.getNewParamName();
        String lastParam = context.getNewParamName();
        additionalParameters.put(firstParam, first);
        additionalParameters.put(lastParam, last);

        return "SELECT i.* " + "FROM (SELECT i.*, ROWNUM AS rn " + "FROM (" + sql + ") i " + "WHERE ROWNUM <= #{" + lastParam + ",javaType=long} " + ") i " + "WHERE rn >= #{" + firstParam
                + ",javaType=long}";
    }
}
