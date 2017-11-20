package com.talanlabs.bean.mybatis.component.it.config;

import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.bean.mybatis.annotation.NlsColumn;
import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.session.context.SqlContext;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.session.handler.INlsColumnHandler;
import com.talanlabs.bean.mybatis.session.meta.MetaBean;
import com.talanlabs.bean.mybatis.session.meta.MetaInfoBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

public class DefaultNlsColumnHandler implements INlsColumnHandler {

    private final BeanConfiguration beanConfiguration;

    private String languageCode;

    public DefaultNlsColumnHandler(BeanConfiguration beanConfiguration) {
        super();

        this.beanConfiguration = beanConfiguration;

        this.languageCode = "fra";
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    @Override
    public Object getContext() {
        return languageCode;
    }

    @Override
    public Map<String, Object> getAdditionalParameter(Class<?> componentClass, String propertyName) {
        Map<String, Object> map = new HashMap<>();
        map.put("languageCode", languageCode);
        return map;
    }

    @Override
    public String getSelectNlsColumnId(Class<?> componentClass, String propertyName) {
        return "com.talanlabs.bean.mybatis.component.it.mapper.NlsMapper.selectNlsColumn";
    }

    @Override
    public boolean isUpdateDefaultNlsColumn(Class<?> componentClass, String propertyName) {
        return "eng".equals(languageCode);
    }

    @Override
    public String getMergeNlsColumnId(Class<?> componentClass, String propertyName) {
        return "com.talanlabs.bean.mybatis.component.it.mapper.NlsMapper.mergeNlsColumn";
    }

    @Override
    public String getDeleteNlsColumnsId(Class<?> componentClass) {
        return "com.talanlabs.bean.mybatis.component.it.mapper.NlsMapper.deleteNlsColumns";
    }

    @Override
    public Pair<String, SqlResult> buildNameResultForWhere(Class<?> beanClass, String propertyName, String previousPropertyName, String tableJoinName, SqlContext context) {
        MetaBean metaBean = beanConfiguration.getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

        if (!metaInfoBean.getPropertyNames().contains(propertyName)) {
            throw new IllegalArgumentException("Not find NlsColumn for Bean=" + beanClass + " with property=" + propertyName);
        }
        NlsColumn nlsColumn = metaInfoBean.getPropertyAnnotation(propertyName, NlsColumn.class);
        if (nlsColumn == null) {
            throw new IllegalArgumentException("Not find NlsColumn for Bean=" + beanClass + " with property=" + propertyName);
        }

        SqlResult.Builder builder = SqlResult.newBuilder();

        String fullPropertyName = (StringUtils.isNotBlank(previousPropertyName) ? previousPropertyName + "." : "") + propertyName;

        String joinTableName = context.getJoinName(fullPropertyName);
        if (StringUtils.isBlank(joinTableName)) {
            joinTableName = context.createJoinName(fullPropertyName);

            Entity entity = beanClass.getAnnotation(Entity.class);

            String idPpropertyName = metaBean.getIdPropertyName(beanClass);
            if (StringUtils.isBlank(idPpropertyName)) {
                throw new IllegalArgumentException("Not find Id for Bean=" + beanClass);
            }
            if (!metaInfoBean.isPropertyAnnotationPresent(idPpropertyName, Column.class)) {
                throw new IllegalArgumentException("Not find Column for Bean=" + beanClass + " with property=" + idPpropertyName);
            }

            Column column = metaInfoBean.getPropertyAnnotation(idPpropertyName, Column.class);

            String tableNameParam = context.getNewParamName();
            builder.appendParameter(tableNameParam, entity.name());
            String columnNameParam = context.getNewParamName();
            builder.appendParameter(columnNameParam, nlsColumn.name());
            String languageCodeParam = context.getNewParamName();
            builder.appendParameter(languageCodeParam, languageCode);

            String joinSql =
                    "T_NLS " + joinTableName + " ON " + joinTableName + ".TABLE_NAME = #{" + tableNameParam + ",javaType=java.lang.String} AND " + joinTableName + ".COLUMN_NAME = #{" + columnNameParam
                            + ",javaType=java.lang.String} AND " + joinTableName + ".LANGUAGE_CODE = #{" + languageCodeParam + ",javaType=java.lang.String} AND " + joinTableName + ".TABLE_ID = " + (
                            StringUtils.isNotBlank(tableJoinName) ?
                                    tableJoinName + "." :
                                    "") + column.name();

            builder.appendJoin(SqlResult.Join.Type.LeftOuter, joinSql);
        }

        String name = "NVL(" + joinTableName + "." + "MEANING" + ", " + (StringUtils.isNotBlank(tableJoinName) ? tableJoinName + "." : "") + nlsColumn.name() + ")";

        return Pair.of(name, builder.build());
    }

    @Override
    public Pair<String, SqlResult> buildNameResultForOrderBy(Class<?> beanClass, String propertyName, String previousPropertyName, String tableJoinName, SqlContext context) {
        return buildNameResultForWhere(beanClass, propertyName, previousPropertyName, tableJoinName, context);
    }
}
