package com.talanlabs.bean.mybatis.component.data;

import com.talanlabs.bean.mybatis.annotation.Association;
import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.bean.mybatis.annotation.JoinTable;
import com.talanlabs.bean.mybatis.component.IEntity;
import com.talanlabs.bean.mybatis.component.ITracable;
import com.talanlabs.bean.mybatis.helper.IId;
import com.talanlabs.component.annotation.ComponentBean;

import java.util.Date;

@ComponentBean
@Entity(name = "T_PERSON")
public interface IPerson extends IEntity, ITracable {

    @Column(name = "FIRST_NAME")
    String getFirstName();

    void setFirstName(String firstName);

    @Column(name = "LAST_NAME")
    String getLastName();

    void setLastName(String lastName);

    @Column(name = "AGE")
    int getAge();

    void setAge(int age);

    @Column(name = "HEIGHT")
    float getHeight();

    void setHeight(float height);

    @Column(name = "WEIGHT")
    double getWeight();

    void setWeight(double weight);

    @Column(name = "SEXE")
    Sexe getSexe();

    void setSexe(Sexe sexe);

    @Column(name = "BIRTHDAY")
    Date getBirthday();

    void setBirthday(Date birthday);

    @Column(name = "ADDRESS_ID")
    IId getAddressId();

    void setAddressId(IId addressId);

    @Association(propertySource = PersonFields.addressId)
    IAddress getAddress();

    void setAddress(IAddress address);

    @Column(name = "ADDRESS_BIS_ID")
    IId getAddressBisId();

    void setAddressBisId(IId addressBisId);

    @Association(propertySource = PersonFields.addressBisId)
    IAddress getAddressBis();

    void setAddressBis(IAddress addressBis);

    @Association(propertySource = PersonFields.id, joinTable = @JoinTable(name = "T_ASSO_PERSON_ADDRESS", left = "PERSON_ID", right = "ADDRESS_ID"))
    IAddress getAddress2();

    void setAddress2(IAddress address2);

    @Association(propertySource = PersonFields.id, propertyTarget = AddressFields.city, joinTable = { @JoinTable(name = "T_ASSO_PERSON_INT", left = "PERSON_ID", right = "INT_ID"),
            @JoinTable(name = "T_ASSO_INT_ADDRESS", left = "INT_ID", right = "ADDRESS_ID") })
    IAddress getAddress3();

    void setAddress3(IAddress address3);

    enum Sexe {
        MAN, WOMAN
    }

}
