package com.talanlabs.bean.mybatis.rsql.engine.where.comparisons;

import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.session.context.SqlContext;
import com.talanlabs.bean.mybatis.rsql.engine.IllegalPropertyException;
import com.talanlabs.bean.mybatis.rsql.engine.where.IComparisonOperatorManager;
import com.talanlabs.bean.mybatis.rsql.engine.where.comparisons.factory.AssociationRsqlResultFactory;
import com.talanlabs.bean.mybatis.rsql.engine.where.comparisons.factory.ColumnRsqlResultFactory;
import com.talanlabs.bean.mybatis.rsql.engine.where.comparisons.factory.IRsqlResultFactory;
import com.talanlabs.bean.mybatis.rsql.engine.where.comparisons.factory.NlsColumnRsqlResultFactory;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class StandardComparisonOperatorManager implements IComparisonOperatorManager, IRsqlResultFactory.IRsqlResultContext {

    private List<IRsqlResultFactory> rsqlResultFactories = new ArrayList<>();

    public StandardComparisonOperatorManager(BeanConfiguration beanConfiguration) {
        super();

        addRsqlResultFactory(new ColumnRsqlResultFactory(beanConfiguration));
        addRsqlResultFactory(new NlsColumnRsqlResultFactory(beanConfiguration));
        addRsqlResultFactory(new AssociationRsqlResultFactory(beanConfiguration));
    }

    public void addRsqlResultFactory(IRsqlResultFactory rsqlResultFactory) {
        rsqlResultFactories.add(0, rsqlResultFactory);
    }

    @Override
    public SqlResult visit(Class<?> beanClass, ComparisonNode node, SqlContext context) {
        return visit(beanClass, node, null, node.getSelector(), context.getDefaultTableName(), context);
    }

    @Override
    public SqlResult visit(Class<?> beanClass, ComparisonNode node, String previousPropertyName, String selector, String tableJoinName, SqlContext context) {
        int indexPoint = selector.indexOf(".");
        String propertyName;
        String nextPropertyName;
        if (indexPoint == -1) {
            propertyName = selector;
            nextPropertyName = null;
        } else {
            propertyName = selector.substring(0, indexPoint);
            nextPropertyName = selector.substring(indexPoint + 1);
        }

        String current = (StringUtils.isNotBlank(previousPropertyName) ? previousPropertyName + "." : "") + propertyName;

        for (IRsqlResultFactory rsqlResultFactory : rsqlResultFactories) {
            if (rsqlResultFactory.acceptProperty(beanClass, propertyName)) {
                return rsqlResultFactory.buildBeanRsqlResult(this, beanClass, propertyName, node, previousPropertyName, nextPropertyName, tableJoinName, context);
            }
        }

        throw new IllegalPropertyException(String.format("Property %s not accepted", current));
    }
}
