package com.talanlabs.bean.mybatis.component.data;

import com.talanlabs.bean.mybatis.helper.IId;
import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.component.IComponent;
import com.talanlabs.component.annotation.ComponentBean;


@Entity(name = "T_CONTAINER")
@ComponentBean
public interface IContainer extends IComponent {

    @Column(name = "WAGON_ID")
    IId getWagonId();

    void setWagonId(IId wagonId);

    @Column(name = "CODE")
    String getCode();

    void setCode(String code);

}
