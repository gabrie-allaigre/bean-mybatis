package com.talanlabs.bean.mybatis.it;

import com.talanlabs.bean.mybatis.data.*;
import com.talanlabs.bean.mybatis.factory.IdFactory;
import com.talanlabs.bean.mybatis.rsql.statement.Request;
import com.talanlabs.bean.mybatis.rsql.statement.RsqlStatementNameHelper;
import com.talanlabs.bean.mybatis.statement.StatementNameHelper;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;

public class UpdateIT extends AbstractHSQLIntegration {

    @Test
    public void testUpdate() {
        getDefaultNlsColumnHandler().setLanguageCode(null);
        CountryBean countryBean1 = getBeanSqlSessionManager().findById(CountryBean.class, IdFactory.IdString.from("1"));
        Assertions.assertThat(countryBean1).isNotNull().extracting(CountryBean::getCode, CountryBean::getName, CountryBean::getUpdatedBy).containsExactly("FRA", "france", null);

        countryBean1.setCode("fr");
        getBeanSqlSessionManager().update(CountryBean.class,countryBean1);

        CountryBean countryBean2 = getBeanSqlSessionManager().findById(CountryBean.class, IdFactory.IdString.from("1"));
        Assertions.assertThat(countryBean2).isNotNull().extracting(CountryBean::getCode, CountryBean::getName, CountryBean::getUpdatedBy).containsExactly("fr", "france", "unknow");
    }

    @Test
    public void testUpdateWithProperties1() {
        getDefaultNlsColumnHandler().setLanguageCode("fra");
        CountryBean countryBean1 = getBeanSqlSessionManager().findById(CountryBean.class, IdFactory.IdString.from("1"));
        Assertions.assertThat(countryBean1).isNotNull().extracting(CountryBean::getCode, CountryBean::getName, CountryBean::getUpdatedBy).containsExactly("FRA", "Fromage", null);

        countryBean1.setCode("fr");
        countryBean1.setName("FRANCE");
        getBeanSqlSessionManager().update(CountryBean.class,countryBean1,"name");

        CountryBean countryBean2 = getBeanSqlSessionManager().findById(CountryBean.class, IdFactory.IdString.from("1"));
        Assertions.assertThat(countryBean2).isNotNull().extracting(CountryBean::getCode, CountryBean::getName, CountryBean::getUpdatedBy).containsExactly("FRA", "FRANCE", null);
    }

    @Test
    public void testUpdateWithProperties2() {
        getDefaultNlsColumnHandler().setLanguageCode("fra");
        CountryBean countryBean1 = getBeanSqlSessionManager().findById(CountryBean.class, IdFactory.IdString.from("1"));
        Assertions.assertThat(countryBean1).isNotNull().extracting(CountryBean::getCode, CountryBean::getName, CountryBean::getUpdatedBy).containsExactly("FRA", "Fromage", null);

        countryBean1.setCode("fr");
        countryBean1.setName("FRANCE");
        getBeanSqlSessionManager().update(CountryBean.class,countryBean1,"code");

        CountryBean countryBean2 = getBeanSqlSessionManager().findById(CountryBean.class, IdFactory.IdString.from("1"));
        Assertions.assertThat(countryBean2).isNotNull().extracting(CountryBean::getCode, CountryBean::getName, CountryBean::getUpdatedBy).containsExactly("fr", "Fromage", null);
    }
}
