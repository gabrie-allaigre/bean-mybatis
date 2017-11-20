package com.talanlabs.bean.mybatis.data.check;

import com.talanlabs.bean.mybatis.helper.IId;
import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.bean.mybatis.annotation.Version;
import com.talanlabs.bean.mybatis.data.EntityBean;

@Entity(name = "T_CONTAINER")
public class WrongVersionBean extends EntityBean {

    @Column(name = "WAGON_ID")
    private IId wagonId;
    @Column(name = "CODE")
    @Version
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
