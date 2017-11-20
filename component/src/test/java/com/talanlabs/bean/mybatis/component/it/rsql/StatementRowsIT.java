package com.talanlabs.bean.mybatis.component.it.rsql;

import com.talanlabs.bean.mybatis.component.data.ICountry;
import com.talanlabs.bean.mybatis.component.it.AbstractHSQLIntegration;
import com.talanlabs.bean.mybatis.rsql.configuration.RsqlConfigurationBuilder;
import com.talanlabs.bean.mybatis.rsql.database.HSQLDBHandler;
import com.talanlabs.bean.mybatis.rsql.engine.policy.AlwaysUpperStringPolicy;
import com.talanlabs.bean.mybatis.rsql.statement.Request;
import com.talanlabs.bean.mybatis.rsql.statement.RsqlStatementNameHelper;
import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class StatementRowsIT extends AbstractHSQLIntegration {

    @BeforeClass
    public static void init() {
        getBeanConfiguration().setRsqlConfiguration(
                RsqlConfigurationBuilder.newBuilder(getBeanConfiguration()).stringPolicy(new AlwaysUpperStringPolicy()).pageStatementFactory(new HSQLDBHandler(getBeanConfiguration())).build());
    }

    @Test
    public void testSimpleRows1() {
        List<ICountry> countries = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(ICountry.class), Request.newBuilder().rows(0, 2).build());
        Assertions.assertThat(countries).isNotNull().hasSize(2);
    }

    @Test
    public void testSimpleRows2() {
        List<ICountry> countries = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(ICountry.class), Request.newBuilder().sort("code").rows(0, 3).build());
        Assertions.assertThat(countries).isNotNull().hasSize(3).extracting("code").containsExactly("CHI", "ENG", "ESP");
    }

    @Test
    public void testSimpleRows3() {
        List<ICountry> countries = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(ICountry.class), Request.newBuilder().sort("code").rows(1, 3).build());
        Assertions.assertThat(countries).isNotNull().hasSize(3).extracting("code").containsExactly("ENG", "ESP", "F%");
    }

    @Test
    public void testSimpleRows4() {
        List<ICountry> countries = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(ICountry.class), Request.newBuilder().sort("-code").rows(1, 3).build());
        Assertions.assertThat(countries).isNotNull().hasSize(3).extracting("code").containsExactly("ITA", "FRA", "F%");
    }
}
