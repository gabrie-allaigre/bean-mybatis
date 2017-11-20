package com.talanlabs.bean.mybatis.rsql.statement;

import com.talanlabs.bean.mybatis.helper.BeanMyBatisHelper;
import com.talanlabs.bean.mybatis.statement.StatementNameHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RsqlStatementNameHelper {

    public static final String RSQL_NAME = "rsql";
    public static final String COUNT_RSQL_NAME = "countRsql";
    public static final Pattern RSQL_PATTERN = Pattern.compile("(" + StatementNameHelper.BEAN_CLASS_PAT + ")/" + RSQL_NAME);
    public static final Pattern COUNT_RSQL_PATTERN = Pattern.compile("(" + StatementNameHelper.BEAN_CLASS_PAT + ")/" + COUNT_RSQL_NAME);

    private RsqlStatementNameHelper() {
        super();
    }

    // Rsql

    /**
     * Build rsql key
     *
     * @param beanClass bean class
     * @return key
     */
    public static String buildRsqlKey(Class<?> beanClass) {
        if (beanClass == null) {
            return null;
        }
        return BeanMyBatisHelper.beanClassToString(beanClass) + "/" + RSQL_NAME;
    }

    /**
     * Verify is rsql key
     *
     * @param key key
     * @return true or false
     */
    public static boolean isRsqlKey(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        Matcher m = RSQL_PATTERN.matcher(key);
        return m.matches();
    }

    /**
     * Extract bean in the key
     *
     * @param key key
     * @return bean class
     */
    public static <E> Class<E> extractBeanClassInRsqlKey(String key) {
        if (!isRsqlKey(key)) {
            return null;
        }
        Matcher m = RSQL_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return BeanMyBatisHelper.loadBeanClass(m.group(1));
    }

    // CountRsql

    /**
     * Build rsql key
     *
     * @param beanClass bean class
     * @return key
     */
    public static <E> String buildCountRsqlKey(Class<E> beanClass) {
        if (beanClass == null) {
            return null;
        }
        return BeanMyBatisHelper.beanClassToString(beanClass) + "/" + COUNT_RSQL_NAME;
    }

    /**
     * Verify is rsql key
     *
     * @param key key
     * @return true or false
     */
    public static boolean isCountRsqlKey(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        Matcher m = COUNT_RSQL_PATTERN.matcher(key);
        return m.matches();
    }

    /**
     * Extract bean in the key
     *
     * @param key key
     * @return bean class
     */
    public static <E> Class<E> extractBeanClassInCountRsqlKey(String key) {
        if (!isCountRsqlKey(key)) {
            return null;
        }
        Matcher m = COUNT_RSQL_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return BeanMyBatisHelper.loadBeanClass(m.group(1));
    }
}
