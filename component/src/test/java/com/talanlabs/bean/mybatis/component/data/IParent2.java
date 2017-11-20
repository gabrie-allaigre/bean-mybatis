package com.talanlabs.bean.mybatis.component.data;

import com.talanlabs.bean.mybatis.component.IEntity;
import com.talanlabs.bean.mybatis.component.ITracable;
import com.talanlabs.bean.mybatis.helper.IId;
import com.talanlabs.bean.mybatis.annotation.*;
import com.talanlabs.component.annotation.ComponentBean;

@ComponentBean
@Entity(name = "T_PARENT2")
public interface IParent2 extends IEntity, ITracable {

    @Column(name = "NAME")
    String getName();

    void setName(String name);

    @Column(name = "COUNTRY1_ID")
    IId getCountry1Id();

    void setCountry1Id(IId country1Id);

    @Association(propertySource = Parent2Fields.country1Id, fetchType = FetchType.EAGER, nestedOption = @NestedOption(depth = 1))
    ICountry getCountry1();

    void setCountry1(ICountry country1);

    @Column(name = "COUNTRY2_ID")
    IId getCountry2Id();

    void setCountry2Id(IId country2Id);

    @Association(propertySource = Parent2Fields.country2Id, fetchType = FetchType.EAGER, nestedOption = @NestedOption(depth = 1))
    ICountry getCountry2();

    void setCountry2(ICountry country2);

}
