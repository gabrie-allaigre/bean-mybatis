package com.talanlabs.bean.mybatis.component.data;

import com.talanlabs.bean.mybatis.component.ICancelable;
import com.talanlabs.bean.mybatis.component.IEntity;
import com.talanlabs.bean.mybatis.component.ITracable;
import com.talanlabs.bean.mybatis.helper.IId;
import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.component.annotation.ComponentBean;

@Entity(name = "T_GROUP")
@ComponentBean
public interface IGroup extends IEntity, ITracable, ICancelable {

    @Column(name = "USER_ID")
    IId getUserId();

    void setUserId(IId userId);

    @Column(name = "NAME")
    String getName();

    void setName(String name);

}
