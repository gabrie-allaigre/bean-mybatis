package com.talanlabs.bean.mybatis.rsql.engine.where.comparisons.factory;

import com.talanlabs.bean.mybatis.session.meta.MetaBean;
import com.talanlabs.bean.mybatis.session.meta.MetaInfoBean;
import com.talanlabs.bean.mybatis.rsql.configuration.IRsqlConfiguration;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;

import java.lang.annotation.Annotation;

public abstract class AbstractRsqlResultFactory<E extends Annotation> implements IRsqlResultFactory {

    private final BeanConfiguration beanConfiguration;
    private final Class<E> annotationClass;

    public AbstractRsqlResultFactory(BeanConfiguration beanConfiguration, Class<E> annotationClass) {
        super();

        this.beanConfiguration = beanConfiguration;
        this.annotationClass = annotationClass;
    }

    public final BeanConfiguration getBeanConfiguration() {
        return beanConfiguration;
    }

    public final IRsqlConfiguration getRsqlConfiguration() {
        return beanConfiguration.getRsqlConfiguration();
    }

    public final Class<E> getAnnotationClass() {
        return annotationClass;
    }

    @Override
    public boolean acceptProperty(Class<?> beanClass, String propertyName) {
        MetaBean metaBean = beanConfiguration.getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);
        return metaInfoBean.isPropertyAnnotationPresent(propertyName, annotationClass);
    }
}
