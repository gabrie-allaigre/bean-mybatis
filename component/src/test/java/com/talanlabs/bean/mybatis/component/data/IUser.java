package com.talanlabs.bean.mybatis.component.data;

import com.talanlabs.bean.mybatis.component.IEntity;
import com.talanlabs.bean.mybatis.component.ITracable;
import com.talanlabs.bean.mybatis.helper.IId;
import com.talanlabs.bean.mybatis.annotation.*;
import com.talanlabs.component.annotation.ComponentBean;

import java.util.List;

@ComponentBean
@Entity(name = "T_USER")
@Cache
public interface IUser extends IEntity, ITracable {

    @Column(name = "LOGIN")
    String getLogin();

    void setLogin(String login);

    @Collection(propertyTarget = GroupFields.userId)
    List<IGroup> getGroups();

    void setGroups(List<IGroup> groups);

    @Column(name = "COUNTRY_ID")
    IId getCountryId();

    void setCountryId(IId countryId);

    @Association(propertySource = UserFields.countryId)
    ICountry getCountry();

    void setCountry(ICountry country);

    @Column(name = "COUNTRY_CODE")
    String getCountryCode();

    void setCountryCode(String countryCode);

    @Association(propertySource = { UserFields.countryCode, UserFields.countryId }, propertyTarget = { CountryFields.code, CountryFields.id })
    ICountry getCountryOther();

    void setCountryOther(ICountry countryOther);

    @Column(name = "ADDRESS_ID")
    IId getAddressId();

    void setAddressId(IId addressId);

    @Association(propertySource = UserFields.addressId)
    IAddress getAddress();

    void setAddress(IAddress address);

    //@Collection(joinTable = { @JoinTable(name = "t_asso_user_toto", left = "user_id", right = "toto_id"), @JoinTable(name = "t_asso_toto_address", left = "toto_id", right = "address_id") })
    @Collection(propertyTarget = AddressFields.id, joinTable = @JoinTable(name = "t_asso_user_address", left = "user_id", right = "address_id"))
    List<IAddress> getAddresses();

    void setAddresses(List<IAddress> addresses);

}
