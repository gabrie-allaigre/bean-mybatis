package com.talanlabs.bean.mybatis.resultmap;

import com.talanlabs.bean.mybatis.helper.BeanMyBatisHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResultMapNameHelper {

    public static final String PREFIX_PAT = "[a-zA-Z\\d_$][a-zA-Z\\d_$]*";
    public static final String DEPTH_PAT = "(-)?[\\d$][\\d$]*";
    public static final String RESULT_MAP_NAME = "resultMap";
    public static final String NESTED_RESULT_MAP_NAME = "nestedResultMap";
    public static final String DEPTH_NAME = "depth";
    public static final String COLUMN_PREFIX_NAME = "columnPrefix";
    public static final String BEAN_CLASS_PAT = "([a-zA-Z_$][a-zA-Z\\d_$]*\\.)*[a-zA-Z_$][a-zA-Z\\d_$]*";
    public static final Pattern NESTED_RESULT_MAP_PATTERN = Pattern
            .compile("(" + BEAN_CLASS_PAT + ")/" + NESTED_RESULT_MAP_NAME + "(" + "\\?" + DEPTH_NAME + "=(" + DEPTH_PAT + ")&" + COLUMN_PREFIX_NAME + "=(" + PREFIX_PAT + "))?");
    public static final Pattern RESULT_MAP_PATTERN = Pattern
            .compile("(" + BEAN_CLASS_PAT + ")/" + RESULT_MAP_NAME);

    private ResultMapNameHelper() {
        super();
    }

    // ResultMap

    /**
     * Build key for result map
     *
     * @param beanClass bean class
     * @return key
     */
    public static String buildResultMapKey(Class<?> beanClass) {
        if (beanClass == null) {
            return null;
        }
        return BeanMyBatisHelper.beanClassToString(beanClass) + "/" + RESULT_MAP_NAME;
    }

    /**
     * Is a result map key
     *
     * @param key key
     * @return true or false
     */
    public static boolean isResultMapKey(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        Matcher m = RESULT_MAP_PATTERN.matcher(key);
        return m.matches();
    }

    /**
     * Extract bean in the key
     *
     * @param key key
     * @return bean class
     */
    public static Class<?> extractBeanClassInResultMapKey(String key) {
        if (!isResultMapKey(key)) {
            return null;
        }
        Matcher m = RESULT_MAP_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return BeanMyBatisHelper.loadBeanClass(m.group(1));
    }

    // NestedResultMap

    /**
     * Build key for result map
     *
     * @param beanClass bean class
     * @return key
     */
    public static String buildNestedResultMapKey(Class<?> beanClass) {
        if (beanClass == null) {
            return null;
        }
        return BeanMyBatisHelper.beanClassToString(beanClass) + "/" + NESTED_RESULT_MAP_NAME;
    }

    /**
     * Build key for result map
     *
     * @param beanClass bean class
     * @param depth          depth
     * @param columnPrefix   column prefix
     * @return key
     */
    public static String buildNestedResultMapKey(Class<?> beanClass, int depth, String columnPrefix) {
        if (beanClass == null) {
            return null;
        }
        return BeanMyBatisHelper.beanClassToString(beanClass) + "/" + NESTED_RESULT_MAP_NAME + "?" + DEPTH_NAME + "=" + depth + "&" + COLUMN_PREFIX_NAME + "=" + columnPrefix;
    }

    /**
     * Is a result map key
     *
     * @param key key
     * @return true or false
     */
    public static boolean isNestedResultMapKey(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        Matcher m = NESTED_RESULT_MAP_PATTERN.matcher(key);
        return m.matches();
    }

    /**
     * Extract bean in the key
     *
     * @param key key
     * @return bean class
     */
    public static Class<?> extractBeanClassInNestedResultMapKey(String key) {
        if (!isNestedResultMapKey(key)) {
            return null;
        }
        Matcher m = NESTED_RESULT_MAP_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return BeanMyBatisHelper.loadBeanClass(m.group(1));
    }

    /**
     * Extract depth in the key
     *
     * @param key key
     * @return depth
     */
    public static Integer extractDepthInNestedResultMapKey(String key) {
        if (!isNestedResultMapKey(key)) {
            return null;
        }
        Matcher m = NESTED_RESULT_MAP_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        String level = m.group(4);
        return NumberUtils.isNumber(level) ? Integer.parseInt(level) : null;
    }

    /**
     * Extract prefix in the key
     *
     * @param key key
     * @return key
     */
    public static String extractColumnPrefixInNestedResultMapKey(String key) {
        if (!isNestedResultMapKey(key)) {
            return null;
        }
        Matcher m = NESTED_RESULT_MAP_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return m.group(6);
    }
}
