package com.talanlabs.bean.mybatis.component.it;

import com.talanlabs.bean.mybatis.component.data.*;
import com.talanlabs.bean.mybatis.factory.IdFactory;
import com.talanlabs.bean.mybatis.statement.StatementNameHelper;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

public class FindByIdIT extends AbstractHSQLIntegration {

    @Test
    public void testFindUserById() {
        getDefaultNlsColumnHandler().setLanguageCode(null);
        IUser user = getSqlSessionManager().selectOne(StatementNameHelper.buildFindEntityByIdKey(IUser.class), IdFactory.IdString.from("1"));

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(user.getId()).isEqualTo(IdFactory.IdString.from("1"));
        softAssertions.assertThat(user.getVersion()).isEqualTo(0);
        softAssertions.assertThat(user.getCountryCode()).isEqualTo("FRA");
        softAssertions.assertThat(user.getCountryId()).isEqualTo(IdFactory.IdString.from("1"));
        softAssertions.assertThat(user.getAddressId()).isEqualTo(IdFactory.IdString.from("2"));
        softAssertions.assertThat(user.getCreatedBy()).isEqualTo("GABY");
        softAssertions.assertAll();
    }

    @Test
    public void testAssociation() {
        getDefaultNlsColumnHandler().setLanguageCode(null);
        IUser user = getSqlSessionManager().selectOne(StatementNameHelper.buildFindEntityByIdKey(IUser.class), IdFactory.IdString.from("1"));

        IAddress address = user.getAddress();

        Assertions.assertThat(address).isNotNull();
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(address.getId()).isEqualTo(IdFactory.IdString.from("2"));
        softAssertions.assertThat(address.getVersion()).isEqualTo(0);
        softAssertions.assertThat(address.getCity()).isEqualTo("Valence");
        softAssertions.assertThat(address.getCountryId()).isEqualTo(IdFactory.IdString.from("1"));
        softAssertions.assertThat(address.getPostalZip()).isEqualTo("26000");
        softAssertions.assertThat(address.getCreatedBy()).isEqualTo("GABY");
        softAssertions.assertAll();

        ICountry country = address.getCountry();
        Assertions.assertThat(country).isNotNull();
        softAssertions.assertThat(country.getId()).isEqualTo(IdFactory.IdString.from("1"));
        softAssertions.assertThat(country.getVersion()).isEqualTo(0);
        softAssertions.assertThat(country.getCode()).isEqualTo("FRA");
        softAssertions.assertThat(country.getName()).isEqualTo("france");
        softAssertions.assertThat(country.getCreatedBy()).isEqualTo("GABY");
        softAssertions.assertAll();
    }

    @Test
    public void testCollection() {
        IUser user = getSqlSessionManager().selectOne(StatementNameHelper.buildFindEntityByIdKey(IUser.class), IdFactory.IdString.from("1"));

        Assertions.assertThat(user.getGroups()).isNotNull().hasSize(2);

        Assertions.assertThat(user.getAddresses()).isNotNull().hasSize(3);
    }

    @Test
    public void testNlsColumn() {
        getDefaultNlsColumnHandler().setLanguageCode("fra");
        ICountry country = getSqlSessionManager().selectOne(StatementNameHelper.buildFindEntityByIdKey(ICountry.class), IdFactory.IdString.from("1"));

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(country.getId()).isEqualTo(IdFactory.IdString.from("1"));
        softAssertions.assertThat(country.getVersion()).isEqualTo(0);
        softAssertions.assertThat(country.getCode()).isEqualTo("FRA");
        softAssertions.assertThat(country.getName()).isEqualTo("Fromage");
        softAssertions.assertThat(country.getCreatedBy()).isEqualTo("GABY");
        softAssertions.assertAll();
    }

    @Test
    public void testFindUserComponentById() {
        getDefaultNlsColumnHandler().setLanguageCode(null);
        IUser user = getBeanSqlSessionManager().findById(IUser.class, IdFactory.IdString.from("1"));

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(user.getId()).isEqualTo(IdFactory.IdString.from("1"));
        softAssertions.assertThat(user.getVersion()).isEqualTo(0);
        softAssertions.assertThat(user.getCountryCode()).isEqualTo("FRA");
        softAssertions.assertThat(user.getCountryId()).isEqualTo(IdFactory.IdString.from("1"));
        softAssertions.assertThat(user.getAddressId()).isEqualTo(IdFactory.IdString.from("2"));
        softAssertions.assertThat(user.getCreatedBy()).isEqualTo("GABY");
        softAssertions.assertAll();
    }

