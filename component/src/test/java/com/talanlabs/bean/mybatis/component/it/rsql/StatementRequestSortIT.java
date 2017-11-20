package com.talanlabs.bean.mybatis.component.it.rsql;

import com.talanlabs.bean.mybatis.component.data.*;
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

public class StatementRequestSortIT extends AbstractHSQLIntegration {

    @BeforeClass
    public static void init() {
        getBeanConfiguration().setRsqlConfiguration(RsqlConfigurationBuilder.newBuilder(getBeanConfiguration()).stringPolicy(new AlwaysUpperStringPolicy())
                .pageStatementFactory(new HSQLDBHandler(getBeanConfiguration())).build());
    }
    
    @Test
    public void testRequestSortCode1() {
        List<ICountry> countries = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(ICountry.class), Request.newBuilder().rsql("code==E*").sort("-code").build());
        Assertions.assertThat(countries).isNotNull().hasSize(2).extracting(CountryFields.code).containsSequence("ESP", "ENG");
    }

    @Test
    public void testRequestSortCode2() {
        List<ICountry> countries = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(ICountry.class), Request.newBuilder().rsql("code==E*").sort("+code").build());
        Assertions.assertThat(countries).isNotNull().hasSize(2).extracting(CountryFields.code).containsSequence("ENG", "ESP");
    }

    @Test
    public void testRequestSortCode3() {
        List<ICountry> countries = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(ICountry.class), Request.newBuilder().rsql("code==*S*").sort("-name").build());
        Assertions.assertThat(countries).isNotNull().hasSize(2).extracting(CountryFields.code).containsSequence("USA", "ESP");
    }

    @Test
    public void testComplexeRequestSort1() {
        List<IPerson> persons = getSqlSessionManager()
                .selectList(RsqlStatementNameHelper.buildRsqlKey(IPerson.class), Request.newBuilder().rsql("firstName==Gabriel,firstName==Ra*").sort("address.city").build());
        Assertions.assertThat(persons).isNotNull().hasSize(2).extracting(PersonFields.address).extracting(AddressFields.city).containsSequence("London", "Versailles");
    }

    @Test
    public void testComplexeSort2() {
        List<IPerson> persons = getSqlSessionManager()
                .selectList(RsqlStatementNameHelper.buildRsqlKey(IPerson.class), Request.newBuilder().rsql("firstName==Gabriel,firstName==Ra*").sort("-address.city").build());
        Assertions.assertThat(persons).isNotNull().hasSize(2).extracting(PersonFields.address).extracting(AddressFields.city).containsSequence("Versailles", "London");
    }
}
