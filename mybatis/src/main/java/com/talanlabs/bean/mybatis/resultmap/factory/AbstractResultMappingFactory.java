package com.talanlabs.bean.mybatis.resultmap.factory;

import com.talanlabs.bean.mybatis.session.BeanConfiguration;

import java.lang.annotation.Annotation;

public abstract class AbstractResultMappingFactory<E extends Annotation> implements IResultMappingFactory {

    private final BeanConfiguration beanConfiguration;
    private final Class<E> annotationClass;

    public AbstractResultMappingFactory(BeanConfiguration beanConfiguration, Class<E> annotationClass) {
        super();

        this.beanConfiguration = beanConfiguration;
        this.annotationClass = annotationClass;
    }

    @Override
    public boolean acceptProperty(Class<?> beanClass, String propertyName) {
        return beanConfiguration.getMetaBean().forBeanClass(beanClass).isPropertyAnnotationPresent(propertyName, annotationClass);
    }

    public final BeanConfiguration getBeanConfiguration() {
        return beanConfiguration;
    }

    public final Class<E> getAnnotationClass() {
        return annotationClass;
    }

}
