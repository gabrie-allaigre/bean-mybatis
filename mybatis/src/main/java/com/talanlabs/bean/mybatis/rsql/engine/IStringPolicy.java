package com.talanlabs.bean.mybatis.rsql.engine;

import com.talanlabs.bean.mybatis.rsql.sort.SortDirection;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface IStringPolicy {

    /**
     * @param beanClass  bean class
     * @param propertyName    property
     * @param operator        operator
     * @param name            name
     * @param parameterValues parameters values
     * @return For compare string, prepare name (modify sql name) and parameter
     */
    Pair<String, List<String>> prepareNameAndParametersForWhere(Class<?> beanClass, String propertyName, ComparisonOperator operator, String name,
                                                                List<String> parameterValues);

    /**
     * @param beanClass bean class
     * @param propertyName   property
     * @param sortDirection  operator
     * @param name           name
     * @return For compare string, prepare name (modify sql name)
     */
    String prepareNameForOrderBy(Class<?> beanClass, String propertyName, SortDirection sortDirection, String name);
}
