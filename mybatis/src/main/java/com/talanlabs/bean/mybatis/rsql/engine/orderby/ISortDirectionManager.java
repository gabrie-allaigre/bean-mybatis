package com.talanlabs.bean.mybatis.rsql.engine.orderby;


import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.session.context.SqlContext;
import com.talanlabs.bean.mybatis.rsql.sort.SortNode;

public interface ISortDirectionManager {

    /**
     * Create result for direction
     *
     * @param beanClass bean class
     * @param node      current node
     * @param context   current context
     * @return a result
     */
    SqlResult visit(Class<?> beanClass, SortNode node, SqlContext context);

}
