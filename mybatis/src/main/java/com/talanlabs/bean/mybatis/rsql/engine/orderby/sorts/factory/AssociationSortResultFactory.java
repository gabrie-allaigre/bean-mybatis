package com.talanlabs.bean.mybatis.rsql.engine.orderby.sorts.factory;

import com.talanlabs.bean.mybatis.annotation.Association;
import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.session.context.SqlContext;
import com.talanlabs.bean.mybatis.rsql.sort.SortDirection;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.session.meta.BeanSqlResultHelper;
import com.talanlabs.bean.mybatis.session.meta.MetaBean;
import org.apache.commons.lang3.StringUtils;

public class AssociationSortResultFactory extends AbstractSortResultFactory<Association> {

    public AssociationSortResultFactory(BeanConfiguration beanConfiguration) {
        super(beanConfiguration, Association.class);
    }

    @Override
    public SqlResult buildBeanSortResult(ISortResultContext sortResultContext, Class<?> beanClass, String propertyName, SortDirection sortDirection, String previousPropertyName,
            String nextPropertyName, String tableJoinName, SqlContext context) {
        MetaBean metaBean = getBeanConfiguration().getMetaBean();

        String current = (StringUtils.isNotBlank(previousPropertyName) ? previousPropertyName + "." : "") + propertyName;

        String joinTableName = context.getJoinName(current);
        if (StringUtils.isNotBlank(joinTableName)) {
            Class<?> javaType = metaBean.getAssociationClass(beanClass, propertyName);

            return sortResultContext.visit(javaType, sortDirection, current, nextPropertyName, joinTableName, context);
        } else {
            return buildJoins(sortResultContext, beanClass, propertyName, sortDirection, previousPropertyName, nextPropertyName, tableJoinName, context);
        }
    }

    private SqlResult buildJoins(ISortResultContext sortResultContext, Class<?> beanClass, String propertyName, SortDirection sortDirection, String previousPropertyName, String nextPropertyName,
            String tablePrefix, SqlContext context) {
        MetaBean metaBean = getBeanConfiguration().getMetaBean();

        BeanSqlResultHelper beanSqlResultHelper = getBeanConfiguration().getBeanSqlResultHelper();

        String current = (StringUtils.isNotBlank(previousPropertyName) ? previousPropertyName + "." : "") + propertyName;
        String joinTableName = context.createJoinName(current);

        SqlResult.Builder builder = SqlResult.newBuilder();
        builder.appendSqlResult(beanSqlResultHelper.computeAssociationJoins(beanClass, propertyName, tablePrefix, joinTableName, SqlResult.Join.Type.LeftOuter));

        Class<?> javaType = metaBean.getAssociationClass(beanClass, propertyName);

        builder.appendSqlResult(sortResultContext.visit(javaType, sortDirection, current, nextPropertyName, joinTableName, context));
        return builder.build();
    }
}
