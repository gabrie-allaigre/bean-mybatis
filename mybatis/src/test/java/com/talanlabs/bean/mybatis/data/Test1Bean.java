package com.talanlabs.bean.mybatis.data;

import com.talanlabs.bean.mybatis.helper.IId;
import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Entity;

@Entity(name = "T_WAGON")
public class Test1Bean extends EntityBean {

    @Column(name = "TRAIN_ID", javaType = String.class)
    private IId trainId;

    public IId getTrainId() {
        return trainId;
    }

    public void setTrainId(IId trainId) {
        this.trainId = trainId;
    }
}
