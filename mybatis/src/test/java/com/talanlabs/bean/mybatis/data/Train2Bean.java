package com.talanlabs.bean.mybatis.data;

import com.talanlabs.bean.mybatis.annotation.*;

import java.util.List;

@Entity(name = "T_TRAIN")
public class Train2Bean extends TrainBean {

    @Override
    @Association(propertySource = "startCountryId", fetchType = FetchType.EAGER, nestedOption = @NestedOption(depth = 1))
    public CountryBean getStartCountry() {
        return super.getStartCountry();
    }

    @Override
    @Association(propertySource = "endCountryId", fetchType = FetchType.EAGER, nestedOption = @NestedOption(depth = 1))
    public CountryBean getEndCountry() {
        return super.getEndCountry();
    }

    @Override
    @Collection(propertyTarget = "trainId", fetchType = FetchType.EAGER, nestedOption = @NestedOption(depth = 1))
    public List<WagonBean> getWagons() {
        return super.getWagons();
    }
}
