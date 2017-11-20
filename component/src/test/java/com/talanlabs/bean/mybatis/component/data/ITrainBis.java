package com.talanlabs.bean.mybatis.component.data;

import com.talanlabs.bean.mybatis.component.IEntity;
import com.talanlabs.bean.mybatis.annotation.*;
import com.talanlabs.component.annotation.ComponentBean;

import java.util.List;

@Entity(name = "T_TRAIN")
@ComponentBean
public interface ITrainBis extends IEntity {

    @Column(name = "CODE")
    String getCode();

    void setCode(String code);

    @Collection(propertyTarget = WagonFields.trainId, fetchType = FetchType.EAGER, nestedOption = @NestedOption(depth = 2))
    List<IWagonBis> getWagons();

    void setWagons(List<IWagonBis> wagons);
}
