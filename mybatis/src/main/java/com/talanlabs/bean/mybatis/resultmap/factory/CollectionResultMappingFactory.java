package com.talanlabs.bean.mybatis.resultmap.factory;

import com.google.common.collect.Sets;
import com.talanlabs.bean.mybatis.annotation.Collection;
import com.talanlabs.bean.mybatis.annotation.FetchType;
import com.talanlabs.bean.mybatis.annotation.JoinTable;
import com.talanlabs.bean.mybatis.annotation.OrderBy;
import com.talanlabs.bean.mybatis.session.meta.MetaBean;
import com.talanlabs.bean.mybatis.session.meta.MetaInfoBean;
import com.talanlabs.bean.mybatis.resultmap.ResultMapNameHelper;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.statement.StatementNameHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.ResultMapping;

public class CollectionResultMappingFactory extends AbstractResultMappingFactory<Collection> {

    public CollectionResultMappingFactory(BeanConfiguration beanConfiguration) {
        super(beanConfiguration, Collection.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultMapping buildResultMapping(ResultMappingContext resultMappingContext, Class<?> beanClass,
                                                  String propertyName) {
        MetaInfoBean metaInfoBean = getBeanConfiguration().getMetaBean().forBeanClass(beanClass);

        Collection collection = metaInfoBean.getPropertyAnnotation(propertyName, Collection.class);

        if (FetchType.EAGER.equals(collection.fetchType()) && resultMappingContext.getDepth() != 0 && collection.nestedOption().depth() != 0) {
            return buildNestedResultMapping(resultMappingContext, beanClass, propertyName, collection);
        } else {
            return buildDefaultResultMapping(beanClass, propertyName, collection);
        }
    }

    @SuppressWarnings("unchecked")
    private ResultMapping buildNestedResultMapping(ResultMappingContext resultMappingContext, Class<?> beanClass,
                                                   String propertyName, Collection collection) {
        MetaBean metaBean = getBeanConfiguration().getMetaBean();

        Class<?> javaType = metaBean.getCollectionClass(beanClass, propertyName);

        ResultMapping.Builder resultMappingBuilder = new ResultMapping.Builder(getBeanConfiguration(), propertyName);
        resultMappingBuilder.javaType(javaType);

        Class<?> ofType = metaBean.getCollectionElementClass(beanClass, propertyName);

        int index = resultMappingContext.getNewJoinIndex();
        String columnPrefix = (StringUtils.isBlank(resultMappingContext.getColumnPrefix()) ? "t_" : "") + index + "_";
        resultMappingBuilder.columnPrefix((StringUtils.isBlank(resultMappingContext.getColumnPrefix()) ? "t_" : "") + index + "_");

        String[] propertyTarget = collection.propertyTarget();

        String[] columnTarget = metaBean.prepareColumns(ofType, propertyTarget);

        resultMappingBuilder.notNullColumns(Sets.newHashSet(columnTarget));

        int ad = collection.nestedOption().depth() > 0 ? collection.nestedOption().depth() - 1 : collection.nestedOption().depth();
        int cd = resultMappingContext.getDepth() > 0 ? resultMappingContext.getDepth() - 1 : resultMappingContext.getDepth();

        resultMappingBuilder.nestedResultMapId(ResultMapNameHelper.buildNestedResultMapKey(ofType, resultMappingContext.getDepth() < 0 ? ad : cd, columnPrefix));

        return resultMappingBuilder.build();
    }

    @SuppressWarnings("unchecked")
    private ResultMapping buildDefaultResultMapping(Class<?> beanClass,
                                                    String propertyName, Collection collection) {
        MetaBean metaBean = getBeanConfiguration().getMetaBean();

        String[] propertySource = metaBean.addIdPropertyIfEmpty(beanClass, collection.propertySource());

        Class<?> javaType = metaBean.getCollectionClass(beanClass, propertyName);

        String column = null;
        String[] columnSource = metaBean.prepareColumns(beanClass, propertySource);
        if (columnSource.length == 1) {
            column = columnSource[0];
        }

        ResultMapping.Builder resultMappingBuilder = new ResultMapping.Builder(getBeanConfiguration(), propertyName, column, javaType);
        resultMappingBuilder.composites(BeanResultMapHelper.buildComposites(getBeanConfiguration(), beanClass, propertySource, columnSource));
        if (FetchType.LAZY.equals(collection.fetchType())) {
            resultMappingBuilder.lazy(true);
        } else if (FetchType.EAGER.equals(collection.fetchType())) {
            resultMappingBuilder.lazy(false);
        }
        if (StringUtils.isNotBlank(collection.select())) {
            resultMappingBuilder.nestedQueryId(collection.select());
        } else {
            Class<?> ofType = metaBean.getCollectionElementClass(beanClass, propertyName);

            String[] propertyTarget = collection.propertyTarget();

            boolean ignoreCancel = metaBean.getCanceledPropertyName(ofType) != null;

            OrderBy[] orderBies = collection.orderBy();

            JoinTable[] joinTables = collection.joinTable();
            if (joinTables.length > 0) {
                resultMappingBuilder.nestedQueryId(
                        StatementNameHelper.buildFindBeansByJoinTableKey(beanClass, ofType, ignoreCancel, joinTables, propertySource, propertyTarget, orderBies));
            } else {
                resultMappingBuilder.nestedQueryId(StatementNameHelper.buildFindBeansByKey(ofType, ignoreCancel, propertyTarget, orderBies));
            }
        }
        return resultMappingBuilder.build();
    }
}
