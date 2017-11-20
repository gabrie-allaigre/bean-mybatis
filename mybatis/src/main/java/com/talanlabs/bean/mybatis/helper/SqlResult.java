package com.talanlabs.bean.mybatis.helper;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SqlResult {

    public final List<String> selects;
    public final List<Join> joins;
    public final List<String> wheres;
    public final Map<String, Object> parameterMap;
    public final List<String> orderBies;

    private SqlResult(List<String> selects, List<Join> joins, List<String> wheres, Map<String, Object> parameterMap, List<String> orderBies) {
        super();

        this.selects = selects;
        this.joins = joins;
        this.wheres = wheres;
        this.parameterMap = parameterMap;
        this.orderBies = orderBies;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("joins", joins).append("wheres", wheres).append("parameterMap", parameterMap).toString();
    }

    public static class Builder {

        private List<String> selects = new ArrayList<>();
        private List<Join> joins = new ArrayList<>();
        private List<String> wheres = new ArrayList<>();
        private Map<String, Object> parameterMap = new HashMap<>();
        private List<String> orderBies = new ArrayList<>();

        private Builder() {
            super();
        }

        public Builder appendSqlResult(SqlResult sqlResult) {
            this.selects.addAll(sqlResult.selects);
            this.joins.addAll(sqlResult.joins);
            this.wheres.addAll(sqlResult.wheres);
            this.parameterMap.putAll(sqlResult.parameterMap);
            this.orderBies.addAll(sqlResult.orderBies);
            return this;
        }

        public Builder appendSelect(String select) {
            this.selects.add(select);
            return this;
        }

        public Builder appendJoin(Join.Type type, String sql) {
            return appendJoin(Join.of(type, sql));
        }

        public Builder appendJoin(Join join) {
            this.joins.add(join);
            return this;
        }

        public Builder appendWhere(String where) {
            this.wheres.add(where);
            return this;
        }

        public Builder appendParameter(String key, Object value) {
            this.parameterMap.put(key, value);
            return this;
        }

        public Builder appendParameters(Map<String, Object> parameterMap) {
            this.parameterMap.putAll(parameterMap);
            return this;
        }

        public Builder appendOrderBy(String name) {
            this.orderBies.add(name);
            return this;
        }

        public SqlResult build() {
            return new SqlResult(selects, joins, wheres, parameterMap, orderBies);
        }
    }

    public static class Join {

        public final String sql;
        public final Type type;

        private Join(Type type, String sql) {
            super();

            this.type = type;
            this.sql = sql;
        }

        public static Join of(Type type, String sql) {
            return new Join(type, sql);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this).append("type", type).append("sql", sql).toString();
        }

        public enum Type {
            Inner, Outer, LeftOuter, RightOuter
        }
    }

    public static class SqlResultJoiner {

        final List<String> selects;
        final List<SqlResult.Join> joins;
        final List<String> wheres;
        final Map<String, Object> paramMap;
        final List<String> orderBies;

        private SqlResultJoiner() {
            super();

            this.selects = new ArrayList<>();
            this.joins = new ArrayList<>();
            this.wheres = new ArrayList<>();
            this.paramMap = new HashMap<>();
            this.orderBies = new ArrayList<>();
        }

        public static Collector<SqlResult, SqlResultJoiner, SqlResult> joining() {
            return Collector.of(SqlResultJoiner::new, SqlResultJoiner::accumulater, SqlResultJoiner::combiner, SqlResultJoiner::finisher);
        }

        private static void accumulater(SqlResultJoiner a, SqlResult t) {
            a.selects.addAll(t.selects);
            a.joins.addAll(t.joins);
            a.wheres.addAll(t.wheres);
            a.paramMap.putAll(t.parameterMap);
            a.orderBies.addAll(t.orderBies);
        }

        private static SqlResultJoiner combiner(SqlResultJoiner a1, SqlResultJoiner a2) {
            a1.selects.addAll(a2.selects);
            a1.joins.addAll(a2.joins);
            a1.wheres.addAll(a2.wheres);
            a1.paramMap.putAll(a2.paramMap);
            a1.orderBies.addAll(a2.orderBies);
            return a1;
        }

        private static SqlResult finisher(SqlResultJoiner a) {
            return new SqlResult(a.selects, a.joins, a.wheres, a.paramMap, a.orderBies);
        }
    }

    public static class SqlResultJoiner2 {

        final List<String> selects;
        final List<SqlResult.Join> joins;
        final StringJoiner whereSqlJoiner;
        final Map<String, Object> paramMap;
        final List<String> orderBies;

        private SqlResultJoiner2(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
            super();

            this.selects = new ArrayList<>();
            this.joins = new ArrayList<>();
            this.whereSqlJoiner = new StringJoiner(delimiter, prefix, suffix);
            this.whereSqlJoiner.setEmptyValue("");
            this.paramMap = new HashMap<>();
            this.orderBies = new ArrayList<>();
        }

        public static Collector<SqlResult, SqlResultJoiner2, SqlResult> joining(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
            return Collector.of(() -> new SqlResultJoiner2(delimiter, prefix, suffix), SqlResultJoiner2::accumulater, SqlResultJoiner2::combiner, SqlResultJoiner2::finisher);
        }

        private static void accumulater(SqlResultJoiner2 a, SqlResult t) {
            a.selects.addAll(t.selects);
            a.joins.addAll(t.joins);
            a.whereSqlJoiner.add(t.wheres.size() > 1 ? t.wheres.stream().collect(Collectors.joining(" AND ", "(", ")")) : t.wheres.get(0));
            a.paramMap.putAll(t.parameterMap);
            a.orderBies.addAll(t.orderBies);
        }

        private static SqlResultJoiner2 combiner(SqlResultJoiner2 a1, SqlResultJoiner2 a2) {
            a1.joins.addAll(a2.joins);
            a1.whereSqlJoiner.merge(a2.whereSqlJoiner);
            a1.paramMap.putAll(a2.paramMap);
            return a1;
        }

        private static SqlResult finisher(SqlResultJoiner2 a) {
            return new SqlResult(a.selects, a.joins, Collections.singletonList(a.whereSqlJoiner.toString()), a.paramMap, a.orderBies);
        }
    }
}
