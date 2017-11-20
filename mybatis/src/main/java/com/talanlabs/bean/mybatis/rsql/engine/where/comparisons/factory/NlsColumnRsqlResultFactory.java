package com.talanlabs.bean.mybatis.rsql.engine.where.comparisons.factory;

import com.talanlabs.bean.mybatis.annotation.NlsColumn;
import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.session.context.SqlContext;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.session.handler.INlsColumnHandler;
import com.talanlabs.bean.mybatis.session.meta.MetaBean;
import com.talanlabs.bean.mybatis.session.meta.MetaInfoBean;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.lang.reflect.Type;

public class NlsColumnRsqlResultFactory extends AbstractColumnRsqlResultFactory<NlsColumn> {

    public NlsColumnRsqlResultFactory(BeanConfiguration beanConfiguration) {
        super(beanConfiguration, NlsColumn.class);
    }

    @Override
    public SqlResult buildBeanRsqlResult(IRsqlResultContext rsqlResultContext, Class<?> beanClass, String propertyName,
                                              ComparisonNode node, String previousPropertyName, String nextPropertyName, String tableJoinName, SqlContext context) {
        MetaBean metaBean = getBeanConfiguration().getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

        NlsColumn nlsColumn = metaInfoBean.getPropertyAnnotation(propertyName, NlsColumn.class);
        Class<?> javaType = metaBean.getNlsColumnClass(beanClass, propertyName);
        Type realType = metaBean.getNlsColumnType(beanClass, propertyName);
        JdbcType jdbcType = !JdbcType.UNDEFINED.equals(nlsColumn.jdbcType()) ? nlsColumn.jdbcType() : null;
        Class<? extends TypeHandler<?>> typeHandlerClass = !UnknownTypeHandler.class.equals(nlsColumn.typeHandler()) ? nlsColumn.typeHandler() : null;

        INlsColumnHandler nlsColumnHandler = getBeanConfiguration().getNlsColumnHandler();
        if (nlsColumnHandler == null) {
            throw new IllegalArgumentException("NlsColumnHandler is null");
        }

        Pair<String, SqlResult> res = nlsColumnHandler.buildNameResultForWhere(beanClass, propertyName, previousPropertyName, tableJoinName, context);
        if (res == null) {
            throw new IllegalArgumentException(
                    "NlsColumnHandler return null for buildNameResultForWhere with bean=" + beanClass + " and property=" + propertyName);
        }

        SqlResult.Builder builder = SqlResult.newBuilder();
        builder.appendSqlResult(res.getRight());

        builder.appendSqlResult(buildBeanRsqlResult2(rsqlResultContext, beanClass, propertyName, node, previousPropertyName, nextPropertyName, null, res.getLeft(), javaType, realType, jdbcType,
                typeHandlerClass, context));

        return builder.build();
    }
}
