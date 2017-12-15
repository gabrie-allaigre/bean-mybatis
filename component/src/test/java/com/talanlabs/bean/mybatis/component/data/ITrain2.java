package com.talanlabs.bean.mybatis.component.data;

import com.talanlabs.bean.mybatis.component.IEntity;
import com.talanlabs.bean.mybatis.annotation.Collection;
import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.bean.mybatis.annotation.OrderBy;
import com.talanlabs.component.annotation.ComponentBean;

import java.util.List;

@Entity(name = "T_TRAIN")
@ComponentBean
public interface ITrain2 extends IEntity {

    @Column(name = "CODE")
    String getCode();

    void setCode(String code);

    @Collection(propertyTarget = WagonFields.trainId, orderBy = @OrderBy(WagonFields.position))
    List<IWagon> getWagons();

    void setWagons(List<IWagon> wagons);
}
