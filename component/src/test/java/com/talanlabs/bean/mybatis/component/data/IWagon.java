package com.talanlabs.bean.mybatis.component.data;

import com.talanlabs.bean.mybatis.component.IEntity;
import com.talanlabs.bean.mybatis.helper.IId;
import com.talanlabs.bean.mybatis.annotation.Collection;
import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.component.IComponent;
import com.talanlabs.component.annotation.ComponentBean;

import java.util.List;

@Entity(name = "T_WAGON")
@ComponentBean
public interface IWagon extends IEntity {

    @Column(name = "TRAIN_ID")
    IId getTrainId();

    void setTrainId(IId trainId);

    @Column(name = "CODE")
    String getCode();

    void setCode(String code);

    @Column(name = "POSITION")
    Integer getPosition();

    void setPosition(Integer position);

    @Collection(propertyTarget = ContainerFields.wagonId)
    List<IContainer> getContainers();

    void setContainers(List<IContainer> containers);

    @Collection(propertyTarget = WheelFields.wagonId)
    List<IWheel> getWheels();

    void setWheels(List<IWheel> wheels);

    @Entity(name = "T_WHEEL")
    @ComponentBean
    interface IWheel extends IComponent {

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
