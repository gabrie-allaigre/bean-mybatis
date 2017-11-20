package com.talanlabs.bean.mybatis.it;

import com.talanlabs.bean.mybatis.data.Country2Bean;
import com.talanlabs.bean.mybatis.data.CountryBean;
import com.talanlabs.bean.mybatis.data.Test2Bean;
import com.talanlabs.bean.mybatis.data.Test3Bean;
import com.talanlabs.bean.mybatis.data.Train2Bean;
import com.talanlabs.bean.mybatis.data.Train3Bean;
import com.talanlabs.bean.mybatis.data.Train4Bean;
import com.talanlabs.bean.mybatis.data.TrainBean;
import com.talanlabs.bean.mybatis.data.WagonBean;
import com.talanlabs.bean.mybatis.factory.IdFactory;
import com.talanlabs.bean.mybatis.rsql.statement.Request;
import com.talanlabs.bean.mybatis.rsql.statement.RsqlStatementNameHelper;
import com.talanlabs.bean.mybatis.statement.StatementNameHelper;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;

public class FindIT extends AbstractHSQLIntegration {

    @Test
    public void testFindTrainById() {
        getDefaultNlsColumnHandler().setLanguageCode(null);
        List<TrainBean> trainBeans = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(TrainBean.class), "code==1*");
        Assertions.assertThat(trainBeans).hasSize(2).extracting(TrainBean::getCode).containsExactly("123456", "145678");
        Assertions.assertThat(trainBeans.get(0).getWagons()).hasSize(4);
    }

    @Test
    public void testFindTrain2ById() {
        getDefaultNlsColumnHandler().setLanguageCode(null);
        List<Train2Bean> trainBeans = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(Train2Bean.class), "code==1*");
        Assertions.assertThat(trainBeans).hasSize(2).extracting(TrainBean::getCode).containsExactly("123456", "145678");
        Assertions.assertThat(trainBeans.get(0).getWagons()).hasSize(4);
    }

    @Test
    public void testFindTrain3ById() {
        getDefaultNlsColumnHandler().setLanguageCode(null);
        List<Train3Bean> trainBeans = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(Train3Bean.class), "code==1*");
        Assertions.assertThat(trainBeans).hasSize(2).extracting(TrainBean::getCode).containsExactly("123456", "145678");
        Assertions.assertThat(trainBeans.get(0).getWagons()).hasSize(4).extracting(WagonBean::getCode).containsExactly("A", "B", "C", "D");
        Assertions.assertThat(trainBeans.get(1).getWagons()).hasSize(2).extracting(WagonBean::getCode).containsExactly("0", "1");
    }

    @Test
    public void testFindTrain4ById() {
        getDefaultNlsColumnHandler().setLanguageCode(null);
        List<Train4Bean> trainBeans = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(Train4Bean.class), "code==1*");
        Assertions.assertThat(trainBeans).hasSize(2);
        Assertions.assertThat(trainBeans.get(0).getWagons()).hasSize(4).extracting(WagonBean::getCode).containsExactly("A", "B", "C", "D");
        Assertions.assertThat(trainBeans.get(1).getWagons()).hasSize(2).extracting(WagonBean::getCode).containsExactly("0", "1");
    }

    @Test
    public void testFindTrain3ByIdSort() {
        getDefaultNlsColumnHandler().setLanguageCode(null);
        List<Train3Bean> trainBeans = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(Train3Bean.class), Request.newBuilder().rsql("code==1*").sort("-code").build());
        Assertions.assertThat(trainBeans).hasSize(2).extracting(TrainBean::getCode).containsExactly("145678", "123456");
        Assertions.assertThat(trainBeans.get(0).getWagons()).hasSize(2).extracting(WagonBean::getCode).containsExactly("0", "1");
        Assertions.assertThat(trainBeans.get(1).getWagons()).hasSize(4).extracting(WagonBean::getCode).containsExactly("A", "B", "C", "D");
    }

    @Test
    public void testFindTrain4ByIdSort() {
        getDefaultNlsColumnHandler().setLanguageCode(null);
        List<Train4Bean> trainBeans = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(Train4Bean.class), Request.newBuilder().rsql("code==1*").sort("-code").build());
        Assertions.assertThat(trainBeans).hasSize(2).extracting(TrainBean::getCode).containsExactly("145678", "123456");
        Assertions.assertThat(trainBeans.get(0).getWagons()).hasSize(2).extracting(WagonBean::getCode).containsExactly("0", "1");
        Assertions.assertThat(trainBeans.get(1).getWagons()).hasSize(4).extracting(WagonBean::getCode).containsExactly("A", "B", "C", "D");
    }

    @Test
    public void testFindTrainByIdOther() {
        getDefaultNlsColumnHandler().setLanguageCode(null);
        List<TrainBean> trainBeans = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(TrainBean.class), "startCountry.code==F*");
        Assertions.assertThat(trainBeans).hasSize(3).extracting(TrainBean::getCode).containsExactly("123456", "145678", "222222");
        Assertions.assertThat(trainBeans.get(0).getWagons()).hasSize(4);
    }

    @Test
    public void testFindTrain2ByIdOther() {
        getDefaultNlsColumnHandler().setLanguageCode(null);
        List<Train2Bean> trainBeans = getSqlSessionManager().selectList(RsqlStatementNameHelper.buildRsqlKey(Train2Bean.class), "startCountry.code==F*");
        Assertions.assertThat(trainBeans).hasSize(3).extracting(TrainBean::getCode).containsExactly("123456", "145678", "222222");
        Assertions.assertThat(trainBeans.get(0).getWagons()).hasSize(4);
    }

    @Test
    public void testFindTrain3ByIdSortRows() {
        getDefaultNlsColumnHandler().setLanguageCode(null);
        List<Train3Bean> trainBeans = getSqlSessionManager()
                .selectList(RsqlStatementNameHelper.buildRsqlKey(Train3Bean.class), Request.newBuilder().rows(1, 2).rsql("startCountry.code==F*").sort("-code").build());
        Assertions.assertThat(trainBeans).hasSize(2).extracting(TrainBean::getCode).containsExactly("145678", "123456");
        Assertions.assertThat(trainBeans.get(0).getWagons()).hasSize(2).extracting(WagonBean::getCode).containsExactly("0", "1");
    }

    @Test
    public void testFindTrain4ByIdSortRows() {
        getDefaultNlsColumnHandler().setLanguageCode(null);
        List<Train4Bean> trainBeans = getSqlSessionManager()
                .selectList(RsqlStatementNameHelper.buildRsqlKey(Train4Bean.class), Request.newBuilder().rows(1, 2).rsql("startCountry.code==F*").sort("-code").build());
        Assertions.assertThat(trainBeans).hasSize(2).extracting(TrainBean::getCode).containsExactly("145678", "123456");
        Assertions.assertThat(trainBeans.get(0).getWagons()).hasSize(2).extracting(WagonBean::getCode).containsExactly("0", "1");
    }

    @Test
    public void testFindCountryById() {
        getDefaultNlsColumnHandler().setLanguageCode("fra");
        CountryBean country = getSqlSessionManager().selectOne(StatementNameHelper.buildFindEntityByIdKey(CountryBean.class), IdFactory.IdString.from("1"));
        Assertions.assertThat(country.getName()).isEqualTo("Fromage");
    }

    @Test
    public void testFindCountry2ById() {
        getDefaultNlsColumnHandler().setLanguageCode("fra");
        Country2Bean country = getSqlSessionManager().selectOne(StatementNameHelper.buildFindEntityByIdKey(Country2Bean.class), IdFactory.IdString.from("1"));
        Assertions.assertThat(country.getName()).isEqualTo("Fromage");
    }

    @Test
    public void testFindTest2ById() {
        getDefaultNlsColumnHandler().setLanguageCode("fra");
        Test2Bean test2 = getSqlSessionManager().selectOne(StatementNameHelper.buildFindEntityByIdKey(Test2Bean.class), IdFactory.IdString.from("1"));
        Assertions.assertThat(test2.getName1()).isEqualTo("Citrouille");
        Assertions.assertThat(test2.getName2()).isEqualTo("Toto1");
        Assertions.assertThat(test2.getStartCountry().getName()).isEqualTo("Fromage");
        Assertions.assertThat(test2.getEndCountry().getName()).isEqualTo("chine");

        test2 = getSqlSessionManager().selectOne(StatementNameHelper.buildFindEntityByIdKey(Test2Bean.class), IdFactory.IdString.from("2"));
        Assertions.assertThat(test2.getName1()).isEqualTo("Gaby2");
        Assertions.assertThat(test2.getName2()).isEqualTo("Poire");
        Assertions.assertThat(test2.getStartCountry().getName()).isEqualTo("Fromage");
        Assertions.assertThat(test2.getEndCountry().getName()).isEqualTo("england");
    }

    @Test
    public void testFindTest3ById() {
        getDefaultNlsColumnHandler().setLanguageCode("fra");
        Test3Bean test3 = getSqlSessionManager().selectOne(StatementNameHelper.buildFindEntityByIdKey(Test3Bean.class), IdFactory.IdString.from("1"));
        Assertions.assertThat(test3.getName1()).isEqualTo("Citrouille");
        Assertions.assertThat(test3.getName2()).isEqualTo("Toto1");
        Assertions.assertThat(test3.getStartCountry().getName()).isEqualTo("Fromage");
        Assertions.assertThat(test3.getEndCountry().getName()).isEqualTo("chine");

        test3 = getSqlSessionManager().selectOne(StatementNameHelper.buildFindEntityByIdKey(Test3Bean.class), IdFactory.IdString.from("2"));
        Assertions.assertThat(test3.getName1()).isEqualTo("Gaby2");
        Assertions.assertThat(test3.getName2()).isEqualTo("Poire");
        Assertions.assertThat(test3.getStartCountry().getName()).isEqualTo("Fromage");
        Assertions.assertThat(test3.getEndCountry().getName()).isEqualTo("england");
    }
}
