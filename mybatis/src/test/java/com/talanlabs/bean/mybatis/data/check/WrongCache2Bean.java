package com.talanlabs.bean.mybatis.data.check;

import com.talanlabs.bean.mybatis.helper.IId;
import com.talanlabs.bean.mybatis.annotation.Cache;
import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.bean.mybatis.data.EntityBean;

@Entity(name = "T_TOTO")
@Cache(links = WrongColumnBean.class)
public class WrongCache2Bean extends EntityBean {

    @Column(name = "trainId")
    private IId trainId;

    public IId getTrainId() {
        return trainId;
    }

    public void setTrainId(IId trainId) {
        this.trainId = trainId;
    }
}
