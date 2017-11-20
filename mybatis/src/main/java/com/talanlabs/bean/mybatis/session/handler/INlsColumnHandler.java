package com.talanlabs.bean.mybatis.session.handler;

import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.session.context.SqlContext;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

public interface INlsColumnHandler {

    /**
     * Get a current context, used for cache
     *
     * @return context
     */
    Object getContext();

    /**
     * Get additional parameter
     *
     * @param beanClass bean class
     * @param propertyName   property name
     * @return map or null
     */
    Map<String, Object> getAdditionalParameter(Class<?> beanClass, String propertyName);

    /**
     * Select id for nls column, default parameter :
     * <p>
     * - tableName : Table name, String
     * <p>
     * - columnName : Column name, String
     * <p>
     * - defaultValue : Default value into table
     * <p>
     * - id : unique id
     *
     * @param beanClass bean class
     * @param propertyName   property name
     * @return select id
     */
    String getSelectNlsColumnId(Class<?> beanClass, String propertyName);

    /**
     * Update original nls column
     *
     * @param beanClass bean class
     * @param propertyName   property name
     * @return true or false
     */
    boolean isUpdateDefaultNlsColumn(Class<?> beanClass, String propertyName);

    /**
     * Merge id for nls column
     * <p>
     * - tableName : Table name, String
     * <p>
     * - columnName : Column name, String
     * <p>
     * - id : unique id
     * <p>
     * - meaning : value
     *
     * @param beanClass bean class
     * @param propertyName   property name
     * @return merge id
     */
    String getMergeNlsColumnId(Class<?> beanClass, String propertyName);

    /**
     * Delete id for nls column
     * <p>
     * - tableName : Table name, String
     * <p>
     * - id : unique id
     *
     * @param beanClass bean class
     * @return delete id
     */
    String getDeleteNlsColumnsId(Class<?> beanClass);

    /**
     * @param beanClass   bean class
     * @param propertyName     property name
     * @param previousPropertyName previous property name
     * @param tableJoinName    jointure name
     * @param context          a engine context
     * @return Build a sql name result for where
     */
    Pair<String,SqlResult> buildNameResultForWhere(Class<?> beanClass, String propertyName, String previousPropertyName, String tableJoinName, SqlContext context);

    /**
     * @param beanClass   bean class
     * @param propertyName     property name
     * @param previousPropertyName previous property name
     * @param tableJoinName    jointure name
     * @param context          a engine context
     * @return Build a sql name result for order by
     */
    Pair<String,SqlResult> buildNameResultForOrderBy(Class<?> beanClass, String propertyName, String previousPropertyName, String tableJoinName, SqlContext context);


}
