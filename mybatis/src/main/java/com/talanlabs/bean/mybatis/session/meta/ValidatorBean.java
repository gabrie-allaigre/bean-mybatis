package com.talanlabs.bean.mybatis.session.meta;

public interface ValidatorBean {

    /**
     * Verify if bean is correct
     *
     * @param beanClass bean class
     */
    void validateBean(Class<?> beanClass) throws ValidationBeanException;

}
