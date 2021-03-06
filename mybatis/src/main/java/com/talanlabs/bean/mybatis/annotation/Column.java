package com.talanlabs.bean.mybatis.annotation;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface Column {

    /**
     * Column name in database
     *
     * @return name
     */
    String name();

    /**
     * Java type for collection
     *
     * @return javaType or void.class for auto
     */
    Class<?> javaType() default void.class;

    /**
     * Jdbc Type
     *
     * @return type
     */
    JdbcType jdbcType() default JdbcType.UNDEFINED;

    /**
     * Type handler
     *
     * @return handler
     */
    Class<? extends TypeHandler<?>> typeHandler() default UnknownTypeHandler.class;

}
