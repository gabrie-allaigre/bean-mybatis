package com.talanlabs.bean.mybatis.data.check;

import com.talanlabs.bean.mybatis.helper.IId;
import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.bean.mybatis.data.EntityBean;

@Entity(name = "")
public class WrongEntityBean extends EntityBean {

    private IId trainId;

    public IId getTrainId() {
        return trainId;
    }

    public void setTrainId(IId trainId) {
        this.trainId = trainId;
    }
}
