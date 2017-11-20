package com.talanlabs.bean.mybatis.session.meta;

import com.talanlabs.bean.mybatis.annotation.Association;
import com.talanlabs.bean.mybatis.annotation.Cache;
import com.talanlabs.bean.mybatis.annotation.Canceled;
import com.talanlabs.bean.mybatis.annotation.Collection;
import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Entity;
import com.talanlabs.bean.mybatis.annotation.JoinTable;
import com.talanlabs.bean.mybatis.annotation.NlsColumn;
import com.talanlabs.bean.mybatis.annotation.OrderBy;
import com.talanlabs.bean.mybatis.annotation.Version;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class DefaultValidatorBean implements ValidatorBean {

    private static final ValidationBeanException NULL_VALIDATION_BEAN_EXCEPTION = new ValidationBeanException();

    private final BeanConfiguration beanConfiguration;

    private final com.google.common.cache.Cache<Class<?>, ValidationBeanException> validateBeanMap;

    public DefaultValidatorBean(BeanConfiguration beanConfiguration) {
        super();

        this.beanConfiguration = beanConfiguration;

        this.validateBeanMap = com.google.common.cache.CacheBuilder.newBuilder().concurrencyLevel(4).build();
    }

    @Override
    public void validateBean(Class<?> beanClass) throws ValidationBeanException {
        validateBean(beanClass, new HashSet<>());
    }

    private void validateBean(Class<?> beanClass, Set<Class<?>> dejaVues) throws ValidationBeanException {
        MetaBean metaBean = beanConfiguration.getMetaBean();
        
        if (dejaVues.contains(beanClass)) {
            return;
        }
        dejaVues.add(beanClass);

        try {
            ValidationBeanException exception = this.validateBeanMap.get(beanClass, () -> {
                try {
                    validateEntity(beanClass);

                    if (beanClass.isAnnotationPresent(Cache.class)) {
                        validateCache(beanClass, dejaVues);
                    }

                    MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);
                    for (String propertyName : metaInfoBean.getPropertyNames()) {
                        validateProperty(beanClass, propertyName, dejaVues);
                    }

                    return NULL_VALIDATION_BEAN_EXCEPTION;
                } catch (ValidationBeanException e) {
                    return e;
                } catch (Exception e) {
                    return new ValidationBeanException("Failed to validate Bean=" + beanClass, e);
                }
            });
            if (exception != NULL_VALIDATION_BEAN_EXCEPTION) {
                throw exception;
            }
        } catch (ExecutionException e) {
            throw new ValidationBeanException("Failed to validate Bean=" + beanClass);
        }
    }

    private void validateProperty(Class<?> beanClass, String propertyName, Set<Class<?>> dejaVues) throws ValidationBeanException {
        MetaBean metaBean = beanConfiguration.getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

        if (metaInfoBean.isPropertyAnnotationPresent(propertyName, Version.class)) {
            validateVersion(beanClass, propertyName);
        }
        if (metaInfoBean.isPropertyAnnotationPresent(propertyName, Canceled.class)) {
            validateCanceled(beanClass, propertyName);
        }

        int i = 0;
        if (metaInfoBean.isPropertyAnnotationPresent(propertyName, Column.class)) {
            validateColumn(beanClass, propertyName);
            i++;
        }
        if (metaInfoBean.isPropertyAnnotationPresent(propertyName, NlsColumn.class)) {
            validateNlsColumn(beanClass, propertyName);
            i++;
        }
        if (metaInfoBean.isPropertyAnnotationPresent(propertyName, Association.class)) {
            validateAssociation(beanClass, propertyName, dejaVues);
            i++;
        }
        if (metaInfoBean.isPropertyAnnotationPresent(propertyName, Collection.class)) {
            validateCollection(beanClass, propertyName, dejaVues);
            i++;
        }
        if (i > 1) {
            throw new ValidationBeanException("Error for Bean=" + beanClass + " with property=" + propertyName + " multi annotation Column, NlsColumn, Assocation, Collection");
        }
    }

    private void validateEntity(Class<?> beanClass) throws ValidationBeanException {
        if (!beanClass.isAnnotationPresent(Entity.class)) {
            throw new ValidationBeanException("Bean=" + beanClass + " is not entity");
        }
        Entity entity = beanClass.getAnnotation(Entity.class);
        if (StringUtils.isBlank(entity.name())) {
            throw new ValidationBeanException("Entity for Bean=" + beanClass + " name is empty");
        }
    }

    private void validateCache(Class<?> beanClass, Set<Class<?>> dejaVues) throws ValidationBeanException {
        Cache cache = beanClass.getAnnotation(Cache.class);
        for(Class<?> clazz : cache.links()) {
            validateBean(clazz, dejaVues);
        }
    }

    private void validateVersion(Class<?> beanClass, String propertyName) throws ValidationBeanException {
        MetaBean metaBean = beanConfiguration.getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

        if (!metaInfoBean.isPropertyAnnotationPresent(propertyName, Column.class)) {
            throw new ValidationBeanException("Version must be with Column");
        }

        Class<?> javaType = metaBean.getColumnClass(beanClass, propertyName);
        if (javaType != int.class && javaType != Integer.class && javaType != long.class && javaType != Long.class) {
            throw new ValidationBeanException("Version must be int, Integer, long or Long");
        }
    }

    private void validateCanceled(Class<?> beanClass, String propertyName) throws ValidationBeanException {
        MetaBean metaBean = beanConfiguration.getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

        if (!metaInfoBean.isPropertyAnnotationPresent(propertyName, Column.class)) {
            throw new ValidationBeanException("Version must be with Column");
        }

        Class<?> javaType = metaBean.getColumnClass(beanClass, propertyName);
        if (javaType != boolean.class && javaType != Boolean.class) {
            throw new ValidationBeanException("Canceled must be boolean or Boolean");
        }
    }

    private void validateColumn(Class<?> beanClass, String propertyName) throws ValidationBeanException {
        MetaBean metaBean = beanConfiguration.getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

        if (propertyName == null) {
            throw new ValidationBeanException("Not exists property for Bean=" + beanClass);
        }
        if (!metaInfoBean.getPropertyNames().contains(propertyName)) {
            throw new ValidationBeanException("Not exists property for Bean=" + beanClass + " with property=" + propertyName);
        }
        if (!metaInfoBean.isPropertyAnnotationPresent(propertyName, Column.class)) {
            throw new ValidationBeanException("Not present annotation Column for Bean=" + beanClass + " with property=" + propertyName);
        }
        Column column = metaInfoBean.getPropertyAnnotation(propertyName, Column.class);
        if (StringUtils.isBlank(column.name())) {
            throw new ValidationBeanException("Not name in Column for Bean=" + beanClass + " with property=" + propertyName);
        }
    }

    private void validateColumns(Class<?> beanClass, String[] properties) throws ValidationBeanException {
        if (properties == null || properties.length == 0) {
            throw new ValidationBeanException("property is null or empty");
        }

        for (String propertyName : properties) {
            validateColumn(beanClass, propertyName);
        }
    }

    private void validateNlsColumn(Class<?> beanClass, String propertyName) throws ValidationBeanException {
        MetaBean metaBean = beanConfiguration.getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

        if (propertyName == null) {
            throw new ValidationBeanException("Not exists property for Bean=" + beanClass);
        }
        if (!metaInfoBean.getPropertyNames().contains(propertyName)) {
            throw new ValidationBeanException("Not exists property for Bean=" + beanClass + " with property=" + propertyName);
        }
        if (!metaInfoBean.isPropertyAnnotationPresent(propertyName, NlsColumn.class)) {
            throw new ValidationBeanException("Not present annotation NlsColumn for Bean=" + beanClass + " with property=" + propertyName);
        }
        NlsColumn nlsColumn = metaInfoBean.getPropertyAnnotation(propertyName, NlsColumn.class);
        if (StringUtils.isBlank(nlsColumn.name())) {
            throw new ValidationBeanException("Not name in NlsColumn for Bean=" + beanClass + " with property=" + propertyName);
        }

        if (nlsColumn.propertySource().length == 0 && metaBean.getIdPropertyName(beanClass) == null) {
            throw new ValidationBeanException("Not id in NlsColumn for Bean=" + beanClass + " with property=" + propertyName);
        }
        if (nlsColumn.propertySource().length > 0) {
            validateColumns(beanClass, nlsColumn.propertySource());
        }
    }

    private void validateAssociation(Class<?> beanClass, String propertyName, Set<Class<?>> dejaVues) throws ValidationBeanException {
        MetaBean metaBean = beanConfiguration.getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

        if (propertyName == null) {
            throw new ValidationBeanException("Not exists property for Bean=" + beanClass);
        }
        if (!metaInfoBean.getPropertyNames().contains(propertyName)) {
            throw new ValidationBeanException("Not exists property for Bean=" + beanClass + " with property=" + propertyName);
        }
        if (!metaInfoBean.isPropertyAnnotationPresent(propertyName, Association.class)) {
            throw new ValidationBeanException("Not present annotation Association for Bean=" + beanClass + " with property=" + propertyName);
        }
        Association association = metaInfoBean.getPropertyAnnotation(propertyName, Association.class);

        Class<?> javaType = metaBean.getAssociationClass(beanClass, propertyName);
        if (javaType == null) {
            throw new ValidationBeanException("Not javatype in Association for Bean=" + beanClass + " with property=" + propertyName);
        }
        validateBean(javaType, dejaVues);

        if (association.propertySource().length == 0) {
            throw new ValidationBeanException("Not property in source Association for Bean=" + beanClass + " with property=" + propertyName);
        }
        if (association.propertySource().length > 0) {
            validateColumns(beanClass, association.propertySource());
        }

        if (association.propertyTarget().length == 0) {
            if (metaBean.getIdPropertyName(javaType) == null) {
                throw new ValidationBeanException("Not id in target Association for Bean=" + javaType + " with property=" + propertyName);
            }
            validateColumn(javaType, metaBean.getIdPropertyName(javaType));
        } else {
            validateColumns(javaType, association.propertyTarget());
        }

        if (association.joinTable().length > 0) {
            validateJoinTables(beanClass, propertyName, association.joinTable(), association.propertySource(), metaBean.addIdPropertyIfEmpty(javaType, association.propertyTarget()));
        } else {
            if (association.propertyTarget().length == 0) {
                if (association.propertySource().length != 1) {
                    throw new ValidationBeanException("Not same length source and target in Association for Bean=" + beanClass + " with property=" + propertyName);
                }
            } else if (association.propertySource().length != association.propertyTarget().length) {
                throw new ValidationBeanException("Not same length source and target in Association for Bean=" + beanClass + " with property=" + propertyName);
            }
        }
    }

    private void validateCollection(Class<?> beanClass, String propertyName, Set<Class<?>> dejaVues) throws ValidationBeanException {
        MetaBean metaBean = beanConfiguration.getMetaBean();
        MetaInfoBean metaInfoBean = metaBean.forBeanClass(beanClass);

        if (propertyName == null) {
            throw new ValidationBeanException("Not exists property for Bean=" + beanClass);
        }
        if (!metaInfoBean.getPropertyNames().contains(propertyName)) {
            throw new ValidationBeanException("Not exists property for Bean=" + beanClass + " with property=" + propertyName);
        }
        if (!metaInfoBean.isPropertyAnnotationPresent(propertyName, Collection.class)) {
            throw new ValidationBeanException("Not present annotation Collection for Bean=" + beanClass + " with property=" + propertyName);
        }
        Collection collection = metaInfoBean.getPropertyAnnotation(propertyName, Collection.class);

        Class<?> javaType = metaBean.getCollectionClass(beanClass, propertyName);
        if (javaType == null) {
            throw new ValidationBeanException("Not javatype in Collection for Bean=" + beanClass + " with property=" + propertyName);
        }
        if (!java.util.Collection.class.isAssignableFrom(javaType)) {
            throw new ValidationBeanException("Javatype is not collection in Collection for Bean=" + beanClass + " with property=" + propertyName);
        }

        Class<?> ofType = metaBean.getCollectionElementClass(beanClass, propertyName);
        if (ofType == null) {
            throw new ValidationBeanException("Not oftype in Collection for Bean=" + beanClass + " with property=" + propertyName);
        }
        validateBean(ofType, dejaVues);

        if (collection.propertySource().length == 0) {
            if (metaBean.getIdPropertyName(beanClass) == null) {
                throw new ValidationBeanException("Not id in source Association for Bean=" + beanClass + " with property=" + propertyName);
            }
            validateColumn(beanClass, metaBean.getIdPropertyName(beanClass));
        } else {
            validateColumns(beanClass, collection.propertySource());
        }

        if (collection.propertyTarget().length == 0) {
            throw new ValidationBeanException("Not property in source Collection for Bean=" + beanClass + " with property=" + propertyName);
        }
        if (collection.propertyTarget().length > 0) {
            validateColumns(ofType, collection.propertyTarget());
        }

        if (collection.joinTable().length > 0) {
            validateJoinTables(beanClass, propertyName, collection.joinTable(), metaBean.addIdPropertyIfEmpty(beanClass, collection.propertySource()), collection.propertyTarget());
        } else {
            if (collection.propertySource().length == 0) {
                if (collection.propertyTarget().length != 1) {
                    throw new ValidationBeanException("Not same length source and target in Collection for Bean=" + beanClass + " with property=" + propertyName);
                }
            } else if (collection.propertySource().length != collection.propertyTarget().length) {
                throw new ValidationBeanException("Not same length source and target in Collection for Bean=" + beanClass + " with property=" + propertyName);
            }
        }

        if (collection.orderBy().length > 0) {
            validateOrderBies(ofType, collection.orderBy());
        }
    }

    private void validateJoinTables(Class<?> beanClass, String propertyName, JoinTable[] joinTables, String[] propertySource, String[] propertyTarget) throws ValidationBeanException {
        int i = 0;
        for (JoinTable joinTable : joinTables) {
            if (StringUtils.isBlank(joinTable.name())) {
                throw new ValidationBeanException("JoinTable is empty for Bean=" + beanClass + " with property=" + propertyName);
            }
            if (joinTable.left().length == 0) {
                throw new ValidationBeanException("JoinTable join=" + joinTable.name() + " left is empty for Bean=" + beanClass + " with property=" + propertyName);
            }
            if (joinTable.right().length == 0) {
                throw new ValidationBeanException("JoinTable join=" + joinTable.name() + " right is empty for Bean=" + beanClass + " with property=" + propertyName);
            }

            if (i == 0) {
                if (propertySource.length != joinTable.left().length) {
                    throw new ValidationBeanException("JoinTable join=" + joinTable.name() + " left different size propertySource for Bean=" + beanClass + " with property=" + propertyName);
                }
            }
            if (i == joinTables.length - 1) {
                if (propertyTarget.length != joinTable.right().length) {
                    throw new ValidationBeanException("JoinTable join=" + joinTable.name() + " right different size propertyTarget for Bean=" + beanClass + " with property=" + propertyName);
                }
            }
            if (i > 0 && i < joinTables.length) {
                if (joinTables[i - 1].right().length != joinTable.left().length) {
                    throw new ValidationBeanException("JoinTable join=" + joinTable.name() + " left different size with previous join for Bean=" + beanClass + " with property=" + propertyName);
                }
            }

            i++;
        }
    }

    private void validateOrderBies(Class<?> beanClass, OrderBy[] orderBies) throws ValidationBeanException {
        if (orderBies != null && orderBies.length > 0) {
            for (OrderBy orderBy : orderBies) {
                validateColumn(beanClass, orderBy.value());
            }
        }
    }
}
