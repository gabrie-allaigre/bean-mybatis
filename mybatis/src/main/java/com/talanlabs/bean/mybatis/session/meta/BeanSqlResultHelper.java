package com.talanlabs.bean.mybatis.session.meta;

import com.talanlabs.bean.mybatis.annotation.Association;
import com.talanlabs.bean.mybatis.annotation.Collection;
import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.bean.mybatis.annotation.FetchType;
import com.talanlabs.bean.mybatis.annotation.JoinTable;
import com.talanlabs.bean.mybatis.annotation.NlsColumn;
import com.talanlabs.bean.mybatis.annotation.OrderBy;
import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.session.context.SqlContext;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.session.handler.INlsColumnHandler;
import com.talanlabs.bean.mybatis.statement.StatementNameHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BeanSqlResultHelper {

    private final BeanConfiguration beanConfiguration;

    public BeanSqlResultHelper(BeanConfiguration beanConfiguration) {
        super();

        this.beanConfiguration = beanConfiguration;
    }

    public SqlResult computeSelect(Class<?> beanClass, String aliasName) {
        return getSelectAndJoin(beanClass, aliasName, "", -1, true);
    }

    public SqlResult computeWhere(Class<?> beanClass, String aliasName, String... propertyNames) {
        MetaBean metaBean = beanConfiguration.getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

        SqlResult.Builder builder = SqlResult.newBuilder();

        int param = 1;
        for (String propertyName : propertyNames) {
            Column column = metaInfoBean.getPropertyAnnotation(propertyName, Column.class);
            builder.appendWhere(aliasName + "." + column.name() + " = " + buildColumn(beanClass, propertyName, StatementNameHelper.buildParam(param)));

            param++;
        }

        return builder.build();
    }

    public SqlResult computeOrderBy(Class<?> beanClass, String aliasName, List<OrderBy> orderBies) {
        MetaBean metaBean = beanConfiguration.getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

        SqlResult.Builder builder = SqlResult.newBuilder();

        if (orderBies != null && !orderBies.isEmpty()) {
            for (OrderBy orderBy : orderBies) {
                Column column = metaInfoBean.getPropertyAnnotation(orderBy.value(), Column.class);

                builder.appendOrderBy(aliasName + "." + column.name() + " " + orderBy.sort().name());
            }
        }

        return builder.build();
    }

    public SqlResult getSelectAndJoin(Class<?> beanClass, String aliasTable, String columnPrefix, int depth, boolean inner) {
        MetaBean metaBean = beanConfiguration.getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

        AtomicInteger joinInteger = new AtomicInteger();
        String prefix = StringUtils.isBlank(columnPrefix) ? "" : "t" + columnPrefix + "_";

        SqlResult.Builder builder = SqlResult.newBuilder();

        metaInfoBean.getPropertyNames().stream().sorted().forEach(propertyName -> {
            if (metaInfoBean.isPropertyAnnotationPresent(propertyName, Column.class)) {
                Column column = metaInfoBean.getPropertyAnnotation(propertyName, Column.class);
                builder.appendSelect(aliasTable + "." + column.name() + " as " + prefix + column.name());
            } else if (metaInfoBean.isPropertyAnnotationPresent(propertyName, NlsColumn.class)) {
                NlsColumn nlsColumn = metaInfoBean.getPropertyAnnotation(propertyName, NlsColumn.class);

                if (FetchType.EAGER.equals(nlsColumn.fetchType()) && depth != 0 && nlsColumn.nested()) {
                    builder.appendSqlResult(getSelectAndJoinNlsColumn(beanClass, propertyName, aliasTable, prefix, joinInteger));
                } else {
                    builder.appendSelect(aliasTable + "." + nlsColumn.name() + " as " + prefix + nlsColumn.name());
                }
            } else if (metaInfoBean.isPropertyAnnotationPresent(propertyName, Association.class)) {
                Association association = metaInfoBean.getPropertyAnnotation(propertyName, Association.class);

                if (FetchType.EAGER.equals(association.fetchType()) && association.nestedOption().depth() != 0 && depth != 0) {
                    builder.appendSqlResult(getSelectAndJoinAssociation(beanClass, propertyName, aliasTable, columnPrefix, depth, joinInteger, inner));
                }
            } else if (metaInfoBean.isPropertyAnnotationPresent(propertyName, Collection.class)) {
                Collection collection = metaInfoBean.getPropertyAnnotation(propertyName, Collection.class);

                if (FetchType.EAGER.equals(collection.fetchType()) && collection.nestedOption().depth() != 0 && depth != 0) {
                    builder.appendSqlResult(getSelectAndJoinCollection(beanClass, propertyName, aliasTable, columnPrefix, depth, joinInteger, inner));
                }
            }
        });
        return builder.build();
    }

    private SqlResult getSelectAndJoinNlsColumn(Class<?> beanClass, String propertyName, String aliasTable, String prefix, AtomicInteger joinInteger) {
        MetaBean metaBean = beanConfiguration.getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

        NlsColumn nlsColumn = metaInfoBean.getPropertyAnnotation(propertyName, NlsColumn.class);

        int i = joinInteger.getAndIncrement();

        String joinTableName = aliasTable + "_" + i;

        INlsColumnHandler nlsColumnHandler = beanConfiguration.getNlsColumnHandler();
        Pair<String, SqlResult> pair = nlsColumnHandler.buildNameResultForWhere(beanClass, propertyName, "", aliasTable,
                SqlContext.newBulder().defaultJoinPrefix(joinTableName + "_").defaultParamPrefix(joinTableName + "_").build());

        SqlResult.Builder builder = SqlResult.newBuilder();
        builder.appendSelect(pair.getLeft() + " as " + prefix + nlsColumn.name());
        builder.appendSqlResult(pair.getRight());
        return builder.build();
    }

    private SqlResult getSelectAndJoinAssociation(Class<?> beanClass, String propertyName, String aliasTable, String columnPrefix, int depth, AtomicInteger joinInteger, boolean inner) {
        MetaBean metaBean = beanConfiguration.getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);
        Association association = metaInfoBean.getPropertyAnnotation(propertyName, Association.class);

        int i = joinInteger.getAndIncrement();

        int ad = association.nestedOption().depth() > 0 ? association.nestedOption().depth() - 1 : association.nestedOption().depth();
        int cd = depth > 0 ? depth - 1 : depth;

        String joinTableName = aliasTable + "_" + i;

        boolean inner2 = inner && association.nestedOption().alwaysNotNull();

        SqlResult.Join.Type type = inner2 ? SqlResult.Join.Type.Inner : SqlResult.Join.Type.LeftOuter;

        SqlResult.Builder builder = SqlResult.newBuilder();

        builder.appendSqlResult(computeAssociationJoins(beanClass, propertyName, aliasTable, joinTableName, type));

        Class<?> javaType = metaBean.getAssociationClass(beanClass, propertyName);

        builder.appendSqlResult(getSelectAndJoin(javaType, joinTableName, columnPrefix + "_" + i, depth < 0 ? ad : cd, inner2));

        return builder.build();
    }

    public SqlResult computeAssociationJoins(Class<?> beanClass, String propertyName, String aliasTable, String joinTableName, SqlResult.Join.Type type) {
        MetaBean metaBean = beanConfiguration.getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);
        Association association = metaInfoBean.getPropertyAnnotation(propertyName, Association.class);

        if (StringUtils.isNotBlank(association.select())) {
            throw new IllegalArgumentException(String.format("Property %s not accepted, not use select association", propertyName));
        }

        String[] columnSource = metaBean.prepareColumns(beanClass, association.propertySource());

        Class<?> javaType = metaBean.getAssociationClass(beanClass, propertyName);
        Entity entity = javaType.getAnnotation(Entity.class);

        String[] propertyTarget = metaBean.addIdPropertyIfEmpty(javaType, association.propertyTarget());

        String[] columnTarget = metaBean.prepareColumns(javaType, propertyTarget);

        JoinTable[] joinTables = association.joinTable();

        return buildJoins(aliasTable, entity.name(), joinTableName, columnSource, columnTarget, joinTables, type);
    }

    private SqlResult getSelectAndJoinCollection(Class<?> beanClass, String propertyName, String aliasTable, String columnPrefix, int depth, AtomicInteger joinInteger, boolean inner) {
        MetaBean metaBean = beanConfiguration.getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);
        Collection collection = metaInfoBean.getPropertyAnnotation(propertyName, Collection.class);

        int i = joinInteger.getAndIncrement();

        int ad = collection.nestedOption().depth() > 0 ? collection.nestedOption().depth() - 1 : collection.nestedOption().depth();
        int cd = depth > 0 ? depth - 1 : depth;

        String joinTableName = aliasTable + "_" + i;

        boolean inner2 = inner && collection.nestedOption().alwaysNotNull();

        SqlResult.Join.Type type = inner2 ? SqlResult.Join.Type.Inner : SqlResult.Join.Type.LeftOuter;

        SqlResult.Builder builder = SqlResult.newBuilder();

        builder.appendSqlResult(computeCollectionJoins(beanClass, propertyName, aliasTable, joinTableName, type));

        Class<?> javaType = metaBean.getCollectionElementClass(beanClass, propertyName);
        MetaInfoBean javaMetaInfoBean = metaBean.forBeanClass(javaType);

        for (OrderBy orderBy : collection.orderBy()) {
            Column column = javaMetaInfoBean.getPropertyAnnotation(orderBy.value(), Column.class);

            builder.appendOrderBy(joinTableName + "." + column.name() + " " + orderBy.sort().name());
        }

        builder.appendSqlResult(getSelectAndJoin(javaType, joinTableName, columnPrefix + "_" + i, depth < 0 ? ad : cd, inner2));

        return builder.build();
    }

    private SqlResult computeCollectionJoins(Class<?> beanClass, String propertyName, String aliasTable, String joinTableName, SqlResult.Join.Type type) {
        MetaBean metaBean = beanConfiguration.getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);
        Collection collection = metaInfoBean.getPropertyAnnotation(propertyName, Collection.class);

        if (StringUtils.isNotBlank(collection.select())) {
            throw new IllegalArgumentException(String.format("Property %s not accepted, not use select collection", propertyName));
        }

        String[] propertySource = metaBean.addIdPropertyIfEmpty(beanClass, collection.propertySource());

        String[] columnSource = metaBean.prepareColumns(beanClass, propertySource);

        Class<?> javaType = metaBean.getCollectionElementClass(beanClass, propertyName);

        Entity entity = javaType.getAnnotation(Entity.class);

        String[] columnTarget = metaBean.prepareColumns(javaType, collection.propertyTarget());

        JoinTable[] joinTables = collection.joinTable();

        return buildJoins(aliasTable, entity.name(), joinTableName, columnSource, columnTarget, joinTables, type);
    }

    private SqlResult buildJoins(String aliasTable, String tableName, String joinTableName, String[] columnSource, String[] targetColumns, JoinTable[] joinTables, SqlResult.Join.Type type) {
        SqlResult.Builder builder = SqlResult.newBuilder();

        if (joinTables.length > 0) {
            String prec = aliasTable;
            for (int j = 0; j < joinTables.length; j++) {
                JoinTable joinTable = joinTables[j];

                if (j == 0) {
                    builder.appendJoin(buildJoin(type, joinTable.name(), joinTableName + "_" + j, prec, columnSource, joinTable.left()));
                } else {
                    builder.appendJoin(buildJoin(type, joinTable.name(), joinTableName + "_" + j, prec, joinTables[j - 1].right(), joinTable.left()));
                }
                prec = joinTableName + "_" + j;
            }

            builder.appendJoin(buildJoin(type, tableName, joinTableName, prec, joinTables[joinTables.length - 1].right(), targetColumns));
        } else {
            builder.appendJoin(buildJoin(type, tableName, joinTableName, aliasTable, columnSource, targetColumns));
        }

        return builder.build();
    }

    private SqlResult.Join buildJoin(SqlResult.Join.Type type, String tableName, String joinName, String previousJoinName, String[] sourceColumns, String[] targetColumns) {
        StringJoiner sj = new StringJoiner(" AND ");

        String realJoinName = StringUtils.isNotBlank(joinName) ? joinName + "." : "";
        String realPreviousJoinName = StringUtils.isNotBlank(previousJoinName) ? previousJoinName + "." : "";
        for (int j = 0; j < sourceColumns.length; j++) {
            sj.add(realJoinName + targetColumns[j] + " = " + realPreviousJoinName + sourceColumns[j]);
        }

        return SqlResult.Join.of(type, tableName + " " + joinName + " ON " + sj.toString());
    }

    public String buildColumn(Class<?> beanClass, String propertyName, String param) {
        MetaBean metaBean = beanConfiguration.getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

        Column column = metaInfoBean.getPropertyAnnotation(propertyName, Column.class);

        Class<?> javaType = metaBean.getColumnClass(beanClass, propertyName);
        JdbcType jdbcType = !JdbcType.UNDEFINED.equals(column.jdbcType()) ? column.jdbcType() : null;
        Class<? extends TypeHandler<?>> typeHandlerClass = !UnknownTypeHandler.class.equals(column.typeHandler()) ? column.typeHandler() : null;

        return buildColumn(javaType, jdbcType, typeHandlerClass, param);
    }

    public String buildColumn(Class<?> javaType, JdbcType jdbcType, Class<? extends TypeHandler<?>> typeHandlerClass, String param) {
        return "#{" + param + ",javaType=" + javaType.getName() + (jdbcType != null ? ",jdbcType=" + jdbcType.name() : "") + (typeHandlerClass != null ?
                ",typeHandler=" + typeHandlerClass.getName() :
                "") + "}";
    }

    public String buildSetColumn(Class<?> beanClass, String aliasName, String propertyName) {
        MetaBean metaBean = beanConfiguration.getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

        Column column = metaInfoBean.getPropertyAnnotation(propertyName, Column.class);
        if (column == null) {
            return null;
        }
        return (StringUtils.isBlank(aliasName) ? "" : aliasName + ".") + column.name() + " = " + buildColumn(beanClass, propertyName, propertyName);
    }

    public String buildNlsColumn(Class<?> beanClass, String propertyName, String param) {
        MetaBean metaBean = beanConfiguration.getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

        NlsColumn nlsColumn = metaInfoBean.getPropertyAnnotation(propertyName, NlsColumn.class);

        Class<?> javaType = metaBean.getNlsColumnClass(beanClass, propertyName);
        JdbcType jdbcType = !JdbcType.UNDEFINED.equals(nlsColumn.jdbcType()) ? nlsColumn.jdbcType() : null;
        Class<? extends TypeHandler<?>> typeHandlerClass = !UnknownTypeHandler.class.equals(nlsColumn.typeHandler()) ? nlsColumn.typeHandler() : null;

        return buildColumn(javaType, jdbcType, typeHandlerClass, param);
    }

    /**
     * Build Set nls column, used for update
     *
     * @param beanClass    bean class
     * @param propertyName Property name
     * @return
     */
    public String buildSetNlsColumn(Class<?> beanClass, String propertyName) {
        MetaBean metaBean = beanConfiguration.getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

        NlsColumn nlsColumn = metaInfoBean.getPropertyAnnotation(propertyName, NlsColumn.class);
        return nlsColumn.name() + " = " + buildNlsColumn(beanClass, propertyName, propertyName);
    }

    public SqlResult computeJoins(Class<?> beanClass, Class<?> sourceBeanClass, String[] sourceProperties, String[] targetProperties, JoinTable[] joinTables) {
        MetaBean metaBean = beanConfiguration.getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

        SqlResult.Builder builder = SqlResult.newBuilder();

        int i = 0;
        for (int j = joinTables.length - 1; j >= 0; j--) {
            JoinTable joinTable = joinTables[j];
            String[] rights = joinTable.right();
            String label = "u" + (i + 1);

            List<String> ands = new ArrayList<>();
            if (j == joinTables.length - 1) {
                for (int k = 0; k < targetProperties.length; k++) {
                    String propertyName = targetProperties[k];
                    Column column = metaInfoBean.getPropertyAnnotation(propertyName, Column.class);
                    ands.add(label + "." + rights[k] + " = t." + column.name());
                }
            } else {
                String[] lefts = joinTable.right();
                for (int k = 0; k < lefts.length; k++) {
                    ands.add(label + "." + rights[k] + " = u" + i + "." + lefts[k]);
                }
            }

            builder.appendJoin(SqlResult.Join.Type.Inner, joinTable.name() + " " + label + " on " + String.join(" and ", ands));
            i++;
        }

        JoinTable join = joinTables[0];
        String[] lefts = join.left();

        int param = 1;
        for (int j = 0; j < sourceProperties.length; j++) {
            String propertyName = sourceProperties[j];

            builder.appendWhere("u" + i + "." + lefts[j] + " = " + buildColumn(sourceBeanClass, propertyName, StatementNameHelper.buildParam(param)));

            param++;
        }

        return builder.build();
    }

    public String buildSelectSql(Class<?> beanClass, SqlResult sqlResult, String aliasName) {
        SQL sqlBuilder = new SQL();

        String a = StringUtils.isBlank(aliasName) ? "t" : aliasName;

        addSelectSql(sqlBuilder, sqlResult, a);

        addFromSql(sqlBuilder, beanClass, a);

        addJoinSql(sqlBuilder, sqlResult);
        addWhereSql(sqlBuilder, sqlResult);

        addOrderBySql(sqlBuilder, sqlResult);

        return sqlBuilder.toString();
    }

    public void addSelectSql(SQL sqlBuilder, SqlResult sqlResult, String aliasName) {
        if (!sqlResult.selects.isEmpty()) {
            sqlResult.selects.forEach(sqlBuilder::SELECT);
        } else {
            sqlBuilder.SELECT(aliasName + ".*");
        }
    }

    public void addFromSql(SQL sqlBuilder, Class<?> beanClass, String aliasName) {
        Entity entity = beanClass.getAnnotation(Entity.class);
        sqlBuilder.FROM(entity.name() + " " + aliasName);
    }

    public void addJoinSql(SQL sqlBuilder, SqlResult sqlResult) {
        if (!sqlResult.joins.isEmpty()) {
            for (SqlResult.Join join : sqlResult.joins) {
                switch (join.type) {
                case Inner:
                    sqlBuilder.INNER_JOIN(join.sql);
                    break;
                case Outer:
                    sqlBuilder.OUTER_JOIN(join.sql);
                    break;
                case LeftOuter:
                    sqlBuilder.LEFT_OUTER_JOIN(join.sql);
                    break;
                case RightOuter:
                    sqlBuilder.RIGHT_OUTER_JOIN(join.sql);
                    break;
                }
            }
        }
    }

    public void addWhereSql(SQL sqlBuilder, SqlResult sqlResult) {
        if (!sqlResult.wheres.isEmpty()) {
            sqlResult.wheres.forEach(sqlBuilder::WHERE);
        }
    }

    public void addOrderBySql(SQL sqlBuilder, SqlResult sqlResult) {
        if (!sqlResult.orderBies.isEmpty()) {
            sqlResult.orderBies.forEach(sqlBuilder::ORDER_BY);
        }
    }

    public String buildDeleteSql(Class<?> beanClass, SqlResult sqlResult, String aliasName) {
        SQL sqlBuilder = new SQL();

        String a = StringUtils.isBlank(aliasName) ? "t" : aliasName;

        Entity entity = beanClass.getAnnotation(Entity.class);
        sqlBuilder.DELETE_FROM(entity.name() + " " + a);

        if (!sqlResult.wheres.isEmpty()) {
            sqlBuilder.WHERE(sqlResult.wheres.stream().collect(Collectors.joining(" AND ", "(", ")")));
        }

        return sqlBuilder.toString();
    }
}
