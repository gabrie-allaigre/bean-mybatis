package com.talanlabs.bean.mybatis.session;

public interface IBeanSqlSession {

    /**
     * Find bean by id
     *
     * @param beanClass bean class
     * @param id        identifier unique
     * @return bean
     */
    <E> E findById(Class<E> beanClass, Object id);

    /**
     * Insert bean, generate id and set version to 0, set Tracable
     *
     * @param beanClass bean class
     * @param bean      bean to insert
     * @return 0 or 1 if insert
     */
    <E> int insert(Class<E> beanClass, E bean);

    /**
     * Update bean, update version +1, set Tracable
     *
     * @param beanClass  bean class
     * @param bean       bean to update
     * @param properties properties to update, if null then all
     * @return 0 or 1 if update
     */
    <E> int update(Class<E> beanClass, E bean, String... properties);

    /**
     * Delete bean
     *
     * @param beanClass bean class
     * @param bean      bean to delete
     * @return 0 or 1 if delete
     */
    <E> int delete(Class<E> beanClass, E bean);

}
