package com.talanlabs.bean.mybatis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NestedOption {

    /**
     * @return Depth of nested request, if 0 then none, if -1 then all
     */
    int depth() default 0;

    /**
     * @return If true then user inner join
     */
    boolean alwaysNotNull() default false;
}
