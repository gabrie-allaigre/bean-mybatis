package com.talanlabs.bean.mybatis.component.data;

import com.talanlabs.bean.mybatis.component.ICancelable;
import com.talanlabs.bean.mybatis.component.IEntity;
import com.talanlabs.bean.mybatis.component.ITracable;
import com.talanlabs.bean.mybatis.annotation.Cache;
import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.bean.mybatis.annotation.NlsColumn;
import com.talanlabs.component.annotation.ComponentBean;

@Entity(name = "T_COUNTRY")
@ComponentBean
@Cache(links = IUser.class)
public interface ICountry2 extends IEntity, ITracable, ICancelable {

    @Column(name = "CODE", typeHandler = TestTypeHandler.class)
    String getCode();

    void setCode(String code);

    @NlsColumn(name = "NAME", typeHandler = TestTypeHandler.class)
    String getName();

    void setName(String name);

}
