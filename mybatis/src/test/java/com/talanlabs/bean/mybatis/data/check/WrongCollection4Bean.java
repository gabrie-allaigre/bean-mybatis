package com.talanlabs.bean.mybatis.data.check;

import com.talanlabs.bean.mybatis.annotation.Collection;
import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.bean.mybatis.data.CountryBean;
import com.talanlabs.bean.mybatis.data.EntityBean;

import java.util.List;

@Entity(name = "T_CONTAINER")
public class WrongCollection4Bean extends EntityBean {

    @Collection(propertyTarget = "code")
    private List<WrongNotBean> countries;

    public List<WrongNotBean> getCountries() {
        return countries;
    }

    public void setCountries(List<WrongNotBean> countries) {
        this.countries = countries;
    }
}
