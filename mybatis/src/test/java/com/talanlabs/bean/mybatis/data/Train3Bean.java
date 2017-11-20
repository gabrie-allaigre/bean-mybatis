package com.talanlabs.bean.mybatis.data;

import com.talanlabs.bean.mybatis.annotation.Collection;
import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.bean.mybatis.annotation.OrderBy;

import java.util.List;

@Entity(name = "T_TRAIN")
public class Train3Bean extends TrainBean {

    @Collection(propertyTarget = "trainId", orderBy = @OrderBy(value = "position", sort = OrderBy.Sort.Asc))
    public List<WagonBean> getWagons() {
        return super.getWagons();
    }
}
