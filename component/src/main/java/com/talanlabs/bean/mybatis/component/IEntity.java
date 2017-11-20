package com.talanlabs.bean.mybatis.component;

import com.talanlabs.bean.mybatis.helper.IId;
import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Id;
import com.talanlabs.bean.mybatis.annotation.Version;
import com.talanlabs.bean.mybatis.helper.IdKeyGenerator;
import com.talanlabs.component.IComponent;
import com.talanlabs.component.annotation.ComponentBean;

@ComponentBean
public interface IEntity extends IComponent {

    @EqualsKey
    @Id(keyGeneratorClass = IdKeyGenerator.class)
    @Column(name = "ID")
    IId getId();

    void setId(IId id);

    @Version
    @Column(name = "VERSION")
    Integer getVersion();

    void setVersion(Integer version);

}
