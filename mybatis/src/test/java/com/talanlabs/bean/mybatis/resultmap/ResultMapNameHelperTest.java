package com.talanlabs.bean.mybatis.resultmap;

import com.talanlabs.bean.mybatis.data.WagonBean;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

public class ResultMapNameHelperTest {

    @Test
    public void testBuildResultMapKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(ResultMapNameHelper.buildResultMapKey(null)).isNull();
        softAssertions.assertThat(ResultMapNameHelper.buildResultMapKey(WagonBean.class)).isEqualTo("com.talanlabs.bean.mybatis.data.WagonBean/resultMap");
        softAssertions.assertThat(ResultMapNameHelper.buildResultMapKey(WagonBean.WheelBean.class)).isEqualTo("com.talanlabs.bean.mybatis.data.WagonBean$WheelBean/resultMap");
        softAssertions.assertAll();
    }

    @Test
    public void testIsResultMapKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(ResultMapNameHelper.isResultMapKey(null)).isFalse();
        softAssertions.assertThat(ResultMapNameHelper.isResultMapKey("com.model.WagonBean/resultMap")).isTrue();
        softAssertions.assertThat(ResultMapNameHelper.isResultMapKey("com.model.WagonBean1/resultMap")).isTrue();
        softAssertions.assertThat(ResultMapNameHelper.isResultMapKey("model.WagonBean/resultMap")).isTrue();
        softAssertions.assertThat(ResultMapNameHelper.isResultMapKey("com.talanlabs.bean.mybatis.data.WagonBean/resultMap")).isTrue();
        softAssertions.assertThat(ResultMapNameHelper.isResultMapKey("com.talanlabs.bean.mybatis.data.WagonBean$WheelBean/resultMap")).isTrue();
        softAssertions.assertThat(ResultMapNameHelper.isResultMapKey("model.WagonBean")).isFalse();
        softAssertions.assertThat(ResultMapNameHelper.isResultMapKey("/resultMap")).isFalse();
        softAssertions.assertThat(ResultMapNameHelper.isResultMapKey("model-WagonBean/resultMap")).isFalse();
        softAssertions.assertAll();
    }

    @Test
    public void testExtractBeanNameInResultMapKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(ResultMapNameHelper.extractBeanClassInResultMapKey(null)).isNull();
        softAssertions.assertThat(ResultMapNameHelper.extractBeanClassInResultMapKey("com.talanlabs.bean.mybatis.data.WagonBean/resultMap")).isEqualTo(WagonBean.class);
        softAssertions.assertThat(ResultMapNameHelper.extractBeanClassInResultMapKey("com.talanlabs.bean.mybatis.data.WagonBean$WheelBean/resultMap")).isEqualTo(WagonBean.WheelBean.class);
        softAssertions.assertThat(ResultMapNameHelper.extractBeanClassInResultMapKey("com.model.WagonBean/resultMap")).isNull();
        softAssertions.assertAll();
    }


    @Test
    public void testBuildNestedResultMapKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(ResultMapNameHelper.buildNestedResultMapKey(null)).isNull();
        softAssertions.assertThat(ResultMapNameHelper.buildNestedResultMapKey(WagonBean.class)).isEqualTo("com.talanlabs.bean.mybatis.data.WagonBean/nestedResultMap");
        softAssertions.assertThat(ResultMapNameHelper.buildNestedResultMapKey(WagonBean.WheelBean.class)).isEqualTo("com.talanlabs.bean.mybatis.data.WagonBean$WheelBean/nestedResultMap");
        softAssertions.assertAll();
    }

    @Test
    public void testIsNestedResultMapKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(ResultMapNameHelper.isNestedResultMapKey(null)).isFalse();
        softAssertions.assertThat(ResultMapNameHelper.isNestedResultMapKey("com.model.WagonBean/nestedResultMap")).isTrue();
        softAssertions.assertThat(ResultMapNameHelper.isNestedResultMapKey("com.model.WagonBean1/nestedResultMap")).isTrue();
        softAssertions.assertThat(ResultMapNameHelper.isNestedResultMapKey("model.WagonBean/nestedResultMap")).isTrue();
        softAssertions.assertThat(ResultMapNameHelper.isNestedResultMapKey("com.talanlabs.bean.mybatis.data.WagonBean/nestedResultMap")).isTrue();
        softAssertions.assertThat(ResultMapNameHelper.isNestedResultMapKey("com.talanlabs.bean.mybatis.data.WagonBean$WheelBean/nestedResultMap")).isTrue();
        softAssertions.assertThat(ResultMapNameHelper.isNestedResultMapKey("model.WagonBean")).isFalse();
        softAssertions.assertThat(ResultMapNameHelper.isNestedResultMapKey("/nestedResultMap")).isFalse();
        softAssertions.assertThat(ResultMapNameHelper.isNestedResultMapKey("model-WagonBean/nestedResultMap")).isFalse();
        softAssertions.assertAll();
    }

    @Test
    public void testExtractBeanNameInNestedResultMapKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(ResultMapNameHelper.extractBeanClassInNestedResultMapKey(null)).isNull();
        softAssertions.assertThat(ResultMapNameHelper.extractBeanClassInNestedResultMapKey("com.talanlabs.bean.mybatis.data.WagonBean/nestedResultMap")).isEqualTo(WagonBean.class);
        softAssertions.assertThat(ResultMapNameHelper.extractBeanClassInNestedResultMapKey("com.talanlabs.bean.mybatis.data.WagonBean$WheelBean/nestedResultMap")).isEqualTo(WagonBean.WheelBean.class);
        softAssertions.assertThat(ResultMapNameHelper.extractBeanClassInNestedResultMapKey("com.model.WagonBean/nestedResultMap")).isNull();
        softAssertions.assertAll();
    }

    @Test
    public void testExtractLevelInNestedResultMapKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(ResultMapNameHelper.extractDepthInNestedResultMapKey(null)).isNull();
        softAssertions.assertThat(ResultMapNameHelper.extractDepthInNestedResultMapKey("com.talanlabs.bean.mybatis.data.WagonBean/nestedResultMap")).isNull();
        softAssertions.assertThat(ResultMapNameHelper.extractDepthInNestedResultMapKey("com.talanlabs.bean.mybatis.data.WagonBean$WheelBean/nestedResultMap?depth=1&columnPrefix=t1")).isEqualTo(1);
        softAssertions.assertThat(ResultMapNameHelper.extractDepthInNestedResultMapKey("com.talanlabs.bean.mybatis.data.WagonBean$WheelBean/nestedResultMap?depth=-1&columnPrefix=t1")).isEqualTo(-1);
        softAssertions.assertThat(ResultMapNameHelper.extractDepthInNestedResultMapKey("com.talanlabs.bean.mybatis.data.WagonBean$WheelBean/nestedResultMap?depth=10&columnPrefix=gaby")).isEqualTo(10);
        softAssertions.assertThat(ResultMapNameHelper.extractDepthInNestedResultMapKey("com.model.WagonBean/nestedResultMap")).isNull();
        softAssertions.assertAll();
    }

    @Test
    public void testExtractPrefixInNestedResultMapKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(ResultMapNameHelper.extractColumnPrefixInNestedResultMapKey(null)).isNull();
        softAssertions.assertThat(ResultMapNameHelper.extractColumnPrefixInNestedResultMapKey("com.talanlabs.bean.mybatis.data.WagonBean/nestedResultMap")).isNull();
        softAssertions.assertThat(ResultMapNameHelper.extractColumnPrefixInNestedResultMapKey("com.talanlabs.bean.mybatis.data.WagonBean$WheelBean/nestedResultMap?depth=1&columnPrefix=1")).isEqualTo("1");
        softAssertions.assertThat(ResultMapNameHelper.extractColumnPrefixInNestedResultMapKey("com.talanlabs.bean.mybatis.data.WagonBean$WheelBean/nestedResultMap?depth=10&columnPrefix=gaby")).isEqualTo("gaby");
        softAssertions.assertThat(ResultMapNameHelper.extractColumnPrefixInNestedResultMapKey("com.model.WagonBean/nestedResultMap")).isNull();
        softAssertions.assertAll();
    }
}
