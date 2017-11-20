package com.talanlabs.bean.mybatis.session.factory;

import com.talanlabs.bean.mybatis.session.BeanConfiguration;

public abstract class AbstractCacheFactory implements ICacheFactory {

    private final BeanConfiguration beanConfiguration;

    public AbstractCacheFactory(BeanConfiguration beanConfiguration) {
        super();

        this.beanConfiguration = beanConfiguration;
    }

    public final BeanConfiguration getBeanConfiguration() {
        return beanConfiguration;
    }

}
