package com.talanlabs.bean.mybatis.cache;

import com.talanlabs.bean.mybatis.data.CountryBean;
import com.talanlabs.bean.mybatis.data.WagonBean;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

public class CacheNameHelperTest {

    @Test
    public void testBuildCacheKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(CacheNameHelper.buildCacheKey(null)).isNull();
        softAssertions.assertThat(CacheNameHelper.buildCacheKey(WagonBean.class)).isEqualTo("com.talanlabs.bean.mybatis.data.WagonBean/cache");
        softAssertions.assertThat(CacheNameHelper.buildCacheKey(CountryBean.class)).isEqualTo("com.talanlabs.bean.mybatis.data.CountryBean/cache");
        softAssertions.assertAll();
    }

    @Test
    public void testIsCacheKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(CacheNameHelper.isCacheKey(null)).isFalse();
        softAssertions.assertThat(CacheNameHelper.isCacheKey("com.model.IUser/cache")).isTrue();
        softAssertions.assertThat(CacheNameHelper.isCacheKey("com.model.IUser1/cache")).isTrue();
        softAssertions.assertThat(CacheNameHelper.isCacheKey("model.IUser/cache")).isTrue();
        softAssertions.assertThat(CacheNameHelper.isCacheKey("com.talanlabs.bean.mybatis.data.IUser/cache")).isTrue();
        softAssertions.assertThat(CacheNameHelper.isCacheKey("model.IUser")).isFalse();
        softAssertions.assertThat(CacheNameHelper.isCacheKey("/cache")).isFalse();
        softAssertions.assertThat(CacheNameHelper.isCacheKey("model-IUser/cache")).isFalse();
        softAssertions.assertAll();
    }

    @Test
    public void testExtractBeanNameInCacheKey() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(CacheNameHelper.extractBeanClassInCacheKey(null)).isNull();
        softAssertions.assertThat(CacheNameHelper.extractBeanClassInCacheKey("com.talanlabs.bean.mybatis.data.WagonBean/cache")).isEqualTo(WagonBean.class);
        softAssertions.assertThat(CacheNameHelper.extractBeanClassInCacheKey("com.talanlabs.bean.mybatis.data.CountryBean/cache")).isEqualTo(CountryBean.class);
        softAssertions.assertThat(CacheNameHelper.extractBeanClassInCacheKey("model.IUser/cache")).isNull();
        softAssertions.assertAll();
    }
}
