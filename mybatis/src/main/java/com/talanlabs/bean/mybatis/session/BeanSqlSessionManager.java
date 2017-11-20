package com.talanlabs.bean.mybatis.session;

import com.talanlabs.bean.mybatis.session.defaults.DefaultBeanSqlSession;
import org.apache.ibatis.session.SqlSessionManager;

public class BeanSqlSessionManager implements IBeanSqlSession {

    private final IBeanSqlSession beanSqlSession;

    private BeanSqlSessionManager(IBeanSqlSession beanSqlSession) {
        super();

        this.beanSqlSession = beanSqlSession;
    }

    public static BeanSqlSessionManager newInstance(SqlSessionManager sqlSessionManager) {
        return new BeanSqlSessionManager(new DefaultBeanSqlSession(sqlSessionManager));
    }

    @Override
    public <E> E findById(Class<E> beanClass, Object id) {
        return beanSqlSession.findById(beanClass, id);
    }

    @Override
    public <E> int insert(Class<E> beanClass, E bean) {
        return beanSqlSession.insert(beanClass, bean);
    }

    @Override
    public <E> int update(Class<E> beanClass, E bean, String... properties) {
        return beanSqlSession.update(beanClass, bean, properties);
    }

    @Override
    public <E> int delete(Class<E> beanClass, E bean) {
        return beanSqlSession.delete(beanClass, bean);
    }
}
