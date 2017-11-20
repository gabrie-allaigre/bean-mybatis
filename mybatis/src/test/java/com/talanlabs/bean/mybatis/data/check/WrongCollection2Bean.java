package com.talanlabs.bean.mybatis.data.check;

import com.talanlabs.bean.mybatis.annotation.Collection;
import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.bean.mybatis.data.CountryBean;
import com.talanlabs.bean.mybatis.data.EntityBean;

import java.util.List;

@Entity(name = "T_CONTAINER")
public class WrongCollection2Bean extends EntityBean {

    @Collection(propertyTarget = "code")
    private CountryBean countries;

    public CountryBean getCountries() {
        return countries;
    }

    public void setCountries(CountryBean countries) {
        this.countries = countries;
    }
}
