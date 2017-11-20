package com.talanlabs.bean.mybatis.rsql.engine.orderby.sorts;

import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.session.context.SqlContext;
import com.talanlabs.bean.mybatis.rsql.engine.IllegalPropertyException;
import com.talanlabs.bean.mybatis.rsql.engine.orderby.ISortDirectionManager;
import com.talanlabs.bean.mybatis.rsql.engine.orderby.sorts.factory.AssociationSortResultFactory;
import com.talanlabs.bean.mybatis.rsql.engine.orderby.sorts.factory.ColumnSortResultFactory;
import com.talanlabs.bean.mybatis.rsql.engine.orderby.sorts.factory.ISortResultFactory;
import com.talanlabs.bean.mybatis.rsql.engine.orderby.sorts.factory.NlsColumnSortResultFactory;
import com.talanlabs.bean.mybatis.rsql.sort.SortDirection;
import com.talanlabs.bean.mybatis.rsql.sort.SortNode;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class StandardSortDirectionManager implements ISortDirectionManager, ISortResultFactory.ISortResultContext {

    private List<ISortResultFactory> sortResultFactories = new ArrayList<>();

    public StandardSortDirectionManager(BeanConfiguration beanConfiguration) {
        super();

        addSortResultFactory(new ColumnSortResultFactory(beanConfiguration));
        addSortResultFactory(new NlsColumnSortResultFactory(beanConfiguration));
        addSortResultFactory(new AssociationSortResultFactory(beanConfiguration));
    }

    public void addSortResultFactory(ISortResultFactory sortResultFactory) {
        sortResultFactories.add(0, sortResultFactory);
    }

    @Override
    public SqlResult visit(Class<?> beanClass, SortNode node, SqlContext context) {
        return visit(beanClass, node.getDirection(), null, node.getSelector(), context.getDefaultTableName(), context);
    }

    @Override
    public SqlResult visit(Class<?> beanClass, SortDirection sortDirection, String previousPropertyName, String selector, String tableJoinName, SqlContext context) {
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

        for (ISortResultFactory sortResultFactory : sortResultFactories) {
            if (sortResultFactory.acceptProperty(beanClass, propertyName)) {
                return sortResultFactory.buildBeanSortResult(this, beanClass, propertyName, sortDirection, previousPropertyName, nextPropertyName, tableJoinName, context);
            }
        }

        throw new IllegalPropertyException(String.format("Property %s not accepted", current));
    }
}
