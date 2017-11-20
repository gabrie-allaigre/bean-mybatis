package com.talanlabs.bean.mybatis.component.it;

import com.talanlabs.bean.mybatis.component.data.CountryBuilder;
import com.talanlabs.bean.mybatis.component.data.ICountry;
import com.talanlabs.bean.mybatis.component.data.IUser;
import com.talanlabs.bean.mybatis.factory.IdFactory;
import com.talanlabs.bean.mybatis.statement.StatementNameHelper;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class UpdateIT extends AbstractHSQLIntegration {

    @Test
    public void testUpdate() {
        IUser user = getSqlSessionManager().selectOne(StatementNameHelper.buildFindEntityByIdKey(IUser.class), IdFactory.IdString.from("2"));
        user.setLogin("Toto");
        int i = getSqlSessionManager().update(StatementNameHelper.buildUpdateKey(IUser.class,null,null), user);

        Assertions.assertThat(i).isEqualTo(1);
        Assertions.assertThat(user.getVersion()).isEqualTo(1);

        user = getSqlSessionManager().selectOne(StatementNameHelper.buildFindEntityByIdKey(IUser.class), IdFactory.IdString.from("2"));
        Assertions.assertThat(user.getLogin()).isEqualTo("Toto");
    }

    @Test
    public void testUpdateComponent() {
        IUser user = getBeanSqlSessionManager().findById(IUser.class, IdFactory.IdString.from("1"));
        user.setLogin("Toto");
        int i = getBeanSqlSessionManager().update(IUser.class, user);

        getSqlSessionManager().commit();

        Assertions.assertThat(i).isEqualTo(1);
        Assertions.assertThat(user.getVersion()).isEqualTo(1);

        user = getBeanSqlSessionManager().findById(IUser.class, IdFactory.IdString.from("1"));
        Assertions.assertThat(user.getLogin()).isEqualTo("Toto");
    }

    @Test
    public void testUpdateNlsColumn() {
        ICountry country = getBeanSqlSessionManager().findById(ICountry.class, IdFactory.IdString.from("1"));
        country.setName("Test");
        int i = getBeanSqlSessionManager().update(ICountry.class, country);

        Assertions.assertThat(i).isEqualTo(1);
        Assertions.assertThat(country.getId()).isNotNull();
        Assertions.assertThat(country.getVersion()).isEqualTo(1);

        ICountry country2 = getBeanSqlSessionManager().findById(ICountry.class, country.getId());
        Assertions.assertThat(country2.getId()).isEqualTo(country.getId());
        Assertions.assertThat(country2.getVersion()).isEqualTo(country.getVersion());
        Assertions.assertThat(country2.getCode()).isEqualTo(country.getCode());
        Assertions.assertThat(country2.getName()).isEqualTo(country.getName());
    }

    @Test
    public void testUpdateNlsColumn2() {
        getDefaultNlsColumnHandler().setLanguageCode("fra");

        ICountry country = CountryBuilder.newBuilder().code("FRA").name("France").build();
        int i = getBeanSqlSessionManager().insert(ICountry.class, country);

        Assertions.assertThat(i).isEqualTo(1);
        Assertions.assertThat(country.getId()).isNotNull();

        ICountry country1 = getBeanSqlSessionManager().findById(ICountry.class, country.getId());
        country1.setName("Test");
        i = getBeanSqlSessionManager().update(ICountry.class, country1);

        Assertions.assertThat(i).isEqualTo(1);
        Assertions.assertThat(getBeanSqlSessionManager().findById(ICountry.class, country.getId()).getName()).isEqualTo("Test");

        getSqlSessionManager().commit();
        getSqlSessionManager().clearCache();

        getDefaultNlsColumnHandler().setLanguageCode("eng");

        ICountry country2 = getBeanSqlSessionManager().findById(ICountry.class, country.getId());
        Assertions.assertThat(country2.getName()).isEqualTo("France");
        country2.setName("French");
        i = getBeanSqlSessionManager().update(ICountry.class, country2);

        Assertions.assertThat(i).isEqualTo(1);
        Assertions.assertThat(getBeanSqlSessionManager().findById(ICountry.class, country.getId()).getName()).isEqualTo("French");

        getSqlSessionManager().commit();
        getSqlSessionManager().clearCache();

        getDefaultNlsColumnHandler().setLanguageCode("fra");

        Assertions.assertThat(getBeanSqlSessionManager().findById(ICountry.class, country.getId()).getName()).isEqualTo("Test");

        getSqlSessionManager().commit();
        getSqlSessionManager().clearCache();

        getDefaultNlsColumnHandler().setLanguageCode("spa");

        Assertions.assertThat(getBeanSqlSessionManager().findById(ICountry.class, country.getId()).getName()).isEqualTo("French");

    }
}
