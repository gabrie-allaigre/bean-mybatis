package com.talanlabs.bean.mybatis.data;

import com.talanlabs.bean.mybatis.annotation.Association;
import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.bean.mybatis.annotation.FetchType;
import com.talanlabs.bean.mybatis.annotation.NestedOption;
import com.talanlabs.bean.mybatis.annotation.NlsColumn;
import com.talanlabs.bean.mybatis.helper.IId;

@Entity(name = "T_TEST2")
public class Test3Bean extends EntityBean {

    @NlsColumn(name = "NAME1", fetchType = FetchType.EAGER, nested = true)
    private String name1;
    @NlsColumn(name = "NAME2", fetchType = FetchType.EAGER, nested = true)
    private String name2;
    @Column(name = "START_COUNTRY_ID")
    private IId startCountryId;
    @Association(propertySource = "startCountryId", fetchType = FetchType.EAGER, nestedOption = @NestedOption(depth = -1))
    private Country2Bean startCountry;
    @Column(name = "END_COUNTRY_ID")
    private IId endCountryId;
    @Association(propertySource = "endCountryId", fetchType = FetchType.EAGER, nestedOption = @NestedOption(depth = -1))
    private Country2Bean endCountry;

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public IId getStartCountryId() {
        return startCountryId;
    }

    public void setStartCountryId(IId startCountryId) {
        this.startCountryId = startCountryId;
    }

    public Country2Bean getStartCountry() {
        return startCountry;
    }

    public void setStartCountry(Country2Bean startCountry) {
        this.startCountry = startCountry;
    }

    public IId getEndCountryId() {
        return endCountryId;
    }

    public void setEndCountryId(IId endCountryId) {
        this.endCountryId = endCountryId;
    }

    public Country2Bean getEndCountry() {
        return endCountry;
    }

    public void setEndCountry(Country2Bean endCountry) {
        this.endCountry = endCountry;
    }
}
