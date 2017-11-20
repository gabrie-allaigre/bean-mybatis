package com.talanlabs.bean.mybatis.it.observer;

import com.talanlabs.bean.mybatis.data.ICancelable;
import com.talanlabs.bean.mybatis.data.ITracable;
import com.talanlabs.bean.mybatis.session.observer.AbstractTriggerObserver;
import org.apache.ibatis.session.SqlSession;

import java.util.Date;

public class TracableTriggerObserver extends AbstractTriggerObserver {

    private final IUserByHandler userByHandler;

    public TracableTriggerObserver(IUserByHandler userByHandler) {
        super();

        this.userByHandler = userByHandler;
    }

    @Override
    public void triggerBefore(SqlSession sqlSession, Type type, Object bean) {
        if (userByHandler != null) {
            if (bean instanceof ITracable) {
                ITracable tracable = (ITracable) bean;
                switch (type) {
                    case Insert:
                        tracable.setCreatedBy(userByHandler.getUserBy());
                        tracable.setCreatedDate(new Date());
                        break;
                    case Update:
                        tracable.setUpdatedBy(userByHandler.getUserBy());
                        tracable.setUpdatedDate(new Date());
                        break;
                    case Delete:
                        break;
                    default:
                        break;
                }
            }

            if (bean instanceof ICancelable) {
                ICancelable cancelable = (ICancelable) bean;
                if (cancelable.isCanceled() && cancelable.getCanceledDate() == null) {
                    switch (type) {
                        case Insert:
                            break;
                        case Update:
                            cancelable.setCanceledBy(userByHandler.getUserBy());
                            cancelable.setCanceledDate(new Date());
                            break;
                        case Delete:
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    public interface IUserByHandler {

        /**
         * Get user
         *
         * @return user
         */
        String getUserBy();

    }
}
