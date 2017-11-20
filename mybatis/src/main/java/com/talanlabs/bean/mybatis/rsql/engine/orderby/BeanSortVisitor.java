package com.talanlabs.bean.mybatis.rsql.engine.orderby;

import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.session.context.SqlContext;
import com.talanlabs.bean.mybatis.rsql.engine.IllegalPropertyException;
import com.talanlabs.bean.mybatis.rsql.engine.orderby.registry.ISortDirectionManagerRegistry;
import com.talanlabs.bean.mybatis.rsql.sort.SortNode;
import com.talanlabs.bean.mybatis.rsql.sort.SortVisitor;

import java.util.List;

public class BeanSortVisitor<E> implements SortVisitor<SqlResult, SqlContext> {

    private final Class<E> beanClass;
    private final ISortDirectionManagerRegistry sortDirectionManagerRegistry;

    public BeanSortVisitor(Class<E> beanClass, ISortDirectionManagerRegistry sortDirectionManagerRegistry) {
        super();

        this.beanClass = beanClass;
        this.sortDirectionManagerRegistry = sortDirectionManagerRegistry;
    }

    @Override
    public SqlResult visit(List<SortNode> nodes, SqlContext context) {
        return nodes.stream().map(n -> visit(n, context)).collect(SqlResult.SqlResultJoiner.joining());
    }

    @Override
    public SqlResult visit(SortNode node, SqlContext context) {
        ISortDirectionManager sortDirectionManager = sortDirectionManagerRegistry.getSortDirectionManager(node.getDirection());
        if (sortDirectionManager != null) {
            return sortDirectionManager.visit(beanClass, node, context);
        }
        throw new IllegalPropertyException(String.format("Property %s with operator %s not accepted", node.getSelector(), node.getDirection()));
    }
}