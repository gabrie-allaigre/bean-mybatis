package com.talanlabs.bean.mybatis.component.data;

import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.component.IComponent;
import com.talanlabs.component.annotation.ComponentBean;

@ComponentBean
@Entity(name = "")
public interface IFake2 extends IComponent {

    String getName();

    void setName(String name);

}
