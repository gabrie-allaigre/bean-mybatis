package com.talanlabs.bean.mybatis.data.check;

import com.talanlabs.bean.mybatis.annotation.Collection;
import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.bean.mybatis.annotation.JoinTable;
import com.talanlabs.bean.mybatis.data.CountryBean;
import com.talanlabs.bean.mybatis.data.EntityBean;

import java.util.List;

@Entity(name = "T_CONTAINER")
public class WrongCollection5Bean extends EntityBean {

    @Collection(propertyTarget = "id", joinTable = @JoinTable(name = "t_asso", left = "id", right = { "one", "two" }))
    private List<CountryBean> countries;

    public List<CountryBean> getCountries() {
        return countries;
    }

    public void setCountries(List<CountryBean> countries) {
        this.countries = countries;
    }
}
