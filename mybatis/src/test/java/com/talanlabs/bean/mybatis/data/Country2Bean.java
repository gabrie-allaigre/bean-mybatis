package com.talanlabs.bean.mybatis.data;

import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.bean.mybatis.annotation.FetchType;
import com.talanlabs.bean.mybatis.annotation.NlsColumn;

@Entity(name = "T_COUNTRY")
public class Country2Bean extends CountryBean {

    @NlsColumn(name = "NAME", fetchType = FetchType.EAGER, nested = true)
    public String getName() {
        return super.getName();
    }

}
