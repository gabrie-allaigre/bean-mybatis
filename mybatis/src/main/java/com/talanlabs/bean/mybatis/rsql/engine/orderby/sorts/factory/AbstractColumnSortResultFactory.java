package com.talanlabs.bean.mybatis.rsql.engine.orderby.sorts.factory;

import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.session.context.SqlContext;
import com.talanlabs.bean.mybatis.rsql.engine.IStringPolicy;
import com.talanlabs.bean.mybatis.rsql.sort.SortDirection;
import com.talanlabs.bean.mybatis.rsql.sort.SortDirections;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractColumnSortResultFactory<E extends Annotation> extends AbstractSortResultFactory<E> {

    private final Map<SortDirection, DirectionConvert> directionConvertMap;

    public AbstractColumnSortResultFactory(BeanConfiguration beanConfiguration, Class<E> annotationClass) {
        super(beanConfiguration, annotationClass);

        this.directionConvertMap = new HashMap<>();
        addOperatorConvert(SortDirections.ASC, DirectionConvert.of("ASC"));
        addOperatorConvert(SortDirections.ASC_NULLS_LAST, DirectionConvert.of("ASC NULLS LAST"));
        addOperatorConvert(SortDirections.ASC_NULLS_FIRST, DirectionConvert.of("ASC NULLS FIRST"));
        addOperatorConvert(SortDirections.DESC, DirectionConvert.of("DESC"));
        addOperatorConvert(SortDirections.DESC_NULLS_LAST, DirectionConvert.of("DESC NULLS LAST"));
        addOperatorConvert(SortDirections.DESC_NULLS_FIRST, DirectionConvert.of("DESC NULLS FIRST"));
    }

    public void addOperatorConvert(SortDirection sortDirection, DirectionConvert directionConvert) {
        directionConvertMap.put(sortDirection, directionConvert);
    }

    protected SqlResult buildBeanSortResult2(ISortResultContext sortResultContext, Class<?> beanClass, String propertyName,
                                                 SortDirection sortDirection, String previousPropertyName, String nextPropertyName, String tableJoinName, String columnName, Class<?> javaType, JdbcType jdbcType,
                                                 Class<? extends TypeHandler<?>> typeHandlerClass, SqlContext context) {
        DirectionConvert type = directionConvertMap.get(sortDirection);
        if (type == null) {
            throw new IllegalArgumentException("SortDirection not define convert for Bean=" + beanClass + " with property=" + propertyName);
        }

        String name = (StringUtils.isNotBlank(tableJoinName) ? tableJoinName + "." : "") + columnName;
        IStringPolicy stringComparePolicy = getRsqlConfiguration().getStringPolicy();

        if (stringComparePolicy != null && String.class == javaType) {
            name = stringComparePolicy.prepareNameForOrderBy(beanClass, propertyName, sortDirection, name);
        }

        return parseString(type, name, context);
    }

    private SqlResult parseString(DirectionConvert directionConvert, String name, SqlContext context) {
        return SqlResult.newBuilder().appendOrderBy(name + " " + directionConvert.sql).build();
    }

    public static class DirectionConvert {

        public final String sql;

        private DirectionConvert(String sql) {
            this.sql = sql;
        }

        public static DirectionConvert of(String sql) {
            return new DirectionConvert(sql);
        }

    }
}
