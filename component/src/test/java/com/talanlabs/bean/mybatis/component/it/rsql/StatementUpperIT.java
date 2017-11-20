package com.talanlabs.bean.mybatis.component.it.rsql;

import com.talanlabs.bean.mybatis.component.data.CountryFields;
import com.talanlabs.bean.mybatis.component.data.ICountry;
import com.talanlabs.bean.mybatis.component.it.AbstractHSQLIntegration;
import com.talanlabs.bean.mybatis.rsql.configuration.RsqlConfigurationBuilder;
import com.talanlabs.bean.mybatis.rsql.database.HSQLDBHandler;
import com.talanlabs.bean.mybatis.rsql.engine.policy.AlwaysUpperStringPolicy;
import com.talanlabs.bean.mybatis.rsql.statement.RsqlStatementNameHelper;
import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class StatementUpperIT extends AbstractHSQLIntegration {

    @BeforeClass
    public static void init() {
        getBeanConfiguration().setRsqlConfiguration(
                RsqlConfigurationBuilder.newBuilder(getBeanConfiguration()).stringPolicy(new AlwaysUpperStringPolicy()).pageStatementFactory(new HSQLDBHandler(getBeanConfiguration())).build());
    }

    @Test
    public void testSimpleFindCode() {
        getDefaultNlsColumnHandler().setLanguageCode(null);
        List<ICountry> countries = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(ICountry.class), "code==fra");
        Assertions.assertThat(countries).isNotNull().hasSize(1).extracting(CountryFields.code).containsExactly("FRA");
    }

    @Test
    public void testNlsFindCode() {
        getDefaultNlsColumnHandler().setLanguageCode("fra");
        List<ICountry> countries = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(ICountry.class), "name==FROMAGE");
        Assertions.assertThat(countries).isNotNull().hasSize(1);
    }
}
