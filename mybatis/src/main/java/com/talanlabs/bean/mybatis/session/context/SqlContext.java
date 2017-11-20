package com.talanlabs.bean.mybatis.session.context;

import com.talanlabs.bean.mybatis.statement.StatementNameHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SqlContext {

    private String defaultTableName = "t";
    private String defaultJoinPrefix = "j";
    private String defaultParamPrefix = "";

    private AtomicInteger paramInteger = new AtomicInteger();
    private AtomicInteger joinInteger = new AtomicInteger();
    private Map<String, String> joinMap = new HashMap<>();

    private SqlContext() {
        super();
    }

    public static SqlContextBuilder newBulder() {
        return new SqlContextBuilder();
    }

    public String getDefaultTableName() {
        return defaultTableName;
    }

    public String getDefaultJoinPrefix() {
        return defaultJoinPrefix;
    }

    public String getDefaultParamPrefix() {
        return defaultParamPrefix;
    }

    /**
     * Get a new param name
     *
     * @return a new param name
     */
    public String getNewParamName() {
        return getDefaultParamPrefix() + StatementNameHelper.buildParam(paramInteger.getAndIncrement());
    }

    /**
     * Get a join name
     *
     * @param fullPropertyName property name with join person.address
     * @return a join name if exists
     */
    public String getJoinName(String fullPropertyName) {
        return joinMap.get(fullPropertyName);
    }

    /**
     * Get a new join name
     *
     * @param fullPropertyName property name with join person.address
     * @return a new join name
     */
    public String createJoinName(String fullPropertyName) {
        return joinMap.computeIfAbsent(fullPropertyName, k -> getDefaultJoinPrefix() + joinInteger.getAndIncrement());
    }

    public static class SqlContextBuilder {

        SqlContext sqlContext = new SqlContext();

        public SqlContextBuilder defaultTablePrefix(String defaultTablePrefix) {
            sqlContext.defaultTableName = defaultTablePrefix;
            return this;
        }

        public SqlContextBuilder defaultJoinPrefix(String defaultJoinPrefix) {
            sqlContext.defaultJoinPrefix = defaultJoinPrefix;
            return this;
        }

        public SqlContextBuilder defaultParamPrefix(String defaultParamPrefix) {
            sqlContext.defaultParamPrefix = defaultParamPrefix;
            return this;
        }

        public SqlContext build() {
            return sqlContext;
        }
    }
}