    @Test
    public void testOrderBy() {
        ITrain train = getSqlSessionManager().selectOne(StatementNameHelper.buildFindEntityByIdKey(ITrain.class), IdFactory.IdString.from("1"));
        Assertions.assertThat(train.getWagons()).isNotNull().hasSize(5).extracting("position").containsExactly(5, 1, 2, 4, 3);
        Assertions.assertThat(train.getWagons()).isNotNull().hasSize(5).extracting("code").containsSequence("000000000001", "000000000002", "000000000003", "000000000004", "000000000005");

        ITrain2 train2 = getSqlSessionManager().selectOne(StatementNameHelper.buildFindEntityByIdKey(ITrain2.class), IdFactory.IdString.from("1"));
        Assertions.assertThat(train2.getWagons()).isNotNull().hasSize(5).extracting("position").containsExactly(1, 2, 3, 4, 5);
        Assertions.assertThat(train2.getWagons()).isNotNull().hasSize(5).extracting("code").containsExactly("000000000002", "000000000003", "000000000005", "000000000004", "000000000001");

        ITrain3 train3 = getSqlSessionManager().selectOne(StatementNameHelper.buildFindEntityByIdKey(ITrain3.class), IdFactory.IdString.from("1"));
        Assertions.assertThat(train3.getWagons()).isNotNull().hasSize(5).extracting("position").containsExactly(5, 4, 3, 2, 1);
        Assertions.assertThat(train3.getWagons()).isNotNull().hasSize(5).extracting("code").containsExactly("000000000001", "000000000004", "000000000005", "000000000003", "000000000002");

        ITrain4 train4 = getSqlSessionManager().selectOne(StatementNameHelper.buildFindEntityByIdKey(ITrain4.class), IdFactory.IdString.from("2"));
        Assertions.assertThat(train4.getWagons()).isNotNull().hasSize(5).extracting("position").containsExactly(1, 2, 3, 3, 3);
        Assertions.assertThat(train4.getWagons()).isNotNull().hasSize(5).extracting("code").containsExactly("000000000001", "000000000002", "000000000001", "000000000003", "000000000004");
    }

    @Test
    public void testTypeHandlerCustom() {
        getDefaultNlsColumnHandler().setLanguageCode("fra");
        ICountry2 country = getSqlSessionManager().selectOne(StatementNameHelper.buildFindEntityByIdKey(ICountry2.class), IdFactory.IdString.from("1"));

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(country.getId()).isEqualTo(IdFactory.IdString.from("1"));
        softAssertions.assertThat(country.getVersion()).isEqualTo(0);
        softAssertions.assertThat(country.getCode()).isEqualTo("test");
        softAssertions.assertThat(country.getName()).isEqualTo("Fromage");
        softAssertions.assertThat(country.getCreatedBy()).isEqualTo("GABY");
        softAssertions.assertAll();
    }

    @Test
    public void testContainers() {
        getDefaultNlsColumnHandler().setLanguageCode("fra");
        IWagon wagon = getSqlSessionManager().selectOne(StatementNameHelper.buildFindEntityByIdKey(IWagon.class), IdFactory.IdString.from("1"));
        Assertions.assertThat(wagon).isNotNull();
        Assertions.assertThat(wagon.getContainers()).isNotNull().hasSize(3);
    }

    @Test
    public void testWheels() {
        getDefaultNlsColumnHandler().setLanguageCode("fra");
        IWagon wagon = getSqlSessionManager().selectOne(StatementNameHelper.buildFindEntityByIdKey(IWagon.class), IdFactory.IdString.from("1"));
        Assertions.assertThat(wagon).isNotNull();
        Assertions.assertThat(wagon.getWheels()).isNotNull().hasSize(3);
        Assertions.assertThat(wagon.getWheels()).isNotNull().extracting("size").contains(IWagon.IWheel.Size.A, IWagon.IWheel.Size.B, IWagon.IWheel.Size.C);
    }

