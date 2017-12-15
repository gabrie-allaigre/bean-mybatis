package com.talanlabs.bean.mybatis.component.it.rsql;

import com.talanlabs.bean.mybatis.component.data.CountryFields;
import com.talanlabs.bean.mybatis.component.data.ICountry;
import com.talanlabs.bean.mybatis.component.data.IPerson;
import com.talanlabs.bean.mybatis.component.data.PersonFields;
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

public class StatementSortIT extends AbstractHSQLIntegration {

    @BeforeClass
    public static void init() {
        getBeanConfiguration().setRsqlConfiguration(
                RsqlConfigurationBuilder.newBuilder(getBeanConfiguration()).stringPolicy(new AlwaysUpperStringPolicy()).pageStatementFactory(new HSQLDBHandler(getBeanConfiguration())).build());
    }

    @Test
    public void testSortCode() {
        List<ICountry> countries = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(ICountry.class), Request.newBuilder().sort("code").build());
        Assertions.assertThat(countries).isNotNull().hasSize(7).extracting(CountryFields.code).containsSequence("CHI", "ENG", "ESP", "F%", "FRA", "ITA", "USA");
    }

    @Test
    public void testSortAscCode() {
        List<ICountry> countries = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(ICountry.class), Request.newBuilder().sort("+code").build());
        Assertions.assertThat(countries).isNotNull().hasSize(7).extracting(CountryFields.code).containsSequence("CHI", "ENG", "ESP", "F%", "FRA", "ITA", "USA");
    }

    @Test
    public void testSortDescCode() {
        List<ICountry> countries = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(ICountry.class), Request.newBuilder().sort("-code").build());
        Assertions.assertThat(countries).isNotNull().hasSize(7).extracting(CountryFields.code).containsSequence("USA", "ITA", "FRA", "F%", "ESP", "ENG", "CHI");
    }

    @Test
    public void testSortName() {
        List<ICountry> countries = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(ICountry.class), Request.newBuilder().sort("name").build());
        Assertions.assertThat(countries).isNotNull().hasSize(7).extracting(CountryFields.code).containsSequence("CHI", "ENG", "ESP", "F%", "FRA", "ITA", "USA");
    }

    @Test
    public void testComplexeSort1() {
        List<IPerson> persons = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(IPerson.class), Request.newBuilder().sort("address.city").build());
        Assertions.assertThat(persons).isNotNull().hasSize(5).extracting(IPerson::getAddress).extracting(input -> input != null ? input.getCity() : null)
                .containsSequence(null, "London", "Valence", "Versailles", "Versailles");
    }

    @Test
    public void testComplexeSort5() {
        List<IPerson> persons = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(IPerson.class), Request.newBuilder().sort("+address.city").build());
        Assertions.assertThat(persons).isNotNull().hasSize(5).extracting(IPerson::getAddress).extracting(input -> input != null ? input.getCity() : null)
                .containsSequence(null, "London", "Valence", "Versailles", "Versailles");
    }

    @Test
    public void testComplexeSort6() {
        List<IPerson> persons = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(IPerson.class), Request.newBuilder().sort("++address.city").build());
        Assertions.assertThat(persons).isNotNull().hasSize(5).extracting(IPerson::getAddress).extracting(input -> input != null ? input.getCity() : null)
                .containsSequence(null, "London", "Valence", "Versailles", "Versailles");
    }

    @Test
    public void testComplexeSort2() {
        List<IPerson> persons = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(IPerson.class), Request.newBuilder().sort("-address.city").build());
        Assertions.assertThat(persons).isNotNull().hasSize(5).extracting(IPerson::getAddress).extracting(input -> input != null ? input.getCity() : null)
                .containsSequence(null, "Versailles", "Versailles", "Valence", "London");
    }

    @Test
    public void testComplexeSort7() {
        List<IPerson> persons = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(IPerson.class), Request.newBuilder().sort("-+address.city").build());
        Assertions.assertThat(persons).isNotNull().hasSize(5).extracting(IPerson::getAddress).extracting(input -> input != null ? input.getCity() : null)
                .containsSequence(null, "Versailles", "Versailles", "Valence", "London");
    }

    @Test
    public void testComplexeSort8() {
        List<IPerson> persons = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(IPerson.class), Request.newBuilder().sort("--address.city").build());
        Assertions.assertThat(persons).isNotNull().hasSize(5).extracting(IPerson::getAddress).extracting(input -> input != null ? input.getCity() : null)
                .containsSequence("Versailles", "Versailles", "Valence", "London", null);
    }

    @Test
    public void testComplexeSort3() {
        List<IPerson> persons = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(IPerson.class), Request.newBuilder().sort("-address.city,-firstName").build());
        Assertions.assertThat(persons).isNotNull().hasSize(5).extracting(PersonFields.firstName).containsSequence("David", "Laureline", "Gabriel", "Sandra", "Raphael");
        Assertions.assertThat(persons).isNotNull().hasSize(5).extracting(IPerson::getAddress).extracting(input -> input != null ? input.getCity() : null)
                .containsSequence(null, "Versailles", "Versailles", "Valence", "London");
    }

    @Test
    public void testComplexeSort4() {
        List<IPerson> persons = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(IPerson.class), Request.newBuilder().sort("-address.city,+firstName").build());
        Assertions.assertThat(persons).isNotNull().hasSize(5).extracting(PersonFields.firstName).containsSequence("David", "Gabriel", "Laureline", "Sandra", "Raphael");
        Assertions.assertThat(persons).isNotNull().hasSize(5).extracting(IPerson::getAddress).extracting(input -> input != null ? input.getCity() : null)
                .containsSequence(null, "Versailles", "Versailles", "Valence", "London");
    }
}
