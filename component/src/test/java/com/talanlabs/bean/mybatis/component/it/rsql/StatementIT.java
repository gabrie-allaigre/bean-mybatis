package com.talanlabs.bean.mybatis.component.it.rsql;

import com.talanlabs.bean.mybatis.component.data.CountryFields;
import com.talanlabs.bean.mybatis.component.data.IAddress;
import com.talanlabs.bean.mybatis.component.data.ICountry;
import com.talanlabs.bean.mybatis.component.data.IPerson;
import com.talanlabs.bean.mybatis.component.it.AbstractHSQLIntegration;
import com.talanlabs.bean.mybatis.rsql.statement.RsqlStatementNameHelper;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;

public class StatementIT extends AbstractHSQLIntegration {

    @Test
    public void testSimple() {
        List<ICountry> countries = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(ICountry.class));
        Assertions.assertThat(countries).isNotNull().hasSize(7);
    }

    @Test
    public void testSimpleFindCode() {
        List<ICountry> countries = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(ICountry.class), "code==FRA");
        Assertions.assertThat(countries).isNotNull().hasSize(1).extracting(CountryFields.code).containsExactly("FRA");
    }

    @Test
    public void testSimpleNotFindCode() {
        List<ICountry> countries = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(ICountry.class), "code==TOTO");
        Assertions.assertThat(countries).isNotNull().hasSize(0);
    }

    @Test
    public void testSimpleLikeFindCode() {
        List<ICountry> countries = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(ICountry.class), "code==F*");
        Assertions.assertThat(countries).isNotNull().hasSize(2);
    }

    @Test
    public void testComplexeLikeFindCode1() {
        List<IAddress> addresses = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(IAddress.class), "postalZip==78*");
        Assertions.assertThat(addresses).isNotNull().hasSize(2);
    }

    @Test
    public void testComplexeLikeFindCode2() {
        List<IAddress> addresses = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(IAddress.class), "postalZip==78*;city==Versailles");
        Assertions.assertThat(addresses).isNotNull().hasSize(1);
    }

    @Test
    public void testComplexeLikeFindCode3() {
        List<IAddress> addresses = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(IAddress.class), "postalZip==78*,city==Valence");
        Assertions.assertThat(addresses).isNotNull().hasSize(3);
    }

    @Test
    public void testSimpleGreater() {
        List<IPerson> persons = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(IPerson.class), "age>30");
        Assertions.assertThat(persons).isNotNull().hasSize(3);
    }

    @Test
    public void testSimpleLower1() {
        List<IPerson> persons = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(IPerson.class), "age<4");
        Assertions.assertThat(persons).isNotNull().hasSize(1);
    }

    @Test
    public void testSimpleLower2() {
        List<IPerson> persons = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(IPerson.class), "age<=4");
        Assertions.assertThat(persons).isNotNull().hasSize(2);
    }

    @Test
    public void testSimpleGreaterLower() {
        List<IPerson> persons = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(IPerson.class), "age>=36;age<37");
        Assertions.assertThat(persons).isNotNull().hasSize(1);
    }

    @Test
    public void testSimpleLower3() {
        List<IPerson> persons = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(IPerson.class), "birthday>=2000-01-01");
        Assertions.assertThat(persons).isNotNull().hasSize(2);
    }

    @Test
    public void testSimpleIn() {
        List<IPerson> persons = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(IPerson.class), "firstName=in=(Gabriel,Sandra)");
        Assertions.assertThat(persons).isNotNull().hasSize(2);
    }

    @Test
    public void testSimpleOut() {
        List<IPerson> persons = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(IPerson.class), "firstName=out=(Gabriel,Sandra,Laureline)");
        Assertions.assertThat(persons).isNotNull().hasSize(2);
    }

    @Test
    public void testJoinSimple() {
        List<IPerson> persons = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(IPerson.class), "address.city==Versailles");
        Assertions.assertThat(persons).isNotNull().hasSize(2);
    }

    @Test
    public void testJoinSimple2() {
        List<IPerson> persons = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(IPerson.class), "address.city==Versailles,address.city==Valence");
        Assertions.assertThat(persons).isNotNull().hasSize(3);
    }

    @Test
    public void testJoinDouble() {
        List<IPerson> persons = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(IPerson.class), "addressBis.city==Versailles");
        Assertions.assertThat(persons).isNotNull().hasSize(0);
    }

    @Test
    public void testJoinMultiple() {
        List<IPerson> persons = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(IPerson.class), "address2.city==Versailles");
        Assertions.assertThat(persons).isNotNull().hasSize(0);
    }

    @Test
    public void testNlsSimple1() {
        getDefaultNlsColumnHandler().setLanguageCode("fra");
        List<ICountry> countries = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(ICountry.class), "name==Fromage");
        Assertions.assertThat(countries).isNotNull().hasSize(1);
    }

    @Test
    public void testNlsSimple2() {
        getDefaultNlsColumnHandler().setLanguageCode("fra");
        List<ICountry> countries = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(ICountry.class), "name==chine");
        Assertions.assertThat(countries).isNotNull().hasSize(1);
    }

    @Test
    public void testNlsSimple3() {
        getDefaultNlsColumnHandler().setLanguageCode("fra");
        List<ICountry> countries = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(ICountry.class), "name==e*,name==U*");
        Assertions.assertThat(countries).isNotNull().hasSize(3);
    }

    @Test
    public void testNlsSimple4() {
        getDefaultNlsColumnHandler().setLanguageCode("fra");
        List<ICountry> countries = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(ICountry.class), "name==Fromage,name==chine");
        Assertions.assertThat(countries).isNotNull().hasSize(2);
    }
}