    @Test
    public void testParent1() {
        getDefaultNlsColumnHandler().setLanguageCode("fra");
        IParent1 parent1 = getSqlSessionManager().selectOne(StatementNameHelper.buildFindEntityByIdKey(IParent1.class), IdFactory.IdString.from("1"));
        Assertions.assertThat(parent1).isNotNull();
        Assertions.assertThat(parent1.getCountry()).isNotNull().extracting("code", "name").contains("FRA", "Fromage");
    }

    @Test
    public void testParent2() {
        getDefaultNlsColumnHandler().setLanguageCode("fra");
        IParent2 parent2 = getSqlSessionManager().selectOne(StatementNameHelper.buildFindEntityByIdKey(IParent2.class), IdFactory.IdString.from("1"));
        Assertions.assertThat(parent2).isNotNull();
        Assertions.assertThat(parent2.getCountry1()).isNotNull().extracting("code", "name").contains("FRA", "Fromage");
        Assertions.assertThat(parent2.getCountry2()).isNotNull().extracting("code", "name").contains("CHI", "chine");
    }

    @Test
    public void testParent3() {
        getDefaultNlsColumnHandler().setLanguageCode("fra");
        IParent3 parent3 = getSqlSessionManager().selectOne(StatementNameHelper.buildFindEntityByIdKey(IParent3.class), IdFactory.IdString.from("1"));
        Assertions.assertThat(parent3).isNotNull();
        Assertions.assertThat(parent3.getCountry()).isNotNull().extracting("code", "name").contains("FRA", "Fromage");
    }

    @Test
    public void testParent4() {
        getDefaultNlsColumnHandler().setLanguageCode("fra");
        IParent4 parent4 = getSqlSessionManager().selectOne(StatementNameHelper.buildFindEntityByIdKey(IParent4.class), IdFactory.IdString.from("1"));
        Assertions.assertThat(parent4).isNotNull();
        Assertions.assertThat(parent4.getParent1().getCountry()).isNotNull().extracting("code", "name").contains("FRA", "Fromage");
        Assertions.assertThat(parent4.getParent2().getCountry1()).isNotNull().extracting("code", "name").contains("FRA", "Fromage");
        Assertions.assertThat(parent4.getParent2().getCountry2()).isNotNull().extracting("code", "name").contains("CHI", "chine");
        Assertions.assertThat(parent4.getParent3().getCountry()).isNotNull().extracting("code", "name").contains("CHI", "chine");
        Assertions.assertThat(parent4.getParent3bis().getCountry()).isNotNull().extracting("code", "name").contains("CHI", "chine");
        Assertions.assertThat(parent4.getParent3ter().getCountry()).isNotNull().extracting("code", "name").contains("CHI", "chine");
    }

    @Test
    public void testTrainBis() {
        getDefaultNlsColumnHandler().setLanguageCode("fra");
        ITrainBis train = getSqlSessionManager().selectOne(StatementNameHelper.buildFindEntityByIdKey(ITrainBis.class), IdFactory.IdString.from("1"));
        Assertions.assertThat(train.getWagons()).isNotNull().hasSize(5).extracting(IWagonBis::getPosition).containsExactly(5, 1, 2, 4, 3);
        Assertions.assertThat(train.getWagons()).isNotNull().hasSize(5).extracting(IWagonBis::getCode).containsSequence("000000000001", "000000000002", "000000000003", "000000000004", "000000000005");
        Assertions.assertThat(train.getWagons()).isNotNull().extracting(IWagonBis::getContainers).first().asList().extracting("code").containsExactly("AAAAA", "BBBBB", "CCCCC");
    }

    @Test
    public void testTrainBis2() {
        getDefaultNlsColumnHandler().setLanguageCode("fra");
        ITrainBis2 train2 = getSqlSessionManager().selectOne(StatementNameHelper.buildFindEntityByIdKey(ITrainBis2.class), IdFactory.IdString.from("1"));
        Assertions.assertThat(train2.getWagons()).isNotNull().hasSize(5).extracting(IWagonBis2::getPosition).containsExactly(1, 2, 3, 4, 5);
        Assertions.assertThat(train2.getWagons()).isNotNull().hasSize(5).extracting(IWagonBis2::getCode).containsExactly("000000000002", "000000000003", "000000000005", "000000000004", "000000000001");
        Assertions.assertThat(train2.getWagons()).isNotNull().extracting(IWagonBis2::getContainers).last().asList().extracting("code").containsExactly("CCCCC", "BBBBB", "AAAAA");
    }
}