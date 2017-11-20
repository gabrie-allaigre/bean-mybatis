package com.talanlabs.bean.mybatis.component.data;

import com.talanlabs.bean.mybatis.component.IEntity;
import com.talanlabs.bean.mybatis.component.ITracable;
import com.talanlabs.bean.mybatis.helper.IId;
import com.talanlabs.bean.mybatis.annotation.Association;
import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.component.annotation.ComponentBean;

@ComponentBean
@Entity(name = "T_ADDRESS")
public interface IAddress extends IEntity, ITracable {

    @Column(name = "CITY")
    String getCity();

    void setCity(String city);

    @Column(name = "POSTAL_ZIP")
    String getPostalZip();

    void setPostalZip(String postalZip);

    @Column(name = "COUNTRY_ID")
    IId getCountryId();

    void setCountryId(IId countryId);

    @Association(propertySource = AddressFields.countryId)
    ICountry getCountry();

    void setCountry(ICountry country);

}
