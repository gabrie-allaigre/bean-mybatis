package com.talanlabs.bean.mybatis.session.dispatcher;

import com.talanlabs.bean.mybatis.session.observer.ITriggerObserver;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;

public class TriggerDispatcher {

    private List<ITriggerObserver> triggerObservers = new ArrayList<>();

    /**
     * Add trigger observer
     *
     * @param triggerObserver observer
     */
    public void addTriggerObserver(ITriggerObserver triggerObserver) {
        triggerObservers.add(triggerObserver);
    }

    /**
     * Remove trigger observer
     *
     * @param triggerObserver observer
     */
    public void removeTriggerObserver(ITriggerObserver triggerObserver) {
        triggerObservers.remove(triggerObserver);
    }

    /**
     * Trigger call before insert, update or delete bean
     *
     * @param sqlSession current sqlSession
     * @param type       insert, update or delete
     * @param bean  bean
     */
    public void triggerBefore(SqlSession sqlSession, ITriggerObserver.Type type, Object bean) {
        for (ITriggerObserver triggerObserver : triggerObservers) {
            triggerObserver.triggerBefore(sqlSession, type, bean);
        }
    }

    /**
     * Trigger call after insert, update or delete bean
     *
     * @param sqlSession
     * @param type       insert, update or delete
     * @param bean  bean
     */
    public void triggerAfter(SqlSession sqlSession, ITriggerObserver.Type type, Object bean) {
        for (ITriggerObserver triggerObserver : triggerObservers) {
            triggerObserver.triggerAfter(sqlSession, type, bean);
        }
    }
}
