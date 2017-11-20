package com.talanlabs.bean.mybatis.rsql.engine.orderby.sorts.factory;

import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.session.context.SqlContext;
import com.talanlabs.bean.mybatis.rsql.sort.SortDirection;

public interface ISortResultFactory {

    /**
     * @param beanClass    bean class
     * @param propertyName current property
     * @return true, if factory build with property
     */
    boolean acceptProperty(Class<?> beanClass, String propertyName);

    /**
     * @param sortResultContext    rsql result context
     * @param beanClass            bean class
     * @param propertyName         current property
     * @param sortDirection        current sortDirection
     * @param previousPropertyName last property name
     * @param nextPropertyName     next property name
     * @param tableJoinName        current table prefix
     * @param context              context
     * @return a bean rsql result
     */
    SqlResult buildBeanSortResult(ISortResultContext sortResultContext, Class<?> beanClass, String propertyName,
                                  SortDirection sortDirection, String previousPropertyName, String nextPropertyName, String tableJoinName, SqlContext context);

    interface ISortResultContext {

        SqlResult visit(Class<?> beanClass, SortDirection sortDirection, String previousPropertyName, String selector, String tablePrefix, SqlContext context);

    }
}
