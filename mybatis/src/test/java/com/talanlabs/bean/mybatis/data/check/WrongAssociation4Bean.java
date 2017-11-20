package com.talanlabs.bean.mybatis.data.check;

import com.talanlabs.bean.mybatis.helper.IId;
import com.talanlabs.bean.mybatis.annotation.Association;
import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.bean.mybatis.data.CountryBean;
import com.talanlabs.bean.mybatis.data.EntityBean;

@Entity(name = "T_CONTAINER")
public class WrongAssociation4Bean extends EntityBean {

    @Column(name = "START_COUNTRY_ID")
    private IId startCountryId;
    @Association(propertySource = "startCountryId", propertyTarget = { "code", "id" })
    private CountryBean startCountry;

    public IId getStartCountryId() {
        return startCountryId;
    }

    public void setStartCountryId(IId startCountryId) {
        this.startCountryId = startCountryId;
    }

    public CountryBean getStartCountry() {
        return startCountry;
    }

    public void setStartCountry(CountryBean startCountry) {
        this.startCountry = startCountry;
    }
}
