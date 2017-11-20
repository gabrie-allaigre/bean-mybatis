package com.talanlabs.bean.mybatis.data;

import com.talanlabs.bean.mybatis.helper.IId;
import com.talanlabs.bean.mybatis.annotation.Collection;
import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Entity;

import java.util.List;

@Entity(name = "T_WAGON")
public class WagonBean extends EntityBean {

    @Column(name = "TRAIN_ID")
    private IId trainId;
    @Column(name = "CODE")
    private String code;
    @Column(name = "POSITION")
    private Integer position;
    @Collection(propertyTarget = "wagonId")
    private List<ContainerBean> containers;
    @Collection(propertyTarget = "wagonId")
    private List<WheelBean> wheels;

    public IId getTrainId() {
        return trainId;
    }

    public void setTrainId(IId trainId) {
        this.trainId = trainId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public List<ContainerBean> getContainers() {
        return containers;
    }

    public void setContainers(List<ContainerBean> containers) {
        this.containers = containers;
    }

    public List<WheelBean> getWheels() {
        return wheels;
    }

    public void setWheels(List<WheelBean> wheels) {
        this.wheels = wheels;
    }

    @Entity(name = "T_WHEEL")
    public static class WheelBean extends EntityBean {

        @Column(name = "WAGON_ID")
        private IId wagonId;
        @Column(name = "SIZE")
        private Size size;

        public IId getWagonId() {
            return wagonId;
        }

        public void setWagonId(IId wagonId) {
            this.wagonId = wagonId;
        }

        public Size getSize() {
            return size;
        }

        public void setSize(Size size) {
            this.size = size;
        }

        enum Size {

            A, B, C

        }
    }
}
