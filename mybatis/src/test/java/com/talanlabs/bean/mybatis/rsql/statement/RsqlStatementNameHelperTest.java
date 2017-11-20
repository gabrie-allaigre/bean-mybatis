package com.talanlabs.bean.mybatis.rsql.statement;

import com.talanlabs.bean.mybatis.data.CountryBean;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

public class RsqlStatementNameHelperTest {

    @Test
    public void testBuildRsqlKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(RsqlStatementNameHelper.buildRsqlKey(null)).isNull();
        softAssertions.assertThat(RsqlStatementNameHelper.buildRsqlKey(CountryBean.class)).isEqualTo("com.talanlabs.bean.mybatis.data.CountryBean/rsql");
        softAssertions.assertAll();
    }

    @Test
    public void testIsRsqlKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(RsqlStatementNameHelper.isRsqlKey(null)).isFalse();
        softAssertions.assertThat(RsqlStatementNameHelper.isRsqlKey("com.model.IUser/rsql")).isTrue();
        softAssertions.assertThat(RsqlStatementNameHelper.isRsqlKey("com.model.IUser1/rsql")).isTrue();
        softAssertions.assertThat(RsqlStatementNameHelper.isRsqlKey("model.IUser/rsql")).isTrue();
        softAssertions.assertThat(RsqlStatementNameHelper.isRsqlKey("com.talanlabs.mybatis.rsql.test.data.IUser/rsql")).isTrue();
        softAssertions.assertThat(RsqlStatementNameHelper.isRsqlKey("model.IUser")).isFalse();
        softAssertions.assertThat(RsqlStatementNameHelper.isRsqlKey("/rsql")).isFalse();
        softAssertions.assertThat(RsqlStatementNameHelper.isRsqlKey("model-IUser/rsql")).isFalse();
        softAssertions.assertAll();
    }

    @Test
    public void testExtractBeanNameInRsqlKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(RsqlStatementNameHelper.extractBeanClassInRsqlKey(null)).isNull();
        softAssertions.assertThat(RsqlStatementNameHelper.extractBeanClassInRsqlKey("com.talanlabs.bean.mybatis.data.CountryBean/rsql")).isEqualTo(CountryBean.class);
        softAssertions.assertThat(RsqlStatementNameHelper.extractBeanClassInRsqlKey("model.IUser/rsql")).isNull();
        softAssertions.assertAll();
    }
}
