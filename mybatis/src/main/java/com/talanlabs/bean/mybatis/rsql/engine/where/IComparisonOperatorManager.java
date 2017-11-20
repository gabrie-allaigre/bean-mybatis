package com.talanlabs.bean.mybatis.rsql.engine.where;

import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.session.context.SqlContext;
import cz.jirutka.rsql.parser.ast.ComparisonNode;

public interface IComparisonOperatorManager {

    /**
     * Create result for node
     *
     * @param beanClass bean class
     * @param node      current node
     * @param context   current context
     * @return a result
     */
    SqlResult visit(Class<?> beanClass, ComparisonNode node, SqlContext context);

}
