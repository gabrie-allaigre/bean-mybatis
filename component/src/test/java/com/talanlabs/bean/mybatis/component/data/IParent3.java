package com.talanlabs.bean.mybatis.component.data;

import com.talanlabs.bean.mybatis.component.IEntity;
import com.talanlabs.bean.mybatis.component.ITracable;
import com.talanlabs.bean.mybatis.annotation.*;
import com.talanlabs.component.annotation.ComponentBean;

@ComponentBean
@Entity(name = "T_PARENT1")
public interface IParent3 extends IEntity, ITracable {

    @Column(name = "NAME")
    String getName();

    void setName(String name);

    @Association(propertySource = Parent1Fields.id, joinTable = @JoinTable(name = "t_asso_parent_country", left = "parent_id", right = "country_id"), fetchType = FetchType.EAGER, nestedOption = @NestedOption(depth = 1))
    ICountry getCountry();

    void setCountry(ICountry country);

}
