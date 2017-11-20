package com.talanlabs.bean.mybatis.statement;

import com.talanlabs.bean.mybatis.annotation.Annotations;
import com.talanlabs.bean.mybatis.annotation.JoinTable;
import com.talanlabs.bean.mybatis.annotation.OrderBy;
import com.talanlabs.bean.mybatis.helper.BeanMyBatisHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StatementNameHelper {

    public static final String FIND_ENTITY_BY_ID_NAME = "findEntityById";

    public static final String FIND_BEANS_BY_NAME = "findBeansBy";

    public static final String FIND_BEANS_BY_JOIN_TABLE_NAME = "findBeansByJoinTable";

    public static final String INSERT_NAME = "insert";

    public static final String UPDATE_NAME = "update";

    public static final String DELETE_NAME = "delete";

    public static final String DELETE_ENTITY_BY_ID_NAME = "deleteEntityById";

    public static final String DELETE_BEANS_BY_NAME = "deleteBeansBy";

    public static final String FIND_NLS_COLUMN_NAME = "findNlsColumn";

    public static final String PROPERTY = "property";

    public static final String PROPERTIES = "properties";

    public static final String SOURCE_PROPERTIES = "sourceProperties";

    public static final String TARGET_PROPERTIES = "targetProperties";

    public static final String SOURCE_BEAN = "sourceBean";

    public static final String NLS_PROPERTIES = "nlsProperties";

    public static final String JOIN = "join";

    public static final String IGNORE_CANCEL = "ignoreCancel";

    public static final String ORDER_BY = "orderBy";

    public static final String PARAM = "";

    public static final String PROPERTIES_SEPARATOR = ",";

    public static final String BEAN_CLASS_PAT = "([a-zA-Z_$][a-zA-Z\\d_$]*\\.)*[a-zA-Z_$][a-zA-Z\\d_$]*";

    public static final String PROPERTY_PAT = "[a-zA-Z_$][a-zA-Z\\d_$]*";

    public static final String PROPERTIES_PAT = "(" + PROPERTY_PAT + PROPERTIES_SEPARATOR + ")*" + PROPERTY_PAT;

    public static final String JOIN_PAT = PROPERTY_PAT + ";" + PROPERTIES_PAT + ";" + PROPERTIES_PAT;

    public static final String JOINS_PAT = "(" + JOIN_PAT + "#)*" + JOIN_PAT;

    public static final String SORT_PAT = "(Asc|Desc)";

    public static final String ORDER_BY_PAT = PROPERTY_PAT + ";" + SORT_PAT;

    public static final String ORDERS_BY_PAT = "(" + ORDER_BY_PAT + "#)*" + ORDER_BY_PAT;

    public static final Pattern FIND_ENTITY_BY_ID_PATTERN = Pattern.compile("(" + BEAN_CLASS_PAT + ")/" + FIND_ENTITY_BY_ID_NAME);

    public static final Pattern FIND_BEANS_BY_PATTERN = Pattern
            .compile("(" + BEAN_CLASS_PAT + ")/" + FIND_BEANS_BY_NAME + "\\?" + PROPERTIES + "=(" + PROPERTIES_PAT + ")(&" + ORDER_BY + "=(" + ORDERS_BY_PAT + "))?(&(" + IGNORE_CANCEL + "))?");

    public static final Pattern FIND_BEANS_BY_JOIN_TABLE_PATTERN = Pattern.compile(
            "(" + BEAN_CLASS_PAT + ")/" + FIND_BEANS_BY_JOIN_TABLE_NAME + "\\?" + SOURCE_BEAN + "=(" + BEAN_CLASS_PAT + ")&" + SOURCE_PROPERTIES + "=(" + PROPERTIES_PAT + ")&" + TARGET_PROPERTIES
                    + "=(" + PROPERTIES_PAT + ")&" + JOIN + "=(" + JOINS_PAT + ")(&" + ORDER_BY + "=(" + ORDERS_BY_PAT + "))?(&(" + IGNORE_CANCEL + "))?");

    public static final Pattern INSERT_PATTERN = Pattern.compile("(" + BEAN_CLASS_PAT + ")/" + INSERT_NAME);

    public static final Pattern UPDATE_PATTERN = Pattern
            .compile("(" + BEAN_CLASS_PAT + ")/" + UPDATE_NAME + "\\?" + PROPERTIES + "=(" + PROPERTIES_PAT + ")?(&" + NLS_PROPERTIES + "=(" + PROPERTIES_PAT + ")?)?");

    public static final Pattern DELETE_PATTERN = Pattern.compile("(" + BEAN_CLASS_PAT + ")/" + DELETE_NAME);

    public static final Pattern DELETE_ENTITY_BY_ID_PATTERN = Pattern.compile("(" + BEAN_CLASS_PAT + ")/" + DELETE_ENTITY_BY_ID_NAME);

    public static final Pattern DELETE_BEANS_BY_PATTERN = Pattern.compile("(" + BEAN_CLASS_PAT + ")/" + DELETE_BEANS_BY_NAME + "\\?" + PROPERTIES + "=(" + PROPERTIES_PAT + ")");

    public static final Pattern FIND_NLS_COLUMN_PATTERN = Pattern.compile("(" + BEAN_CLASS_PAT + ")/" + FIND_NLS_COLUMN_NAME + "\\?" + PROPERTY + "=(" + PROPERTY_PAT + ")");

    private StatementNameHelper() {
        super();
    }

    /**
     * Get a param
     *
     * @param i number
     * @return param + number
     */
    public static String buildParam(int i) {
        return PARAM + i;
    }

    // FindEntityById

    /**
     * Build find entity by id key
     *
     * @param beanClass bean class
     * @return key
     */
    public static <E> String buildFindEntityByIdKey(Class<E> beanClass) {
        if (beanClass == null) {
            return null;
        }
        return BeanMyBatisHelper.beanClassToString(beanClass) + "/" + FIND_ENTITY_BY_ID_NAME;
    }

    /**
     * Verify is find entity by id key
     *
     * @param key key
     * @return true or false
     */
    public static boolean isFindEntityByIdKey(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        Matcher m = FIND_ENTITY_BY_ID_PATTERN.matcher(key);
        return m.matches();
    }

    /**
     * Extract bean in the key
     *
     * @param key key
     * @return bean class
     */
    public static <E> Class<E> extractBeanClassInFindEntityByIdKey(String key) {
        if (!isFindEntityByIdKey(key)) {
            return null;
        }
        Matcher m = FIND_ENTITY_BY_ID_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return BeanMyBatisHelper.loadBeanClass(m.group(1));
    }

    // FindBeansBy

    /**
     * Build find beans by id key
     *
     * @param beanClass      bean class
     * @param useCheckCancel use check cancel
     * @param propertyNames  array of property
     * @param orderBys       list of order by
     * @return key
     */
    public static <E> String buildFindBeansByKey(Class<E> beanClass, boolean useCheckCancel, String[] propertyNames, OrderBy[] orderBys) {
        if (beanClass == null || propertyNames == null || propertyNames.length == 0) {
            return null;
        }
        List<String> os = orderBys != null && orderBys.length > 0 ? Arrays.stream(orderBys).map(o -> o.value() + ";" + o.sort().name()).collect(Collectors.toList()) : null;
        return BeanMyBatisHelper.beanClassToString(beanClass) + "/" + FIND_BEANS_BY_NAME + "?" + PROPERTIES + "=" + String.join(PROPERTIES_SEPARATOR, propertyNames) + (os != null ?
                "&" + ORDER_BY + "=" + String.join("#", os) :
                "") + (useCheckCancel ? "&" + IGNORE_CANCEL : "");
    }

    /**
     * Verify is find beans by
     *
     * @param key key
     * @return true or false
     */
    public static boolean isFindBeansByKey(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        Matcher m = FIND_BEANS_BY_PATTERN.matcher(key);
        return m.matches();
    }

    /**
     * Extract bean in the key
     *
     * @param key key
     * @return bean class
     */
    public static <E> Class<E> extractBeanClassInFindBeansByKey(String key) {
        if (!isFindBeansByKey(key)) {
            return null;
        }
        Matcher m = FIND_BEANS_BY_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return BeanMyBatisHelper.loadBeanClass(m.group(1));
    }

    /**
     * Extract properties
     *
     * @param key key
     * @return properties
     */
    public static String[] extractPropertyNamesInFindBeansByKey(String key) {
        if (!isFindBeansByKey(key)) {
            return null;
        }
        Matcher m = FIND_BEANS_BY_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return m.group(3).split(PROPERTIES_SEPARATOR);
    }

    /**
     * Extract order by
     *
     * @param key key
     * @return order by
     */
    public static OrderBy[] extractOrderBiesInFindBeansByKey(String key) {
        if (!isFindBeansByKey(key)) {
            return null;
        }
        Matcher m = FIND_BEANS_BY_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        String group = m.group(6);
        List<OrderBy> res = new ArrayList<>();
        if (StringUtils.isNotBlank(group)) {
            String[] os = group.split("#");
            for (String o : os) {
                String[] ss = o.split(";");
                res.add(Annotations.orderBy(ss[0], OrderBy.Sort.valueOf(ss[1])));
            }
        }
        return res.toArray(new OrderBy[res.size()]);
    }

    /**
     * Extract ignore cancel
     *
     * @param key key
     * @return true or false
     */
    public static boolean isIgnoreCancelInFindBeansByKey(String key) {
        if (!isFindBeansByKey(key)) {
            return false;
        }
        Matcher m = FIND_BEANS_BY_PATTERN.matcher(key);
        return m.find() && IGNORE_CANCEL.equals(m.group(11));
    }

    // FindBeansByJoinTable

    /**
     * Create a key for Find a beans by join table
     *
     * @param sourceBeanClass  source bean class
     * @param targetBeanClass  target bean class
     * @param useCheckCancel   use check cancel
     * @param joins            list of join
     * @param sourceProperties properties source
     * @param targetProperties properties target
     * @param orderBys         list of order by
     * @return key
     */
    public static <E, F> String buildFindBeansByJoinTableKey(Class<E> sourceBeanClass, Class<F> targetBeanClass, boolean useCheckCancel, JoinTable[] joins, String[] sourceProperties,
            String[] targetProperties, OrderBy[] orderBys) {
        if (sourceBeanClass == null || targetBeanClass == null || sourceProperties == null || sourceProperties.length == 0 || joins == null || joins.length == 0 || targetProperties == null
                || targetProperties.length == 0) {
            return null;
        }
        List<String> js = Arrays.stream(joins).map(join -> join.name() + ";" + String.join(PROPERTIES_SEPARATOR, join.left()) + ";" + String.join(PROPERTIES_SEPARATOR, join.right()))
                .collect(Collectors.toList());
        List<String> os = orderBys != null && orderBys.length > 0 ? Arrays.stream(orderBys).map(o -> o.value() + ";" + o.sort().name()).collect(Collectors.toList()) : null;
        return BeanMyBatisHelper.beanClassToString(targetBeanClass) + "/" + FIND_BEANS_BY_JOIN_TABLE_NAME + "?" + SOURCE_BEAN + "=" + BeanMyBatisHelper.beanClassToString(sourceBeanClass) + "&"
                + SOURCE_PROPERTIES + "=" + String.join(PROPERTIES_SEPARATOR, sourceProperties) + "&" + TARGET_PROPERTIES + "=" + String.join(PROPERTIES_SEPARATOR, targetProperties) + "&" + JOIN + "="
                + String.join("#", js) + (os != null ? "&" + ORDER_BY + "=" + String.join("#", os) : "") + (useCheckCancel ? "&" + IGNORE_CANCEL : "");
    }

    /**
     * Verify is find beans by join table key
     *
     * @param key key
     * @return true or false
     */
    public static boolean isFindBeansByJoinTableKey(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        Matcher m = FIND_BEANS_BY_JOIN_TABLE_PATTERN.matcher(key);
        return m.matches();
    }

    /**
     * Extract bean in the key
     *
     * @param key key
     * @return bean class
     */
    public static <E> Class<E> extractBeanClassInFindBeansByJoinTableKey(String key) {
        if (!isFindBeansByJoinTableKey(key)) {
            return null;
        }
        Matcher m = FIND_BEANS_BY_JOIN_TABLE_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return BeanMyBatisHelper.loadBeanClass(m.group(1));
    }

    /**
     * Extract source bean in the key
     *
     * @param key key
     * @return bean class
     */
    public static <E> Class<E> extractSourceBeanClassInFindBeansByJoinTableKey(String key) {
        if (!isFindBeansByJoinTableKey(key)) {
            return null;
        }
        Matcher m = FIND_BEANS_BY_JOIN_TABLE_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return BeanMyBatisHelper.loadBeanClass(m.group(3));
    }

    /**
     * Extract source properties
     *
     * @param key key
     * @return properties
     */
    public static String[] extractSourcePropertiesInFindBeansByJoinTableKey(String key) {
        if (!isFindBeansByJoinTableKey(key)) {
            return null;
        }
        Matcher m = FIND_BEANS_BY_JOIN_TABLE_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return m.group(5).split(PROPERTIES_SEPARATOR);
    }

    /**
     * Extract target properties
     *
     * @param key key
     * @return properties
     */
    public static String[] extractTargetPropertiesInFindBeansByJoinTableKey(String key) {
        if (!isFindBeansByJoinTableKey(key)) {
            return null;
        }
        Matcher m = FIND_BEANS_BY_JOIN_TABLE_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return m.group(7).split(PROPERTIES_SEPARATOR);
    }

    /**
     * Extract join table
     *
     * @param key key
     * @return list of join
     */
    public static JoinTable[] extractJoinInFindBeansByJoinTableKey(String key) {
        if (!isFindBeansByJoinTableKey(key)) {
            return null;
        }
        Matcher m = FIND_BEANS_BY_JOIN_TABLE_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        String[] joins = m.group(9).split("#");
        List<JoinTable> res = new ArrayList<>();
        for (String join : joins) {
            String[] ss = join.split(";");
            res.add(Annotations.joinTable(ss[0], ss[1].split(PROPERTIES_SEPARATOR), ss[2].split(PROPERTIES_SEPARATOR)));
        }
        return res.toArray(new JoinTable[res.size()]);
    }

    /**
     * Extract order by
     *
     * @param key key
     * @return order by
     */
    public static OrderBy[] extractOrderBiesInFindBeansByJoinTableKey(String key) {
        if (!isFindBeansByJoinTableKey(key)) {
            return null;
        }
        Matcher m = FIND_BEANS_BY_JOIN_TABLE_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        String group = m.group(16);
        List<OrderBy> res = new ArrayList<>();
        if (StringUtils.isNotBlank(group)) {
            String[] os = group.split("#");
            for (String o : os) {
                String[] ss = o.split(";");
                res.add(Annotations.orderBy(ss[0], OrderBy.Sort.valueOf(ss[1])));
            }
        }
        return res.toArray(new OrderBy[res.size()]);
    }

    /**
     * Extract ignore cancel
     *
     * @param key key
     * @return true or false
     */
    public static boolean isIgnoreCancelInFindBeansByJoinTableKey(String key) {
        if (!isFindBeansByJoinTableKey(key)) {
            return false;
        }
        Matcher m = FIND_BEANS_BY_JOIN_TABLE_PATTERN.matcher(key);
        return m.find() && IGNORE_CANCEL.equals(m.group(21));
    }

    // Insert

    /**
     * Build a insert key
     *
     * @param beanClass bean class
     * @return key
     */
    public static <E> String buildInsertKey(Class<E> beanClass) {
        if (beanClass == null) {
            return null;
        }
        return BeanMyBatisHelper.beanClassToString(beanClass) + "/" + INSERT_NAME;
    }

    /**
     * Verify is insert key
     *
     * @param key key
     * @return true or false
     */
    public static boolean isInsertKey(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        Matcher m = INSERT_PATTERN.matcher(key);
        return m.matches();
    }

    /**
     * Extract bean in the key
     *
     * @param key key
     * @return bean class
     */
    public static <E> Class<E> extractBeanClassInInsertKey(String key) {
        if (!isInsertKey(key)) {
            return null;
        }
        Matcher m = INSERT_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return BeanMyBatisHelper.loadBeanClass(m.group(1));
    }

    // Update

    /**
     * Build update key
     *
     * @param beanClass bean class
     * @return key
     */
    public static <E> String buildUpdateKey(Class<E> beanClass, String[] propertyNames, String[] nlsPropertyNames) {
        if (beanClass == null) {
            return null;
        }
        return BeanMyBatisHelper.beanClassToString(beanClass) + "/" + UPDATE_NAME + "?" + PROPERTIES + "=" + String.join(PROPERTIES_SEPARATOR, propertyNames != null ? propertyNames : new String[0])
                + "&" + NLS_PROPERTIES + "=" + String.join(PROPERTIES_SEPARATOR, nlsPropertyNames != null ? nlsPropertyNames : new String[0]);
    }

    /**
     * Verify is update key
     *
     * @param key key
     * @return true or false
     */
    public static boolean isUpdateKey(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        Matcher m = UPDATE_PATTERN.matcher(key);
        return m.matches();
    }

    /**
     * Extract bean in the key
     *
     * @param key key
     * @return bean class
     */
    public static <E> Class<E> extractBeanClassInUpdateKey(String key) {
        if (!isUpdateKey(key)) {
            return null;
        }
        Matcher m = UPDATE_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return BeanMyBatisHelper.loadBeanClass(m.group(1));
    }

    /**
     * Extract source properties
     *
     * @param key key
     * @return properties
     */
    public static String[] extractPropertiesInUpdateKey(String key) {
        if (!isUpdateKey(key)) {
            return null;
        }
        Matcher m = UPDATE_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        String properties = m.group(3);
        return properties != null ? properties.split(PROPERTIES_SEPARATOR) : new String[0];
    }

    /**
     * Extract source properties
     *
     * @param key key
     * @return properties
     */
    public static String[] extractNlsPropertiesInUpdateKey(String key) {
        if (!isUpdateKey(key)) {
            return null;
        }
        Matcher m = UPDATE_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        String properties = m.group(6);
        return properties != null ? properties.split(PROPERTIES_SEPARATOR) : new String[0];
    }

    // Delete

    /**
     * Build delete key
     *
     * @param beanClass bean class
     * @return key
     */
    public static <E> String buildDeleteKey(Class<E> beanClass) {
        if (beanClass == null) {
            return null;
        }
        return BeanMyBatisHelper.beanClassToString(beanClass) + "/" + DELETE_NAME;
    }

    /**
     * Verify is delete key
     *
     * @param key key
     * @return true or false
     */
    public static boolean isDeleteKey(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        Matcher m = DELETE_PATTERN.matcher(key);
        return m.matches();
    }

    /**
     * Extract bean in the key
     *
     * @param key key
     * @return bean class
     */
    public static <E> Class<E> extractBeanClassInDeleteKey(String key) {
        if (!isDeleteKey(key)) {
            return null;
        }
        Matcher m = DELETE_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return BeanMyBatisHelper.loadBeanClass(m.group(1));
    }

    // NlsColumn

    /**
     * Build nls key
     *
     * @param beanClass bean class
     * @param property  property
     * @return key
     */
    public static <E> String buildFindNlsColumnKey(Class<E> beanClass, String property) {
        if (beanClass == null) {
            return null;
        }
        return BeanMyBatisHelper.beanClassToString(beanClass) + "/" + FIND_NLS_COLUMN_NAME + "?" + PROPERTY + "=" + property;
    }

    /**
     * Verify is nls key
     *
     * @param key key
     * @return true or false
     */
    public static boolean isFindNlsColumnKey(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        Matcher m = FIND_NLS_COLUMN_PATTERN.matcher(key);
        return m.matches();
    }

    /**
     * Extract bean in the key
     *
     * @param key key
     * @return bean class
     */
    public static <E> Class<E> extractBeanClassInFindNlsColumnKey(String key) {
        if (!isFindNlsColumnKey(key)) {
            return null;
        }
        Matcher m = FIND_NLS_COLUMN_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return BeanMyBatisHelper.loadBeanClass(m.group(1));
    }

    /**
     * Extract property
     *
     * @param key key
     * @return properties
     */
    public static String extractPropertyNameInFindNlsColumnByKey(String key) {
        if (!isFindNlsColumnKey(key)) {
            return null;
        }
        Matcher m = FIND_NLS_COLUMN_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return m.group(3);
    }

    // DeleteEntityById

    /**
     * Build delete entity by id key
     *
     * @param beanClass bean class
     * @return key
     */
    public static <E> String buildDeleteEntityByIdKey(Class<E> beanClass) {
        if (beanClass == null) {
            return null;
        }
        return BeanMyBatisHelper.beanClassToString(beanClass) + "/" + DELETE_ENTITY_BY_ID_NAME;
    }

    /**
     * Verify is delete entity by id key
     *
     * @param key key
     * @return true or false
     */
    public static boolean isDeleteEntityByIdKey(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        Matcher m = DELETE_ENTITY_BY_ID_PATTERN.matcher(key);
        return m.matches();
    }

    /**
     * Extract bean in the key
     *
     * @param key key
     * @return bean class
     */
    public static <E> Class<E> extractBeanClassInDeleteEntityByIdKey(String key) {
        if (!isDeleteEntityByIdKey(key)) {
            return null;
        }
        Matcher m = DELETE_ENTITY_BY_ID_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return BeanMyBatisHelper.loadBeanClass(m.group(1));
    }

    // DeleteBeansBy

    /**
     * Build delete beans by
     *
     * @param beanClass     bean class
     * @param propertyNames array of property
     * @return key
     */
    public static <E> String buildDeleteBeansByKey(Class<E> beanClass, String... propertyNames) {
        if (beanClass == null || propertyNames == null || propertyNames.length == 0) {
            return null;
        }
        return BeanMyBatisHelper.beanClassToString(beanClass) + "/" + DELETE_BEANS_BY_NAME + "?" + PROPERTIES + "=" + String.join(PROPERTIES_SEPARATOR, propertyNames);
    }

    /**
     * Verify is delete beans by
     *
     * @param key key
     * @return true or false
     */
    public static boolean isDeleteBeansByKey(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        Matcher m = DELETE_BEANS_BY_PATTERN.matcher(key);
        return m.matches();
    }

    /**
     * Extract bean in the key
     *
     * @param key key
     * @return bean class
     */
    public static <E> Class<E> extractBeanClassInDeleteBeansByKey(String key) {
        if (!isDeleteBeansByKey(key)) {
            return null;
        }
        Matcher m = DELETE_BEANS_BY_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return BeanMyBatisHelper.loadBeanClass(m.group(1));
    }

    /**
     * Extract properties
     *
     * @param key key
     * @return properties
     */
    public static String[] extractPropertyNamesInDeleteBeansByKey(String key) {
        if (!isDeleteBeansByKey(key)) {
            return null;
        }
        Matcher m = DELETE_BEANS_BY_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return m.group(3).split(PROPERTIES_SEPARATOR);
    }

}
