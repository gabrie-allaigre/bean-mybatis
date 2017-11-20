package com.talanlabs.bean.mybatis.session.meta;

import com.google.common.reflect.TypeToken;
import com.talanlabs.bean.mybatis.annotation.Association;
import com.talanlabs.bean.mybatis.annotation.Cache;
import com.talanlabs.bean.mybatis.annotation.Canceled;
import com.talanlabs.bean.mybatis.annotation.Collection;
import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.bean.mybatis.annotation.Id;
import com.talanlabs.bean.mybatis.annotation.NlsColumn;
import com.talanlabs.bean.mybatis.annotation.Version;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MetaBean {

    private final Function<Class<?>, MetaInfoBean> createMetaInfoBeanFunction;
    private final com.google.common.cache.Cache<Class<?>, MetaInfoBean> metaInfoBeanCache;

    private ValidatorBean validatorBean;

    public MetaBean(BeanConfiguration beanConfiguration) {
        this(beanConfiguration, DefaultMetaInfoBean::new);
    }

    public MetaBean(BeanConfiguration beanConfiguration, Function<Class<?>, MetaInfoBean> createMetaInfoBeanFunction) {
        super();

        this.createMetaInfoBeanFunction = createMetaInfoBeanFunction;
        this.metaInfoBeanCache = com.google.common.cache.CacheBuilder.newBuilder().concurrencyLevel(4).build();
        this.validatorBean = new DefaultValidatorBean(beanConfiguration);
    }

    public ValidatorBean getValidatorBean() {
        return validatorBean;
    }

    public void setValidatorBean(ValidatorBean validatorBean) {
        this.validatorBean = validatorBean;
    }

    /**
     * Get meta info bean
     *
     * @param beanClass bean class
     * @return meta info bean
     */
    public MetaInfoBean forBeanClass(Class<?> beanClass) {
        try {
            return this.metaInfoBeanCache.get(beanClass, () -> this.createMetaInfoBeanFunction.apply(beanClass));
        } catch (ExecutionException e) {
            throw new MetaBeanException("Failed to get MetaInfoBean for Bean=" + beanClass, e);
        }
    }

    /**
     * Find propertyName with annotation Id
     *
     * @param beanClass bean class
     * @return propertyName
     */
    public String getIdPropertyName(Class<?> beanClass) {
        MetaInfoBean metaInfoBean = forBeanClass(beanClass);
        for (String propertyName : metaInfoBean.getPropertyNames()) {
            if (metaInfoBean.isPropertyAnnotationPresent(propertyName, Id.class)) {
                return propertyName;
            }
        }
        return null;
    }

    /**
     * Find propertyName with annotation Version
     *
     * @param beanClass bean class
     * @return propertyName
     */
    public String getVersionPropertyName(Class<?> beanClass) {
        MetaInfoBean metaInfoBean = forBeanClass(beanClass);
        for (String propertyName : metaInfoBean.getPropertyNames()) {
            Class<?> type = metaInfoBean.getPropertyClass(propertyName);
            if (metaInfoBean.isPropertyAnnotationPresent(propertyName, Version.class) && (Integer.class.equals(type) || Long.class.equals(type))) {
                return propertyName;
            }
        }
        return null;
    }

    /**
     * Find propertyName with annotation Column with cancelable
     *
     * @param beanClass bean class
     * @return propertyName
     */
    public String getCanceledPropertyName(Class<?> beanClass) {
        MetaInfoBean metaInfoBean = forBeanClass(beanClass);
        for (String propertyName : metaInfoBean.getPropertyNames()) {
            Class<?> type = metaInfoBean.getPropertyClass(propertyName);
            if (metaInfoBean.isPropertyAnnotationPresent(propertyName, Canceled.class) && (Boolean.class.equals(type) || boolean.class.equals(type))) {
                return propertyName;
            }
        }
        return null;
    }

    /**
     * Get all property names with nlscolumn
     *
     * @param beanClass bean class
     * @return property with nlscolumn
     */
    public Set<String> getPropertyNamesWithNlsColumn(Class<?> beanClass) {
        MetaInfoBean metaInfoBean = forBeanClass(beanClass);
        return metaInfoBean.getPropertyNames().stream().filter(pn -> metaInfoBean.isPropertyAnnotationPresent(pn, NlsColumn.class)).collect(Collectors.toSet());
    }

    /**
     * Verify if bean class use NlsColumn
     *
     * @param beanClass bean class
     * @return bean use nls column
     */
    public boolean isUseNlsColumn(Class<?> beanClass) {
        MetaInfoBean metaInfoBean = forBeanClass(beanClass);
        return metaInfoBean.getPropertyNames().stream().anyMatch(pn -> metaInfoBean.isPropertyAnnotationPresent(pn, NlsColumn.class));
    }

    /**
     * Prepare sources properties with column name
     *
     * @param beanClass       bean class
     * @param propertySources property source
     * @return list of column
     */
    public String[] prepareColumns(Class<?> beanClass, String[] propertySources) {
        MetaInfoBean metaInfoBean = forBeanClass(beanClass);

        List<String> columnNames = new ArrayList<>();
        for (String propertyName : propertySources) {
            columnNames.add(metaInfoBean.getPropertyAnnotation(propertyName, Column.class).name());
        }
        return columnNames.toArray(new String[columnNames.size()]);
    }

    /**
     * Get type of column
     *
     * @param beanClass    bean class
     * @param propertyName property name
     * @return java class
     */
    public Class<?> getColumnClass(Class<?> beanClass, String propertyName) {
        MetaInfoBean metaInfoBean = forBeanClass(beanClass);
        Column column = metaInfoBean.getPropertyAnnotation(propertyName, Column.class);
        return column.javaType() != void.class ? column.javaType() : metaInfoBean.getPropertyClass(propertyName);
    }

    /**
     * Get type of column
     *
     * @param beanClass    bean class
     * @param propertyName property name
     * @return java type
     */
    public Type getColumnType(Class<?> beanClass, String propertyName) {
        MetaInfoBean metaInfoBean = forBeanClass(beanClass);
        Column column = metaInfoBean.getPropertyAnnotation(propertyName, Column.class);
        return column.javaType() != void.class ? column.javaType() : metaInfoBean.getPropertyType(propertyName);
    }

    /**
     * Get type of column
     *
     * @param beanClass    bean class
     * @param propertyName property name
     * @return java class
     */
    public Class<?> getNlsColumnClass(Class<?> beanClass, String propertyName) {
        MetaInfoBean metaInfoBean = forBeanClass(beanClass);
        NlsColumn nlsColumn = metaInfoBean.getPropertyAnnotation(propertyName, NlsColumn.class);
        return nlsColumn.javaType() != void.class ? nlsColumn.javaType() : metaInfoBean.getPropertyClass(propertyName);
    }

    /**
     * Get type of column
     *
     * @param beanClass    bean class
     * @param propertyName property name
     * @return java type
     */
    public Type getNlsColumnType(Class<?> beanClass, String propertyName) {
        MetaInfoBean metaInfoBean = forBeanClass(beanClass);
        NlsColumn nlsColumn = metaInfoBean.getPropertyAnnotation(propertyName, NlsColumn.class);
        return nlsColumn.javaType() != void.class ? nlsColumn.javaType() : metaInfoBean.getPropertyType(propertyName);
    }

    /**
     * Get type of association
     *
     * @param beanClass    bean class
     * @param propertyName property name
     * @return java class
     */
    public Class<?> getAssociationClass(Class<?> beanClass, String propertyName) {
        MetaInfoBean metaInfoBean = forBeanClass(beanClass);
        Association association = metaInfoBean.getPropertyAnnotation(propertyName, Association.class);
        return association.javaType() != void.class ? association.javaType() : metaInfoBean.getPropertyClass(propertyName);
    }

    /**
     * Get type of collection
     *
     * @param beanClass    bean class
     * @param propertyName property name
     * @return java class
     */
    public Class<?> getCollectionClass(Class<?> beanClass, String propertyName) {
        MetaInfoBean metaInfoBean = forBeanClass(beanClass);
        Collection collection = metaInfoBean.getPropertyAnnotation(propertyName, Collection.class);
        return collection.javaType() != java.util.Collection.class ? collection.javaType() : metaInfoBean.getPropertyClass(propertyName);
    }

    /**
     * Get element type of collection
     *
     * @param beanClass    bean class
     * @param propertyName property name
     * @return element type
     */
    public Class<?> getCollectionElementClass(Class<?> beanClass, String propertyName) {
        MetaInfoBean metaInfoBean = forBeanClass(beanClass);
        Collection collection = metaInfoBean.getPropertyAnnotation(propertyName, Collection.class);
        Class<?> ofType = collection.ofType();
        if (ofType == void.class) {
            TypeToken tt = TypeToken.of(forBeanClass(beanClass).getPropertyType(propertyName)).resolveType(java.util.Collection.class.getTypeParameters()[0]);
            ofType = tt.getRawType();
        }
        return ofType;
    }

    /**
     * Verify if bean class use NlsColumn annotation and children
     *
     * @param beanClass bean class
     * @return true if use NlsColumn annotation
     */
    public boolean isAllUseNlsColumn(Class<?> beanClass) {
        return isAllUseNlsColumnDejaVue(beanClass, new HashSet<>());
    }

    @SuppressWarnings("unchecked")
    private boolean isAllUseNlsColumnDejaVue(Class<?> beanClass, Set<Class<?>> dejaVues) {
        if (!beanClass.isAnnotationPresent(Entity.class)) {
            return false;
        }

        dejaVues.add(beanClass);

        MetaInfoBean metaInfoBean = forBeanClass(beanClass);

        for (String propertyName : metaInfoBean.getPropertyNames()) {
            if (metaInfoBean.isPropertyAnnotationPresent(propertyName, NlsColumn.class)) {
                return true;
            } else if (metaInfoBean.isPropertyAnnotationPresent(propertyName, Association.class)) {
                Class<?> javaType = getAssociationClass(beanClass, propertyName);
                if (!dejaVues.contains(javaType) && isAllUseNlsColumnDejaVue(javaType, dejaVues)) {
                    return true;
                }
            } else if (metaInfoBean.isPropertyAnnotationPresent(propertyName, Collection.class)) {
                Class<?> elementClass = getCollectionElementClass(beanClass, propertyName);
                if (!dejaVues.contains(elementClass) && isAllUseNlsColumnDejaVue(elementClass, dejaVues)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Find children for bean class, all level
     *
     * @param beanClass bean class
     * @return all children
     */
    public Set<Class<?>> findAllLinks(Class<?> beanClass) {
        Set<Class<?>> res = new HashSet<>();
        findAllLinks(res, beanClass);
        return res;
    }

    private void findAllLinks(Set<Class<?>> res, Class<?> beanClass) {
        Set<Class<?>> childs = findLinks(beanClass);
        if (childs != null && !childs.isEmpty()) {
            childs.stream().filter(child -> !res.contains(child)).forEach(child -> {
                res.add(child);
                findAllLinks(res, child);
            });
        }
    }

    private Set<Class<?>> findLinks(Class<?> beanClass) {
        Set<Class<?>> res = new HashSet<>();

        Cache cache = beanClass.getAnnotation(Cache.class);
        if (cache != null && cache.links().length > 0) {
            res.addAll(Arrays.asList(cache.links()));
        }

        MetaInfoBean metaInfoBean = forBeanClass(beanClass);

        for (String propertyName : metaInfoBean.getPropertyNames()) {
            if (metaInfoBean.isPropertyAnnotationPresent(propertyName, Association.class)) {
                res.add(getAssociationClass(beanClass, propertyName));
            } else if (metaInfoBean.isPropertyAnnotationPresent(propertyName, Collection.class)) {
                res.add(getCollectionElementClass(beanClass, propertyName));
            }
        }
        return res;
    }

    /**
     * Add id property in propertyNames if empty
     *
     * @param beanClass     bean class
     * @param propertyNames property names
     * @return property names with id property if empty
     */
    public String[] addIdPropertyIfEmpty(Class<?> beanClass, String[] propertyNames) {
        if (propertyNames == null || propertyNames.length == 0) {
            String idPropertyName = getIdPropertyName(beanClass);
            if (idPropertyName == null) {
                return null;
            }
            return new String[] { idPropertyName };
        }
        return propertyNames;
    }

    /**
     * Validate bean class
     *
     * @param beanClass bean class
     */
    public void validateBean(Class<?> beanClass) throws ValidationBeanException {
        this.validatorBean.validateBean(beanClass);
    }
}
