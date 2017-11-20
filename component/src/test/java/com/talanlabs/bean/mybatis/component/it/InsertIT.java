package com.talanlabs.bean.mybatis.component.it;

import com.talanlabs.bean.mybatis.component.data.CountryBuilder;
import com.talanlabs.bean.mybatis.component.data.ICountry;
import com.talanlabs.bean.mybatis.component.data.IUser;
import com.talanlabs.bean.mybatis.component.data.UserBuilder;
import com.talanlabs.bean.mybatis.statement.StatementNameHelper;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class InsertIT extends AbstractHSQLIntegration {

    @Test
    public void testInsert() {
        IUser user = UserBuilder.newBuilder().login("Gabriel").build();
        int i = getSqlSessionManager().insert(StatementNameHelper.buildInsertKey(IUser.class), user);

        Assertions.assertThat(i).isEqualTo(1);
        Assertions.assertThat(user.getId()).isNotNull();
        Assertions.assertThat(user.getVersion()).isEqualTo(0);
    }

    @Test
    public void testInsertComponent() {
        IUser user = UserBuilder.newBuilder().login("Gabriel").build();
        int i = getBeanSqlSessionManager().insert(IUser.class, user);

        Assertions.assertThat(i).isEqualTo(1);
        Assertions.assertThat(user.getId()).isNotNull();
        Assertions.assertThat(user.getVersion()).isEqualTo(0);
    }

    @Test
    public void testInsertNlsColumn() {
        ICountry country = CountryBuilder.newBuilder().code("FRA").name("France").build();
        int i = getBeanSqlSessionManager().insert(ICountry.class, country);

        Assertions.assertThat(i).isEqualTo(1);
        Assertions.assertThat(country.getId()).isNotNull();
        Assertions.assertThat(country.getVersion()).isEqualTo(0);

        ICountry country2 = getBeanSqlSessionManager().findById(ICountry.class, country.getId());
        Assertions.assertThat(country2.getId()).isEqualTo(country.getId());
        Assertions.assertThat(country2.getVersion()).isEqualTo(country.getVersion());
        Assertions.assertThat(country2.getCode()).isEqualTo(country.getCode());
        Assertions.assertThat(country2.getName()).isEqualTo(country.getName());
    }
}
