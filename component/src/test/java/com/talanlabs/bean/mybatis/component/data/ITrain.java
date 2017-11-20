package com.talanlabs.bean.mybatis.component.data;

import com.talanlabs.bean.mybatis.component.IEntity;
import com.talanlabs.bean.mybatis.annotation.Collection;
import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.component.annotation.ComponentBean;

import java.util.List;

@Entity(name = "T_TRAIN")
@ComponentBean
public interface ITrain extends IEntity {

    @Column(name = "CODE")
    String getCode();

    void setCode(String code);

    @Collection(propertyTarget = WagonFields.trainId)
    List<IWagon> getWagons();

    void setWagons(List<IWagon> wagons);
}
