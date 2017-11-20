package com.talanlabs.bean.mybatis.rsql.engine.where;

import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.session.context.SqlContext;
import com.talanlabs.bean.mybatis.rsql.engine.IllegalPropertyException;
import com.talanlabs.bean.mybatis.rsql.engine.where.registry.IComparisonOperatorManagerRegistry;
import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.OrNode;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;

public class BeanRsqlVisitor<E> implements RSQLVisitor<SqlResult, SqlContext> {

    private final Class<E> beanClass;
    private final IComparisonOperatorManagerRegistry comparisonOperatorManagerRegistry;

    public BeanRsqlVisitor(Class<E> beanClass, IComparisonOperatorManagerRegistry comparisonOperatorManagerRegistry) {
        super();

        this.beanClass = beanClass;
        this.comparisonOperatorManagerRegistry = comparisonOperatorManagerRegistry;
    }

    @Override
    public SqlResult visit(AndNode node, SqlContext context) {
        return node.getChildren().stream().map(n -> n.accept(this, context)).collect(SqlResult.SqlResultJoiner.joining());
    }

    @Override
    public SqlResult visit(OrNode node, SqlContext context) {
        return node.getChildren().stream().map(n -> n.accept(this, context)).collect(SqlResult.SqlResultJoiner2.joining(" OR ", "(", ")"));
    }

    @Override
    public SqlResult visit(ComparisonNode node, SqlContext context) {
        IComparisonOperatorManager comparisonOperatorManager = comparisonOperatorManagerRegistry.getComparisonOperatorManager(node.getOperator());
        if (comparisonOperatorManager != null) {
            return comparisonOperatorManager.visit(beanClass, node, context);
        }
        throw new IllegalPropertyException(String.format("Property %s with operator %s not accepted", node.getSelector(), node.getOperator()));
    }
}