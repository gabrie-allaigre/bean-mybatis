package com.talanlabs.bean.mybatis.data;

import com.talanlabs.bean.mybatis.helper.IId;
import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Entity;

@Entity(name = "T_CONTAINER")
public class ContainerBean extends EntityBean {

    @Column(name = "WAGON_ID")
    private IId wagonId;
    @Column(name = "CODE")
    private String code;

    public IId getWagonId() {
        return wagonId;
    }

    public void setWagonId(IId wagonId) {
        this.wagonId = wagonId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
