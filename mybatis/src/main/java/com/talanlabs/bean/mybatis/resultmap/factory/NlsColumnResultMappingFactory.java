package com.talanlabs.bean.mybatis.resultmap.factory;

import com.talanlabs.bean.mybatis.annotation.FetchType;
import com.talanlabs.bean.mybatis.annotation.NlsColumn;
import com.talanlabs.bean.mybatis.session.meta.MetaBean;
import com.talanlabs.bean.mybatis.session.meta.MetaInfoBean;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.statement.StatementNameHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.util.ArrayList;
import java.util.List;

public class NlsColumnResultMappingFactory extends AbstractResultMappingFactory<NlsColumn> {

    public NlsColumnResultMappingFactory(BeanConfiguration beanConfiguration) {
        super(beanConfiguration, NlsColumn.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultMapping buildResultMapping(ResultMappingContext resultMappingContext, Class<?> beanClass, String propertyName) {
        MetaBean metaBean = getBeanConfiguration().getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

        NlsColumn nlsColumn = metaInfoBean.getPropertyAnnotation(propertyName, NlsColumn.class);

        if (FetchType.EAGER.equals(nlsColumn.fetchType()) && resultMappingContext.getDepth() != 0 && nlsColumn.nested()) {
            return buildNestedResultMapping(resultMappingContext, beanClass, propertyName, nlsColumn);
        } else {
            return buildNlsColumnResultMapping(beanClass, propertyName, nlsColumn);
        }
    }

    private ResultMapping buildNestedResultMapping(ResultMappingContext resultMappingContext, Class<?> beanClass, String propertyName, NlsColumn nlsColumn) {
        MetaBean metaBean = getBeanConfiguration().getMetaBean();

        resultMappingContext.getNewJoinIndex();

        Class<?> javaType = metaBean.getNlsColumnClass(beanClass, propertyName);

        ResultMapping.Builder resultMappingBuilder = new ResultMapping.Builder(getBeanConfiguration(), propertyName, nlsColumn.name(), javaType);

        if (!JdbcType.UNDEFINED.equals(nlsColumn.jdbcType())) {
            resultMappingBuilder.jdbcType(nlsColumn.jdbcType());
        }
        if (!UnknownTypeHandler.class.equals(nlsColumn.typeHandler())) {
            resultMappingBuilder.typeHandler(getBeanConfiguration().getTypeHandler(nlsColumn.typeHandler()));
        }

        return resultMappingBuilder.build();
    }

    private ResultMapping buildNlsColumnResultMapping(Class<?> beanClass, String propertyName, NlsColumn nlsColumn) {
        MetaBean metaBean = getBeanConfiguration().getMetaBean();
        MetaInfoBean metaInfoBean = getBeanConfiguration().getMetaBean().forBeanClass(beanClass);

        String columnName = nlsColumn.name();

        Class<?> javaType = metaBean.getNlsColumnClass(beanClass, propertyName);

        ResultMapping.Builder resultMappingBuilder = new ResultMapping.Builder(getBeanConfiguration(), propertyName, null, javaType);
        List<ResultMapping> composites = new ArrayList<>();
        composites.add(new ResultMapping.Builder(getBeanConfiguration(), "defaultValue", columnName, javaType).build());

        String[] propertySources = metaBean.addIdPropertyIfEmpty(beanClass, nlsColumn.propertySource());

        String[] columnSource = metaBean.prepareColumns(beanClass, propertySources);
        for (int i = 0; i < propertySources.length; i++) {
            String pd = propertySources[i];

            composites.add(new ResultMapping.Builder(getBeanConfiguration(), pd, columnSource[i], metaInfoBean.getPropertyClass(pd)).build());
        }
        resultMappingBuilder.composites(composites);

        if (FetchType.LAZY.equals(nlsColumn.fetchType())) {
            resultMappingBuilder.lazy(true);
        } else if (FetchType.EAGER.equals(nlsColumn.fetchType())) {
            resultMappingBuilder.lazy(false);
        }
        if (StringUtils.isNotBlank(nlsColumn.select())) {
            resultMappingBuilder.nestedQueryId(nlsColumn.select());
        } else {
            resultMappingBuilder.nestedQueryId(StatementNameHelper.buildFindNlsColumnKey(beanClass, propertyName));
        }
        return resultMappingBuilder.build();
    }
}
