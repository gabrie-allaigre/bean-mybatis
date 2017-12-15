package com.talanlabs.bean.mybatis.component.data;

import com.talanlabs.bean.mybatis.component.IEntity;
import com.talanlabs.bean.mybatis.helper.IId;
import com.talanlabs.bean.mybatis.annotation.*;
import com.talanlabs.component.IComponent;
import com.talanlabs.component.annotation.ComponentBean;

import java.util.List;

@Entity(name = "T_WAGON")
@ComponentBean
public interface IWagonBis extends IEntity {

    @Column(name = "TRAIN_ID")
    IId getTrainId();

    void setTrainId(IId trainId);

    @Column(name = "CODE")
    String getCode();

    void setCode(String code);

    @Column(name = "POSITION")
    Integer getPosition();

    void setPosition(Integer position);

    @Collection(propertyTarget = ContainerFields.wagonId, fetchType = FetchType.EAGER, nestedOption = @NestedOption(depth = 1))
    List<IContainer> getContainers();

    void setContainers(List<IContainer> containers);

    @Collection(propertyTarget = WheelFields.wagonId, fetchType = FetchType.EAGER, nestedOption = @NestedOption(depth = 1))
    List<IWheelBis> getWheels();

    void setWheels(List<IWheelBis> wheels);

    @Entity(name = "T_WHEEL")
    @ComponentBean
    interface IWheelBis extends IComponent {

        @Column(name = "WAGON_ID")
        IId getWagonId();

        void setWagonId(IId wagonId);

        @Column(name = "SIZE")
        Size getSize();

        void setSize(Size size);

        enum Size {

            A, B, C

        }

    }
}
