package com.talanlabs.bean.mybatis.component.data;

import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.NlsColumn;
import com.talanlabs.bean.mybatis.annotation.Version;
import com.talanlabs.component.IComponent;
import com.talanlabs.component.annotation.ComponentBean;

@ComponentBean
public interface IFake extends IComponent {

    String getName();

    void setName(String name);

    @Column(name = "")
    @Version
    String getName2();

    void setName2(String name2);

    @NlsColumn(name = "")
    String getName3();

    void setName3(String name3);

}
