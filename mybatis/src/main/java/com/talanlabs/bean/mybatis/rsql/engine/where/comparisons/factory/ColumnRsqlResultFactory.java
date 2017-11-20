package com.talanlabs.bean.mybatis.rsql.engine.where.comparisons.factory;

import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.session.meta.MetaBean;
import com.talanlabs.bean.mybatis.session.meta.MetaInfoBean;
import com.talanlabs.bean.mybatis.session.context.SqlContext;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.lang.reflect.Type;

public class ColumnRsqlResultFactory extends AbstractColumnRsqlResultFactory<Column> {

    public ColumnRsqlResultFactory(BeanConfiguration beanConfiguration) {
        super(beanConfiguration, Column.class);
    }

    @Override
    public SqlResult buildBeanRsqlResult(IRsqlResultContext rsqlResultContext, Class<?> beanClass, String propertyName,
                                              ComparisonNode node, String previousPropertyName, String nextPropertyName, String tableJoinName, SqlContext context) {
        MetaBean metaBean = getBeanConfiguration().getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

        Column column = metaInfoBean.getPropertyAnnotation(propertyName, Column.class);
        Class<?> javaType = metaBean.getColumnClass(beanClass, propertyName);
        Type realType = metaBean.getColumnType(beanClass, propertyName);
        JdbcType jdbcType = !JdbcType.UNDEFINED.equals(column.jdbcType()) ? column.jdbcType() : null;
        Class<? extends TypeHandler<?>> typeHandlerClass = !UnknownTypeHandler.class.equals(column.typeHandler()) ? column.typeHandler() : null;
        return buildBeanRsqlResult2(rsqlResultContext, beanClass, propertyName, node, previousPropertyName, nextPropertyName, tableJoinName, column.name(), javaType, realType, jdbcType,
                typeHandlerClass, context);
    }
}
