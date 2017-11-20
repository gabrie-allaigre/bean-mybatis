package com.talanlabs.bean.mybatis.component.data;

import com.talanlabs.bean.mybatis.component.IEntity;
import com.talanlabs.bean.mybatis.component.ITracable;
import com.talanlabs.bean.mybatis.helper.IId;
import com.talanlabs.bean.mybatis.annotation.*;
import com.talanlabs.component.annotation.ComponentBean;

@ComponentBean
@Entity(name = "T_PARENT1")
public interface IParent1 extends IEntity, ITracable {

    @Column(name = "NAME")
    String getName();

    void setName(String name);

    @Column(name = "COUNTRY_ID")
    IId getCountryId();

    void setCountryId(IId countryId);

    @Association(propertySource = Parent1Fields.countryId, fetchType = FetchType.EAGER, nestedOption = @NestedOption(depth = 1))
    ICountry getCountry();

    void setCountry(ICountry country);

}
