package com.talanlabs.bean.mybatis.annotation;

public @interface OrderBy {

    String value();

    Sort sort() default Sort.Asc;

    enum Sort {
        Asc, Desc
    }

}
