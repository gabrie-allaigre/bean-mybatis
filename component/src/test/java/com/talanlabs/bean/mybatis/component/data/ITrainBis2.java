package com.talanlabs.bean.mybatis.component.data;

import com.talanlabs.bean.mybatis.component.IEntity;
import com.talanlabs.bean.mybatis.annotation.*;
import com.talanlabs.component.annotation.ComponentBean;

import java.util.List;

@Entity(name = "T_TRAIN")
@ComponentBean
public interface ITrainBis2 extends IEntity {

    @Column(name = "CODE")
    String getCode();

    void setCode(String code);

    @Collection(propertyTarget = WagonBis2Fields.trainId, orderBy = @OrderBy(WagonBis2Fields.position), fetchType = FetchType.EAGER, nestedOption = @NestedOption(depth = 2))
    List<IWagonBis2> getWagons();

    void setWagons(List<IWagonBis2> wagons);
}
