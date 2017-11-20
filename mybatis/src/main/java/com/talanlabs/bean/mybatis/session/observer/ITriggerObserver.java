package com.talanlabs.bean.mybatis.session.observer;

import org.apache.ibatis.session.SqlSession;

public interface ITriggerObserver {

    /**
     * Trigger call before insert, update or delete bean
     *
     * @param sqlSession current sqlSession
     * @param type       insert, update or delete
     * @param bean       bean
     */
    void triggerBefore(SqlSession sqlSession, Type type, Object bean);

    /**
     * Trigger call after insert, update or delete bean
     *
     * @param sqlSession current sqlSession
     * @param type       insert, update or delete
     * @param bean       bean
     */
    void triggerAfter(SqlSession sqlSession, Type type, Object bean);

    enum Type {
        Insert, Update, Delete
    }
}
