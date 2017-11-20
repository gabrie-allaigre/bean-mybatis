package com.talanlabs.bean.mybatis.rsql.engine.where.comparisons.factory;

import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.session.context.SqlContext;
import com.talanlabs.bean.mybatis.rsql.engine.ILikePolicy;
import com.talanlabs.bean.mybatis.rsql.engine.IStringPolicy;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.session.meta.BeanSqlResultHelper;
import com.talanlabs.rtext.Rtext;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public abstract class AbstractColumnRsqlResultFactory<E extends Annotation> extends AbstractRsqlResultFactory<E> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractColumnRsqlResultFactory.class);

    private final Map<ComparisonOperator, OperatorConvert> operatorConvertMap;

    public AbstractColumnRsqlResultFactory(BeanConfiguration beanConfiguration, Class<E> annotationClass) {
        super(beanConfiguration, annotationClass);

        this.operatorConvertMap = new HashMap<>();
        addOperatorConvert(RSQLOperators.EQUAL, OperatorConvert.of("=", "like"));
        addOperatorConvert(RSQLOperators.NOT_EQUAL, OperatorConvert.of("<>", "not like"));
        addOperatorConvert(RSQLOperators.LESS_THAN, OperatorConvert.of("<", null));
        addOperatorConvert(RSQLOperators.LESS_THAN_OR_EQUAL, OperatorConvert.of("<=", null));
        addOperatorConvert(RSQLOperators.GREATER_THAN_OR_EQUAL, OperatorConvert.of(">=", null));
        addOperatorConvert(RSQLOperators.GREATER_THAN, OperatorConvert.of(">", null));
        addOperatorConvert(RSQLOperators.IN, OperatorConvert.of("IN", null));
        addOperatorConvert(RSQLOperators.NOT_IN, OperatorConvert.of("NOT IN", null));
    }

    public void addOperatorConvert(ComparisonOperator comparisonOperator, OperatorConvert operatorConvert) {
        operatorConvertMap.put(comparisonOperator, operatorConvert);
    }

    protected SqlResult buildBeanRsqlResult2(IRsqlResultContext rsqlResultContext, Class<?> beanClass, String propertyName,
                                                 ComparisonNode node, String previousPropertyName, String nextPropertyName, String tableJoinName, String columnName, Class<?> javaType, Type realType, JdbcType jdbcType,
                                                 Class<? extends TypeHandler<?>> typeHandlerClass, SqlContext context) {
        OperatorConvert type = operatorConvertMap.get(node.getOperator());
        if (type == null) {
            throw new IllegalArgumentException("Operator not define convert for Bean=" + beanClass + " with property=" + propertyName);
        }

        Rtext rtext = getRsqlConfiguration().getRtext();
        if (rtext == null) {
            throw new IllegalArgumentException("Rtext is null");
        }

        String name = (StringUtils.isNotBlank(tableJoinName) ? tableJoinName + "." : "") + columnName;
        IStringPolicy stringComparePolicy = getRsqlConfiguration().getStringPolicy();

        List<String> arguments = node.getArguments();

        if (!node.getOperator().isMultiValue()) {
            String text = arguments.size() > 0 ? arguments.get(0) : null;

            if (text != null && type.likeSql != null && containWildcard(text)) {
                if (stringComparePolicy != null) {
                    Pair<String, List<String>> res = stringComparePolicy
                            .prepareNameAndParametersForWhere(beanClass, propertyName, node.getOperator(), name, arguments);
                    name = res.getLeft();
                    text = res.getRight().get(0);
                }

                return parseString(type, name, text, context);
            } else {
                if (stringComparePolicy != null && String.class == javaType) {
                    Pair<String, List<String>> res = stringComparePolicy
                            .prepareNameAndParametersForWhere(beanClass, propertyName, node.getOperator(), name, arguments);
                    name = res.getLeft();
                    text = res.getRight().get(0);
                }

                return parseValue(type, propertyName, rtext, name, javaType, realType, jdbcType, typeHandlerClass, cleanSpecial(text), context);
            }
        } else {
            if (stringComparePolicy != null && String.class == javaType) {
                Pair<String, List<String>> res = stringComparePolicy
                        .prepareNameAndParametersForWhere(beanClass, propertyName, node.getOperator(), name, arguments);
                name = res.getLeft();
                arguments = res.getRight();
            }

            return parseValues(type, propertyName, rtext, name, javaType, realType, jdbcType, typeHandlerClass, arguments, context);
        }
    }

    private boolean containWildcard(String text) {
        int i = 0;
        while (i < text.length()) {
            if (text.charAt(i) == '*') {
                return true;
            } else if (text.charAt(i) == '\\') {
                i++;
            }
            i++;
        }
        return false;
    }

    private String cleanSpecial(String text) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < text.length()) {
            if (text.charAt(i) == '\\') {
                i++;
                if (i < text.length()) {
                    sb.append(text.charAt(i));
                }
            } else {
                sb.append(text.charAt(i));
            }
            i++;
        }
        return sb.toString();
    }

    private SqlResult parseString(OperatorConvert operatorConvert, String name, String argument, SqlContext context) {
        BeanSqlResultHelper beanSqlResultHelper = getBeanConfiguration().getBeanSqlResultHelper();
        
        ILikePolicy likePolicy = getRsqlConfiguration().getLikePolicy();
        String likeSymbol = likePolicy.getLikeSymbol();

        SqlResult.Builder builder = SqlResult.newBuilder();

        StringJoiner sj = new StringJoiner(" || ");

        StringBuilder sb = new StringBuilder();
        boolean lastStar = false;
        int i = 0;
        while (i < argument.length()) {
            if (argument.charAt(i) == '*') {
                if (sb.length() > 0) {
                    String param = context.getNewParamName();
                    builder.appendParameter(param, sb.toString());
                    sj.add(beanSqlResultHelper.buildColumn(String.class, null, null, param));
                }
                if (!lastStar) {
                    sj.add("'" + likeSymbol + "'");
                    lastStar = true;
                    sb = new StringBuilder();
                }
            } else if (argument.charAt(i) == '\\') {
                i++;
                if (i < argument.length()) {
                    sb.append(argument.charAt(i));
                    lastStar = false;
                }
            } else {
                sb.append(argument.charAt(i));
                lastStar = false;
            }
            i++;
        }
        if (sb.length() > 0) {
            String param = context.getNewParamName();
            builder.appendParameter(param, sb.toString());
            sj.add(beanSqlResultHelper.buildColumn(String.class, null, null, param));
        }

        String res = name + " " + operatorConvert.likeSql + " " + sj.toString();
        if (StringUtils.isNotBlank(likePolicy.getEscapeSymbol())) {
            res += " ESCAPE '" + likePolicy.getEscapeSymbol() + "'";
        }
        builder.appendWhere(res);

        return builder.build();
    }

    private SqlResult parseValue(OperatorConvert operatorConvert, String propertyName, Rtext rtext, String name, Class<?> javaType, Type realType, JdbcType jdbcType,
                                 Class<? extends TypeHandler<?>> typeHandlerClass, String text, SqlContext context) {
        BeanSqlResultHelper beanSqlResultHelper = getBeanConfiguration().getBeanSqlResultHelper();
        
        SqlResult.Builder builder = SqlResult.newBuilder();

        String param = context.getNewParamName();
        String valueSql;
        try {
            Object value = rtext.fromText(text, realType);
            builder.appendParameter(param, value);

            valueSql = beanSqlResultHelper.buildColumn(javaType, jdbcType, typeHandlerClass, param);
        } catch (Exception e) {
            LOG.trace("Failed to convert text {} on {}", text, realType, e);

            builder.appendParameter(param, text);

            valueSql = beanSqlResultHelper.buildColumn(String.class, null, null, param);
        }

        builder.appendWhere(name + " " + operatorConvert.sql + " " + valueSql);

        return builder.build();
    }

    private SqlResult parseValues(OperatorConvert operatorConvert, String propertyName, Rtext rtext, String name, Class<?> javaType, Type realType, JdbcType jdbcType,
                                  Class<? extends TypeHandler<?>> typeHandlerClass, List<String> texts, SqlContext context) {
        BeanSqlResultHelper beanSqlResultHelper = getBeanConfiguration().getBeanSqlResultHelper();
        
        SqlResult.Builder builder = SqlResult.newBuilder();

        StringJoiner sj = new StringJoiner(", ", "(", ")");
        for (String text : texts) {
            String param = context.getNewParamName();
            try {
                Object value = rtext.fromText(text, realType);
                builder.appendParameter(param, value);

                sj.add(beanSqlResultHelper.buildColumn(javaType, jdbcType, typeHandlerClass, param));
            } catch (Exception e) {
                LOG.trace("Failed to convert text {} on {}", text, realType, e);

                builder.appendParameter(param, text);

                sj.add(beanSqlResultHelper.buildColumn(String.class, null, null, param));
            }
        }
        builder.appendWhere(name + " " + operatorConvert.sql + " " + sj.toString());

        return builder.build();
    }

    public static class OperatorConvert {

        public final String sql;
        public final String likeSql;

        private OperatorConvert(String sql, String likeSql) {
            this.sql = sql;
            this.likeSql = likeSql;
        }

        public static OperatorConvert of(String sql) {
            return new OperatorConvert(sql, null);
        }

        public static OperatorConvert of(String sql, String likeSql) {
            return new OperatorConvert(sql, likeSql);
        }
    }
}
