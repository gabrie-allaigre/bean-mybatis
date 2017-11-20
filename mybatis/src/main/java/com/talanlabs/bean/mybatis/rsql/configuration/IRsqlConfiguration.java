package com.talanlabs.bean.mybatis.rsql.configuration;

import com.talanlabs.bean.mybatis.rsql.engine.ILikePolicy;
import com.talanlabs.bean.mybatis.rsql.engine.IStringPolicy;
import com.talanlabs.bean.mybatis.rsql.engine.orderby.BeanSortVisitor;
import com.talanlabs.bean.mybatis.rsql.engine.where.BeanRsqlVisitor;
import com.talanlabs.bean.mybatis.rsql.sort.SortParser;
import com.talanlabs.bean.mybatis.rsql.statement.IPageStatementFactory;
import com.talanlabs.rtext.Rtext;
import cz.jirutka.rsql.parser.RSQLParser;

public interface IRsqlConfiguration {

    /**
     * @return rtext instance
     */
    Rtext getRtext();

    /**
     * @param beanClass bean class
     * @return Return a bean visitor for bean class
     */
    <E> BeanRsqlVisitor<E> getBeanRsqlVisitor(Class<E> beanClass);

    /**
     * @param beanClass bean class
     * @return Return a bean visitor for bean class
     */
    <E> BeanSortVisitor<E> getBeanSortVisitor(Class<E> beanClass);

    /**
     * @return a RSQL Parser
     */
    RSQLParser getRsqlParser();

    /**
     * @return a Sort Parser
     */
    SortParser getSortParser();

    /**
     * @return a String compare policy
     */
    IStringPolicy getStringPolicy();

    /**
     * @return A like policy
     */
    ILikePolicy getLikePolicy();

    /**
     * @return A Page create page statement
     */
    IPageStatementFactory getPageStatementFactory();

}
