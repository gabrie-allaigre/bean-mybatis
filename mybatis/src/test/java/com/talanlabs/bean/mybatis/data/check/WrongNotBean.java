package com.talanlabs.bean.mybatis.data.check;

import com.talanlabs.bean.mybatis.helper.IId;
import com.talanlabs.bean.mybatis.data.EntityBean;

public class WrongNotBean extends EntityBean {

    private IId trainId;

    public IId getTrainId() {
        return trainId;
    }

    public void setTrainId(IId trainId) {
        this.trainId = trainId;
    }
}
