package com.talanlabs.bean.mybatis.component.it.config;

import com.talanlabs.bean.mybatis.component.observer.TracableTriggerObserver;

public class DefaultUserByHandler implements TracableTriggerObserver.IUserByHandler {

    private String userBy;

    public DefaultUserByHandler() {
        super();

        this.userBy = "unknow";
    }

    @Override
    public String getUserBy() {
        return userBy;
    }

    public void setUserBy(String userBy) {
        this.userBy = userBy;
    }
}
