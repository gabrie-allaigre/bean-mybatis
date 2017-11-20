package com.talanlabs.bean.mybatis.session.factory;

import com.talanlabs.bean.mybatis.session.BeanConfiguration;

public abstract class AbstractMappedStatementFactory implements IMappedStatementFactory {

    private final BeanConfiguration beanConfiguration;

    public AbstractMappedStatementFactory(BeanConfiguration beanConfiguration) {
        super();

        this.beanConfiguration = beanConfiguration;
    }

    public final BeanConfiguration getBeanConfiguration() {
        return beanConfiguration;
    }
}
