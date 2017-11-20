package com.talanlabs.bean.mybatis.rsql.engine.policy;

import com.talanlabs.bean.mybatis.rsql.engine.IStringPolicy;
import com.talanlabs.bean.mybatis.rsql.sort.SortDirection;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class NothingStringPolicy implements IStringPolicy {

    @Override
    public Pair<String, List<String>> prepareNameAndParametersForWhere(Class<?> beanClass, String propertyName, ComparisonOperator operator, String name,
            List<String> parameterValues) {
        return Pair.of(name, parameterValues);
    }

    @Override
    public String prepareNameForOrderBy(Class<?> beanClass, String propertyName, SortDirection sortDirection, String name) {
        return name;
    }
}
