package com.talanlabs.bean.mybatis.component.data;

import com.talanlabs.bean.mybatis.component.IEntity;
import com.talanlabs.bean.mybatis.component.ITracable;
import com.talanlabs.bean.mybatis.helper.IId;
import com.talanlabs.bean.mybatis.annotation.*;
import com.talanlabs.component.annotation.ComponentBean;

@ComponentBean
@Entity(name = "T_PARENT4")
public interface IParent4 extends IEntity, ITracable {

    @Column(name = "NAME")
    String getName();

    void setName(String name);

    @Column(name = "PARENT1_ID")
    IId getParent1Id();

    void setParent1Id(IId parent1Id);

    @Association(propertySource = Parent4Fields.parent1Id, fetchType = FetchType.EAGER, nestedOption = @NestedOption(depth = -1))
    IParent1 getParent1();

    void setParent1(IParent1 parent1);

    @Column(name = "PARENT2_ID")
    IId getParent2Id();

    void setParent2Id(IId parent2Id);

    @Association(propertySource = Parent4Fields.parent2Id, fetchType = FetchType.EAGER, nestedOption = @NestedOption(depth = 1, alwaysNotNull = true))
    IParent2 getParent2();

    void setParent2(IParent2 parent2);

    @Column(name = "PARENT3_ID")
    IId getParent3Id();

    void setParent3Id(IId parent3Id);

    @Association(propertySource = Parent4Fields.parent3Id, fetchType = FetchType.EAGER, nestedOption = @NestedOption(depth = -1))
    IParent3 getParent3();

    void setParent3(IParent3 parent3);

    @Association(propertySource = Parent4Fields.id, joinTable = @JoinTable(name = "t_asso_parent4_parent3", left = "parent4_id", right = "parent3_id"), fetchType = FetchType.EAGER)
    IParent3 getParent3bis();

    void setParent3bis(IParent3 parent3bis);

    @Association(propertySource = Parent4Fields.id, joinTable = @JoinTable(name = "t_asso_parent4_parent3", left = "parent4_id", right = "parent3_id"), fetchType = FetchType.EAGER, nestedOption = @NestedOption(depth = -1))
    IParent3 getParent3ter();

    void setParent3ter(IParent3 parent3ter);

}
