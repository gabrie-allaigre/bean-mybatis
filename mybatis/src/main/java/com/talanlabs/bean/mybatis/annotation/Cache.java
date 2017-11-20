package com.talanlabs.bean.mybatis.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Cache {

    /**
     * If false, object is same instance for read else is new instance
     *
     * @return default is false
     */
    boolean readWrite() default false;

    /**
     * Cache size
     *
     * @return default 512
     */
    int size() default 512;

    /**
     * Clear interval
     *
     * @return default 1 hour
     */
    long clearInterval() default 60 * 60 * 1000; // 1 hour

    /**
     * Others bean is links, if links flush cache then flush current cache
     *
     * @return default empty array
     */
    Class<?>[] links() default {};

}
