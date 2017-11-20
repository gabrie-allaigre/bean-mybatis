package com.talanlabs.bean.mybatis.rsql.engine.orderby.registry;


import com.talanlabs.bean.mybatis.rsql.engine.orderby.ISortDirectionManager;
import com.talanlabs.bean.mybatis.rsql.engine.orderby.sorts.StandardSortDirectionManager;
import com.talanlabs.bean.mybatis.rsql.sort.SortDirection;
import com.talanlabs.bean.mybatis.rsql.sort.SortDirections;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;

import java.util.HashMap;
import java.util.Map;

public class DefaultSortDirectionManagerRegistry implements ISortDirectionManagerRegistry {

    private final Map<SortDirection, ISortDirectionManager> comparisonFactoryMap;

    public DefaultSortDirectionManagerRegistry(BeanConfiguration beanConfiguration) {
        super();

        StandardSortDirectionManager standardSortDirectionManager = new StandardSortDirectionManager(beanConfiguration);

        this.comparisonFactoryMap = new HashMap<>();

        registry(SortDirections.ASC, standardSortDirectionManager);
        registry(SortDirections.ASC_NULLS_FIRST, standardSortDirectionManager);
        registry(SortDirections.ASC_NULLS_LAST, standardSortDirectionManager);
        registry(SortDirections.DESC, standardSortDirectionManager);
        registry(SortDirections.DESC_NULLS_FIRST, standardSortDirectionManager);
        registry(SortDirections.DESC_NULLS_LAST, standardSortDirectionManager);
    }

    /**
     * Registry a sort direction
     *
     * @param sortDirection        direction
     * @param sortDirectionFactory factory
     */
    public void registry(SortDirection sortDirection, ISortDirectionManager sortDirectionFactory) {
        this.comparisonFactoryMap.put(sortDirection, sortDirectionFactory);
    }

    @Override
    public ISortDirectionManager getSortDirectionManager(SortDirection sortDirection) {
        return comparisonFactoryMap.get(sortDirection);
    }
}
