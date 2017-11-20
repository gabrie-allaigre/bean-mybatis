package com.talanlabs.bean.mybatis.rsql.engine.where.comparisons.factory;

import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.session.context.SqlContext;
import cz.jirutka.rsql.parser.ast.ComparisonNode;

public interface IRsqlResultFactory {

    /**
     * @param beanClass bean descriptor
     * @param propertyName        current property
     * @return true, if factory build with property
     */
    boolean acceptProperty(Class<?> beanClass, String propertyName);

    /**
     * @param rsqlResultContext    rsql result context
     * @param beanClass            bean descriptor
     * @param propertyName         current property
     * @param node                 current node
     * @param previousPropertyName last property name
     * @param nextPropertyName     next property name
     * @param tableJoinName        current table prefix
     * @param context              context
     * @return a bean rsql result
     */
    SqlResult buildBeanRsqlResult(IRsqlResultContext rsqlResultContext, Class<?> beanClass, String propertyName, ComparisonNode node,
                                       String previousPropertyName, String nextPropertyName, String tableJoinName, SqlContext context);

    interface IRsqlResultContext {

        SqlResult visit(Class<?> beanClass, ComparisonNode node, String previousPropertyName, String selector, String tablePrefix, SqlContext context);

    }
}
