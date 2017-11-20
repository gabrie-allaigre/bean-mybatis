package com.talanlabs.bean.mybatis.rsql.engine.orderby.sorts.factory;

import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.session.meta.MetaBean;
import com.talanlabs.bean.mybatis.session.meta.MetaInfoBean;
import com.talanlabs.bean.mybatis.session.context.SqlContext;
import com.talanlabs.bean.mybatis.rsql.sort.SortDirection;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

public class ColumnSortResultFactory extends AbstractColumnSortResultFactory<Column> {

    public ColumnSortResultFactory(BeanConfiguration beanConfiguration) {
        super(beanConfiguration, Column.class);
    }

    @Override
    public SqlResult buildBeanSortResult(ISortResultContext sortResultContext, Class<?> beanClass, String propertyName, SortDirection sortDirection, String previousPropertyName, String nextPropertyName, String tableJoinName, SqlContext context) {
        MetaBean metaBean = getBeanConfiguration().getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

        Column column = metaInfoBean.getPropertyAnnotation(propertyName, Column.class);
        Class<?> javaType = metaBean.getColumnClass(beanClass, propertyName);
        JdbcType jdbcType = !JdbcType.UNDEFINED.equals(column.jdbcType()) ? column.jdbcType() : null;
        Class<? extends TypeHandler<?>> typeHandlerClass = !UnknownTypeHandler.class.equals(column.typeHandler()) ? column.typeHandler() : null;
        return buildBeanSortResult2(sortResultContext, beanClass, propertyName, sortDirection, previousPropertyName, nextPropertyName, tableJoinName, column.name(), javaType,
                jdbcType, typeHandlerClass, context);
    }
}
