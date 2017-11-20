package com.talanlabs.bean.mybatis.rsql.engine.where.comparisons.factory;

import com.talanlabs.bean.mybatis.annotation.Association;
import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.session.meta.MetaBean;
import com.talanlabs.bean.mybatis.session.context.SqlContext;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.session.meta.BeanSqlResultHelper;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import org.apache.commons.lang3.StringUtils;

public class AssociationRsqlResultFactory extends AbstractRsqlResultFactory<Association> {

    public AssociationRsqlResultFactory(BeanConfiguration beanConfiguration) {
        super(beanConfiguration, Association.class);
    }

    @Override
    public SqlResult buildBeanRsqlResult(IRsqlResultContext rsqlResultContext, Class<?> beanClass, String propertyName,
                                              ComparisonNode node, String previousPropertyName, String nextPropertyName, String tableJoinName, SqlContext context) {
        MetaBean metaBean = getBeanConfiguration().getMetaBean();

        String current = (StringUtils.isNotBlank(previousPropertyName) ? previousPropertyName + "." : "") + propertyName;

        String joinTableName = context.getJoinName(current);
        if (StringUtils.isNotBlank(joinTableName)) {
            Class<?> javaType = metaBean.getAssociationClass(beanClass, propertyName);

            return rsqlResultContext.visit(javaType, node, current, nextPropertyName, joinTableName, context);
        } else {
            return buildJoins(rsqlResultContext, beanClass, propertyName, node, previousPropertyName, nextPropertyName, tableJoinName, context);
        }
    }

    private SqlResult buildJoins(IRsqlResultContext rsqlResultContext, Class<?> beanClass, String propertyName,
                                 ComparisonNode node, String previousPropertyName, String nextPropertyName, String tablePrefix, SqlContext context) {
        MetaBean metaBean = getBeanConfiguration().getMetaBean();

        BeanSqlResultHelper beanSqlResultHelper = getBeanConfiguration().getBeanSqlResultHelper();

        String current = (StringUtils.isNotBlank(previousPropertyName) ? previousPropertyName + "." : "") + propertyName;
        String joinTableName = context.createJoinName(current);

        SqlResult.Builder builder = SqlResult.newBuilder();

        builder.appendSqlResult(beanSqlResultHelper.computeAssociationJoins(beanClass, propertyName, tablePrefix, joinTableName, SqlResult.Join.Type.Inner));

        Class<?> javaType = metaBean.getAssociationClass(beanClass, propertyName);

        builder.appendSqlResult(rsqlResultContext.visit(javaType, node, current, nextPropertyName, joinTableName, context));
        return builder.build();
    }
}
