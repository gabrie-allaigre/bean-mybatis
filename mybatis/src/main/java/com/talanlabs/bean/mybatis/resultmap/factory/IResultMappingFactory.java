package com.talanlabs.bean.mybatis.resultmap.factory;

import org.apache.ibatis.mapping.ResultMapping;

import java.util.concurrent.atomic.AtomicInteger;

public interface IResultMappingFactory {

    /**
     * @param beanClass    bean descriptor
     * @param propertyName current property
     * @return true, if factory build with property
     */
    boolean acceptProperty(Class<?> beanClass, String propertyName);

    /**
     * @param resultMappingContext current resultMappingContext
     * @param beanClass            bean descriptor
     * @param propertyName         current property
     * @return A result mapping
     */
    ResultMapping buildResultMapping(ResultMappingContext resultMappingContext, Class<?> beanClass, String propertyName);

    class ResultMappingContext {

        private final int depth;
        private final String columnPrefix;

        private AtomicInteger joinInteger = new AtomicInteger();

        public ResultMappingContext(int depth, String columnPrefix) {
            this.depth = depth;
            this.columnPrefix = columnPrefix;
        }

        public int getDepth() {
            return depth;
        }

        public String getColumnPrefix() {
            return columnPrefix;
        }

        public int getNewJoinIndex() {
            return joinInteger.getAndIncrement();
        }
    }
}
