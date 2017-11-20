package com.talanlabs.bean.mybatis.session.factory;

import com.talanlabs.bean.mybatis.session.BeanConfiguration;

public abstract class AbstractResultMapFactory implements IResultMapFactory {

    private final BeanConfiguration beanConfiguration;

    public AbstractResultMapFactory(BeanConfiguration beanConfiguration) {
        super();

        this.beanConfiguration = beanConfiguration;
    }

    public final BeanConfiguration getBeanConfiguration() {
        return beanConfiguration;
    }
}
