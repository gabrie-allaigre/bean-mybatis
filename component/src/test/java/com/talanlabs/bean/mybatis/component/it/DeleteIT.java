package com.talanlabs.bean.mybatis.component.it;

import com.talanlabs.bean.mybatis.component.data.ICountry;
import com.talanlabs.bean.mybatis.component.data.IUser;
import com.talanlabs.bean.mybatis.factory.IdFactory;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class DeleteIT extends AbstractHSQLIntegration {

    @Test
    public void testDelete() {
        IUser user = getBeanSqlSessionManager().findById(IUser.class, IdFactory.IdString.from("1"));
        int i = getBeanSqlSessionManager().delete(IUser.class, user);

        Assertions.assertThat(i).isEqualTo(1);

        user = getBeanSqlSessionManager().findById(IUser.class, IdFactory.IdString.from("1"));
        Assertions.assertThat(user).isNull();
    }

    @Test
    public void testDeleteNls() {
        ICountry country = getBeanSqlSessionManager().findById(ICountry.class, IdFactory.IdString.from("1"));
        int i = getBeanSqlSessionManager().delete(ICountry.class, country);

        Assertions.assertThat(i).isEqualTo(1);

        country = getBeanSqlSessionManager().findById(ICountry.class, IdFactory.IdString.from("1"));
        Assertions.assertThat(country).isNull();

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tableName", "T_COUNTRY");
        parameter.put("id", IdFactory.IdString.from("1"));
        Assertions.assertThat(getSqlSessionManager().<Integer>selectOne("com.talanlabs.bean.mybatis.component.it.mapper.NlsMapper.countNlsColumns", parameter)).isEqualTo(0);
    }
}
