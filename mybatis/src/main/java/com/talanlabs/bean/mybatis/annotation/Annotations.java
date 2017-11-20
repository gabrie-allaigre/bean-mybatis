package com.talanlabs.bean.mybatis.annotation;

import java.lang.annotation.Annotation;

public class Annotations {

    private Annotations() {
    }

    public static JoinTable joinTable(String name, String[] left, String[] right) {
        return new JoinTableImpl(name, left, right);
    }

    public static OrderBy orderBy(String value, OrderBy.Sort sort) {
        return new OrderByImpl(value, sort);
    }

    private static class JoinTableImpl implements JoinTable {

        private final String name;
        private final String[] left;
        private final String[] right;

        private JoinTableImpl(String name, String[] left, String[] right) {
            super();

            this.name = name;
            this.left = left;
            this.right = right;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public String[] left() {
            return left;
        }

        @Override
        public String[] right() {
            return right;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return JoinTable.class;
        }
    }

    private static class OrderByImpl implements OrderBy {

        private final String value;
        private final Sort sort;

        public OrderByImpl(String value, Sort sort) {
            super();

            this.value = value;
            this.sort = sort;
        }

        @Override
        public String value() {
            return value;
        }

        @Override
        public Sort sort() {
            return sort;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return OrderBy.class;
        }
    }
}
