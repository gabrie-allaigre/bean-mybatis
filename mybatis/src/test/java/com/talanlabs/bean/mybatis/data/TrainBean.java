package com.talanlabs.bean.mybatis.data;

import com.talanlabs.bean.mybatis.helper.IId;
import com.talanlabs.bean.mybatis.annotation.Association;
import com.talanlabs.bean.mybatis.annotation.Collection;
import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Entity;

import java.util.List;

@Entity(name = "T_TRAIN")
public class TrainBean extends EntityBean {

    @Column(name = "CODE")
    private String code;
    @Collection(propertyTarget = "trainId")
    private List<WagonBean> wagons;
    @Column(name = "START_COUNTRY_ID")
    private IId startCountryId;
    @Association(propertySource = "startCountryId")
    private CountryBean startCountry;
    @Column(name = "END_COUNTRY_ID")
    private IId endCountryId;
    @Association(propertySource = "endCountryId")
    private CountryBean endCountry;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<WagonBean> getWagons() {
        return wagons;
    }

    public void setWagons(List<WagonBean> wagons) {
        this.wagons = wagons;
    }

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

    public IId getEndCountryId() {
        return endCountryId;
    }

    public void setEndCountryId(IId endCountryId) {
        this.endCountryId = endCountryId;
    }

    public CountryBean getEndCountry() {
        return endCountry;
    }

    public void setEndCountry(CountryBean endCountry) {
        this.endCountry = endCountry;
    }
}
