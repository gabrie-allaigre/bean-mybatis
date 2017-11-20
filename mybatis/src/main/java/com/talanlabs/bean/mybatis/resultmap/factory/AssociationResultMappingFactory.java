package com.talanlabs.bean.mybatis.resultmap.factory;

import com.google.common.collect.Sets;
import com.talanlabs.bean.mybatis.annotation.Association;
import com.talanlabs.bean.mybatis.annotation.FetchType;
import com.talanlabs.bean.mybatis.annotation.JoinTable;
import com.talanlabs.bean.mybatis.session.meta.MetaBean;
import com.talanlabs.bean.mybatis.session.meta.MetaInfoBean;
import com.talanlabs.bean.mybatis.resultmap.ResultMapNameHelper;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.statement.StatementNameHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.ResultMapping;

public class AssociationResultMappingFactory extends AbstractResultMappingFactory<Association> {

    public AssociationResultMappingFactory(BeanConfiguration beanConfiguration) {
        super(beanConfiguration, Association.class);
    }

    @Override
    public ResultMapping buildResultMapping(ResultMappingContext resultMappingContext, Class<?> beanClass, String propertyName) {
        MetaInfoBean metaInfoBean = getBeanConfiguration().getMetaBean().forBeanClass(beanClass);

        Association association = metaInfoBean.getPropertyAnnotation(propertyName, Association.class);

        if (FetchType.EAGER.equals(association.fetchType()) && resultMappingContext.getDepth() != 0 && association.nestedOption().depth() != 0) {
            return buildNestedResultMapping(resultMappingContext, beanClass, propertyName, association);
        } else {
            return buildDefaultResultMapping(beanClass, propertyName, association);
        }
    }

    @SuppressWarnings("unchecked")
    private ResultMapping buildNestedResultMapping(ResultMappingContext resultMappingContext, Class<?> beanClass, String propertyName, Association association) {
        MetaBean metaBean = getBeanConfiguration().getMetaBean();

        Class<?> javaType = metaBean.getAssociationClass(beanClass,propertyName);
        ResultMapping.Builder resultMappingBuilder = new ResultMapping.Builder(getBeanConfiguration(), propertyName);
        resultMappingBuilder.javaType(javaType);

        int index = resultMappingContext.getNewJoinIndex();
        String columnPrefix = (StringUtils.isBlank(resultMappingContext.getColumnPrefix()) ? "t_" : "") + index + "_";
        resultMappingBuilder.columnPrefix((StringUtils.isBlank(resultMappingContext.getColumnPrefix()) ? "t_" : "") + index + "_");

        String[] propertyTarget = metaBean.addIdPropertyIfEmpty(javaType, association.propertyTarget());

        String[] columnTarget = metaBean.prepareColumns(beanClass, propertyTarget);
        resultMappingBuilder.notNullColumns(Sets.newHashSet(columnTarget));

        int ad = association.nestedOption().depth() > 0 ? association.nestedOption().depth() - 1 : association.nestedOption().depth();
        int cd = resultMappingContext.getDepth() > 0 ? resultMappingContext.getDepth() - 1 : resultMappingContext.getDepth();

        resultMappingBuilder.nestedResultMapId(ResultMapNameHelper.buildNestedResultMapKey(javaType, resultMappingContext.getDepth() < 0 ? ad : cd, columnPrefix));

        return resultMappingBuilder.build();
    }

    @SuppressWarnings("unchecked")
    private ResultMapping buildDefaultResultMapping(Class<?> beanClass, String propertyName, Association association) {
        MetaBean metaBean = getBeanConfiguration().getMetaBean();

        String[] propertySource = association.propertySource();
        String column = null;
        String[] columnSource = metaBean.prepareColumns(beanClass, propertySource);
        if (columnSource.length == 1) {
            column = columnSource[0];
        }

        Class<?> javaType = metaBean.getAssociationClass(beanClass,propertyName);

        ResultMapping.Builder resultMappingBuilder = new ResultMapping.Builder(getBeanConfiguration(), propertyName, column, javaType);
        if (FetchType.LAZY.equals(association.fetchType())) {
            resultMappingBuilder.lazy(true);
        } else if (FetchType.EAGER.equals(association.fetchType())) {
            resultMappingBuilder.lazy(false);
        }

        resultMappingBuilder.composites(BeanResultMapHelper.buildComposites(getBeanConfiguration(), beanClass, propertySource, columnSource));

        if (StringUtils.isNotBlank(association.select())) {
            resultMappingBuilder.nestedQueryId(association.select());
        } else {
            String[] propertyTarget = metaBean.addIdPropertyIfEmpty(javaType, association.propertyTarget());

            JoinTable[] joinTables = association.joinTable();
            if (joinTables.length > 0) {
                resultMappingBuilder.nestedQueryId(
                        StatementNameHelper.buildFindBeansByJoinTableKey(beanClass, javaType, false, joinTables, propertySource, propertyTarget, null));
            } else {
                resultMappingBuilder.nestedQueryId(StatementNameHelper.buildFindBeansByKey(javaType, false, propertyTarget, null));
            }
        }
        return resultMappingBuilder.build();
    }
}
