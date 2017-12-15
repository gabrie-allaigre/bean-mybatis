package com.talanlabs.bean.mybatis.cache;

import com.talanlabs.bean.mybatis.helper.BeanMyBatisHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CacheNameHelper {

    private static final String CACHE_NAME = "cache";

    private static final String BEAN_CLASS_PAT = "([a-zA-Z_$][a-zA-Z\\d_$]*\\.)*[a-zA-Z_$][a-zA-Z\\d_$]*";

    private static final Pattern CACHE_PATTERN = Pattern.compile("(" + BEAN_CLASS_PAT + ")/" + CACHE_NAME);

    private CacheNameHelper() {
        super();
    }

    // Cache

    /**
     * Build cache key
     *
     * @param beanClass bean class
     * @return key
     */
    public static String buildCacheKey(Class<?> beanClass) {
        if (beanClass == null) {
            return null;
        }
        return BeanMyBatisHelper.beanClassToString(beanClass) + "/" + CACHE_NAME;
    }

    /**
     * Is a cache key
     *
     * @param key key
     * @return true or false
     */
    public static boolean isCacheKey(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        Matcher m = CACHE_PATTERN.matcher(key);
        return m.matches();
    }

    /**
     * Extract bean in the key
     *
     * @param key key
     * @return bean class
     */
    public static Class<?> extractBeanClassInCacheKey(String key) {
        if (!isCacheKey(key)) {
            return null;
        }
        Matcher m = CACHE_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return BeanMyBatisHelper.loadBeanClass(m.group(1));
    }
}
