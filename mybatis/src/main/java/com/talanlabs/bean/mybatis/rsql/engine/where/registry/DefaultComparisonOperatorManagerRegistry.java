package com.talanlabs.bean.mybatis.rsql.engine.where.registry;

import com.talanlabs.bean.mybatis.rsql.engine.where.IComparisonOperatorManager;
import com.talanlabs.bean.mybatis.rsql.engine.where.comparisons.StandardComparisonOperatorManager;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;

import java.util.HashMap;
import java.util.Map;

public class DefaultComparisonOperatorManagerRegistry implements IComparisonOperatorManagerRegistry {

    private final Map<ComparisonOperator, IComparisonOperatorManager> comparisonFactoryMap;

    public DefaultComparisonOperatorManagerRegistry(BeanConfiguration beanConfiguration) {
        super();

        StandardComparisonOperatorManager standardComparisonOperatorManager = new StandardComparisonOperatorManager(beanConfiguration);

        this.comparisonFactoryMap = new HashMap<>();

        registry(RSQLOperators.EQUAL, standardComparisonOperatorManager);
        registry(RSQLOperators.NOT_EQUAL, standardComparisonOperatorManager);
        registry(RSQLOperators.LESS_THAN, standardComparisonOperatorManager);
        registry(RSQLOperators.LESS_THAN_OR_EQUAL, standardComparisonOperatorManager);
        registry(RSQLOperators.GREATER_THAN, standardComparisonOperatorManager);
        registry(RSQLOperators.GREATER_THAN_OR_EQUAL, standardComparisonOperatorManager);
        registry(RSQLOperators.IN, standardComparisonOperatorManager);
        registry(RSQLOperators.NOT_IN, standardComparisonOperatorManager);
    }

    /**
     * Registry a comparison operator
     *
     * @param comparisonOperator        operator
     * @param comparisonOperatorFactory factory
     */
    public void registry(ComparisonOperator comparisonOperator, IComparisonOperatorManager comparisonOperatorFactory) {
        this.comparisonFactoryMap.put(comparisonOperator, comparisonOperatorFactory);
    }

    @Override
    public IComparisonOperatorManager getComparisonOperatorManager(ComparisonOperator comparisonOperator) {
        return comparisonFactoryMap.get(comparisonOperator);
    }
}
