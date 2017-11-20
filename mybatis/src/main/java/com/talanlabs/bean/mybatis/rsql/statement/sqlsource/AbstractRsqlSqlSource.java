package com.talanlabs.bean.mybatis.rsql.statement.sqlsource;

import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.rsql.configuration.IRsqlConfiguration;
import com.talanlabs.bean.mybatis.session.context.SqlContext;
import com.talanlabs.bean.mybatis.rsql.engine.orderby.BeanSortVisitor;
import com.talanlabs.bean.mybatis.rsql.engine.where.BeanRsqlVisitor;
import com.talanlabs.bean.mybatis.rsql.sort.SortParser;
import com.talanlabs.bean.mybatis.rsql.statement.Request;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import cz.jirutka.rsql.parser.RSQLParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.SqlSource;

public abstract class AbstractRsqlSqlSource implements SqlSource {

    protected final BeanConfiguration beanConfiguration;
    protected final Class<?> beanClass;
    protected final SqlSourceBuilder sqlSourceParser;

    public AbstractRsqlSqlSource(BeanConfiguration beanConfiguration, Class<?> beanClass) {
        super();

        this.beanConfiguration = beanConfiguration;
        this.beanClass = beanClass;
        this.sqlSourceParser = new SqlSourceBuilder(beanConfiguration);
    }

    protected SqlResult buildSqlFromWhereOrderBy(String rsql, Request.ICustomRequest customRequest, String sort, Request.ICustomSort customSortLeft, Request.ICustomSort customSortRight,
                                                 SqlContext context) {
        IRsqlConfiguration rsqlConfiguration = beanConfiguration.getRsqlConfiguration();

        SqlResult.Builder builder = SqlResult.newBuilder();

        if (StringUtils.isNotBlank(rsql)) {
            BeanRsqlVisitor<?> beanRsqlVisitor = rsqlConfiguration.getBeanRsqlVisitor(beanClass);
            RSQLParser rsqlParser = rsqlConfiguration.getRsqlParser();
            builder.appendSqlResult(rsqlParser.parse(rsql).accept(beanRsqlVisitor, context));
        }

        if (customRequest != null) {
            builder.appendSqlResult(customRequest.buildSqlResult(context));
        }

        if (customSortLeft != null) {
            builder.appendSqlResult(customSortLeft.buildSqlResult(context));
        }

        if (StringUtils.isNotBlank(sort)) {
            BeanSortVisitor<?> beanSortVisitor = rsqlConfiguration.getBeanSortVisitor(beanClass);
            SortParser sortParser = rsqlConfiguration.getSortParser();
            builder.appendSqlResult(beanSortVisitor.visit(sortParser.parse(sort), context));
        }

        if (customSortRight != null) {
            builder.appendSqlResult(customSortRight.buildSqlResult(context));
        }

        return builder.build();
    }
}
