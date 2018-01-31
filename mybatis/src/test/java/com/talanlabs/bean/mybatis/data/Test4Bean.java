package com.talanlabs.bean.mybatis.data;

import com.talanlabs.bean.mybatis.annotation.Association;
import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.bean.mybatis.annotation.FetchType;
import com.talanlabs.bean.mybatis.annotation.NestedOption;

@Entity(name = "T_TEST3")
public class Test4Bean extends EntityBean {

    @Association(propertySource = "id",propertyTarget = "trainId",fetchType = FetchType.EAGER, nestedOption = @NestedOption(depth = -1))
    private WagonBean wagon;

    public WagonBean getWagon() {
        return wagon;
    }

    public void setWagon(WagonBean wagon) {
        this.wagon = wagon;
    }
}
