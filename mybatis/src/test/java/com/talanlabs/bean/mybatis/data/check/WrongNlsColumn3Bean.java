package com.talanlabs.bean.mybatis.data.check;

import com.talanlabs.bean.mybatis.helper.IId;
import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.bean.mybatis.annotation.NlsColumn;
import com.talanlabs.bean.mybatis.data.EntityBean;

@Entity(name = "T_CONTAINER")
public class WrongNlsColumn3Bean extends EntityBean {

    @Column(name = "wagon_id")
    private IId wagonId;
    @NlsColumn(name = "CODE", propertySource = "toto")
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
