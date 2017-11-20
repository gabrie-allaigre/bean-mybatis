package com.talanlabs.bean.mybatis.statement.sqlsource;

import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.bean.mybatis.annotation.NlsColumn;
import com.talanlabs.bean.mybatis.session.meta.MetaBean;
import com.talanlabs.bean.mybatis.session.meta.MetaInfoBean;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.session.handler.INlsColumnHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.HashMap;
import java.util.Map;

public class FindNlsColumnSqlSource implements SqlSource {

    private final BeanConfiguration beanConfiguration;
    private final Class<?> beanClass;
    private final String propertyName;
    private final Entity entity;
    private final NlsColumn nlsColumn;

    public FindNlsColumnSqlSource(BeanConfiguration beanConfiguration, Class<?> beanClass, String propertyName) {
        super();

        this.beanConfiguration = beanConfiguration;
        this.beanClass = beanClass;
        this.propertyName = propertyName;

        MetaBean metaBean = beanConfiguration.getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);
        entity = beanClass.getAnnotation(Entity.class);
        nlsColumn = metaInfoBean.getPropertyAnnotation(propertyName, NlsColumn.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        Map<String, Object> additionalParameters = new HashMap<>();
        additionalParameters.put("tableName", entity.name());
        additionalParameters.put("columnName", nlsColumn.name());

        INlsColumnHandler nlsColumnHandler = beanConfiguration.getNlsColumnHandler();

        Map<String, Object> aps = nlsColumnHandler.getAdditionalParameter(beanClass, propertyName);
        if (aps != null) {
            additionalParameters.putAll(aps);
        }

        String selectId = nlsColumnHandler.getSelectNlsColumnId(beanClass, propertyName);
        MappedStatement mappedStatement = beanConfiguration.getMappedStatement(selectId);

        BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
        additionalParameters.forEach(boundSql::setAdditionalParameter);
        return boundSql;
    }
}
