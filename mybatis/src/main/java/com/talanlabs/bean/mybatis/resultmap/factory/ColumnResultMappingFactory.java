package com.talanlabs.bean.mybatis.resultmap.factory;

import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Id;
import com.talanlabs.bean.mybatis.session.meta.MetaBean;
import com.talanlabs.bean.mybatis.session.meta.MetaInfoBean;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.util.Collections;

public class ColumnResultMappingFactory extends AbstractResultMappingFactory<Column> {

    public ColumnResultMappingFactory(BeanConfiguration beanConfiguration) {
        super(beanConfiguration, Column.class);
    }

    @Override
    public ResultMapping buildResultMapping(ResultMappingContext resultMappingContext, Class<?> beanClass,
                                                  String propertyName) {
        MetaBean metaBean = getBeanConfiguration().getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

        Column column = metaInfoBean.getPropertyAnnotation(propertyName, Column.class);

        String columnName = column.name();

        Class<?> javaType = metaBean.getColumnClass(beanClass, propertyName);

        ResultMapping.Builder resultMappingBuilder = new ResultMapping.Builder(getBeanConfiguration(), propertyName, columnName, javaType);
        if (metaInfoBean.isPropertyAnnotationPresent(propertyName, Id.class)) {
            resultMappingBuilder.flags(Collections.singletonList(ResultFlag.ID));
        }
        if (!JdbcType.UNDEFINED.equals(column.jdbcType())) {
            resultMappingBuilder.jdbcType(column.jdbcType());
        }
        if (!UnknownTypeHandler.class.equals(column.typeHandler())) {
            resultMappingBuilder.typeHandler(getBeanConfiguration().getTypeHandler(column.typeHandler()));
        }
        return resultMappingBuilder.build();
    }
}
