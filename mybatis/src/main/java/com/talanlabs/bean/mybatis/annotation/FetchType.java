package com.talanlabs.bean.mybatis.annotation;

public enum FetchType {

    /**
     * Load on demand
     */
    LAZY,
    /**
     * Load all
     */
    EAGER,
    /**
     * System default
     */
    DEFAULT
}
