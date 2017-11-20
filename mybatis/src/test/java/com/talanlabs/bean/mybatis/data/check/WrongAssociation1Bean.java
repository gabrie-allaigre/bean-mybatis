package com.talanlabs.bean.mybatis.data.check;

import com.talanlabs.bean.mybatis.annotation.Association;
import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.bean.mybatis.data.CountryBean;
import com.talanlabs.bean.mybatis.data.EntityBean;

@Entity(name = "T_CONTAINER")
public class WrongAssociation1Bean extends EntityBean {

    @Association(propertySource = "startCountryId")
    private CountryBean startCountry;

    public CountryBean getStartCountry() {
        return startCountry;
    }

    public void setStartCountry(CountryBean startCountry) {
        this.startCountry = startCountry;
    }
}
