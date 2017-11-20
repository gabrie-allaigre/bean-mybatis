package com.talanlabs.bean.mybatis.session.observer;

import org.apache.ibatis.session.SqlSession;

public abstract class AbstractTriggerObserver implements ITriggerObserver {

    @Override
    public void triggerBefore(SqlSession sqlSession, Type type, Object bean) {
        // Nothing
    }

    @Override
    public void triggerAfter(SqlSession sqlSession, Type type, Object bean) {
        // Nothing
    }
}
