package com.talanlabs.bean.mybatis.rsql.engine.policy;

import com.talanlabs.bean.mybatis.rsql.engine.IStringPolicy;
import com.talanlabs.bean.mybatis.rsql.sort.SortDirection;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.stream.Collectors;

public class AlwaysUpperStringPolicy implements IStringPolicy {

    private final String upperSqlFunction;

    public AlwaysUpperStringPolicy() {
        this("UPPER");
    }

    public AlwaysUpperStringPolicy(String upperSqlFunction) {
        super();

        this.upperSqlFunction = upperSqlFunction;
    }

    @Override
    public Pair<String, List<String>> prepareNameAndParametersForWhere(Class<?> beanClass, String propertyName, ComparisonOperator operator, String name,
            List<String> parameterValues) {
        List<String> ls = null;
        if (parameterValues != null) {
            ls = parameterValues.stream().map(this::upperParameterValue).collect(Collectors.toList());
        }
        return Pair.of(upperName(name), ls);
    }

    @Override
    public String prepareNameForOrderBy(Class<?> beanClass, String propertyName, SortDirection sortDirection, String name) {
        return upperName(name);
    }

    protected String upperName(String name) {
        return upperSqlFunction + "(" + name + ")";
    }

    protected String upperParameterValue(String parameterValue) {
        return StringUtils.upperCase(parameterValue);
    }
}
