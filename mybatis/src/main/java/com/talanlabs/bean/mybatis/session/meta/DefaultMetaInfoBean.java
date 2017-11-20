package com.talanlabs.bean.mybatis.session.meta;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultMetaInfoBean extends MetaInfoBean {

    private Set<String> propertyNames;
    private Map<String, PropertyInfo> propertyInfoMap;

    public DefaultMetaInfoBean(Class<?> beanClass) {
        super(beanClass);

        init();
    }

    private void init() {
        Set<String> propertyNames = new HashSet<>();

        this.propertyInfoMap = new HashMap<>();

        List<Class<?>> contexts = new ArrayList<>();
        findAllClasses(contexts, beanClass);

        for (Class<?> clazz : contexts) {
            PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(clazz);
            if (pds != null && pds.length > 0) {
                for (PropertyDescriptor pd : pds) {
                    String propertyName = pd.getName();

                    if (propertyNames.contains(propertyName)) {
                        PropertyInfo propertyInfo = this.propertyInfoMap.get(propertyName);
                        propertyInfo.merge(pd.getWriteMethod(), pd.getReadMethod());
                    } else {
                        propertyNames.add(propertyName);
                        this.propertyInfoMap.put(propertyName, new PropertyInfo(propertyName, FieldUtils.getField(clazz, pd.getName(), true), pd.getWriteMethod(), pd.getReadMethod()));
                    }
                }
            }
        }

        Field[] fields = FieldUtils.getAllFields(beanClass);
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                String propertyName = field.getName();
                if (!propertyNames.contains(propertyName)) {
                    propertyNames.add(propertyName);
                    this.propertyInfoMap.put(propertyName, new PropertyInfo(propertyName, field, null, null));
                }
            }
        }

        this.propertyNames = Collections.unmodifiableSet(propertyNames);
    }

    private void findAllClasses(List<Class<?>> contexts, Class<?> clazz) {
        contexts.add(clazz);

        if (clazz.getSuperclass() != null && !contexts.contains(clazz.getSuperclass())) {
            findAllClasses(contexts, clazz.getSuperclass());
        }

        if (clazz.getInterfaces() != null) {
            Arrays.stream(clazz.getInterfaces()).filter(c -> !contexts.contains(c)).forEach(c -> findAllClasses(contexts, c));
        }
    }

    @Override
    public Set<String> getPropertyNames() {
        return this.propertyNames;
    }

    private void checkProperty(String propertyName) {
        if (!this.propertyNames.contains(propertyName)) {
            throw new IllegalArgumentException("Property " + propertyName + " not found in " + beanClass);
        }
    }

    @Override
    public void setPropertyValue(Object bean, String propertyName, Object value) {
        checkProperty(propertyName);

        this.propertyInfoMap.get(propertyName).setValue(bean, value);
    }

    @Override
    public Object getPropertyValue(Object bean, String propertyName) {
        checkProperty(propertyName);

        return this.propertyInfoMap.get(propertyName).getValue(bean);
    }

    @Override
    public Type getPropertyType(String propertyName) {
        checkProperty(propertyName);
        return this.propertyInfoMap.get(propertyName).type;
    }

    @Override
    public Class<?> getPropertyClass(String propertyName) {
        checkProperty(propertyName);
        return this.propertyInfoMap.get(propertyName).clazz;
    }

    @Override
    public boolean isPropertyAnnotationPresent(String propertyName, Class<? extends Annotation> annotationClass) {
        checkProperty(propertyName);
        return this.propertyInfoMap.get(propertyName).isAnnotationPresent(annotationClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <A extends Annotation> A getPropertyAnnotation(String propertyName, Class<A> annotationClass) {
        checkProperty(propertyName);
        return (A) this.propertyInfoMap.get(propertyName).getAnnotation(annotationClass);
    }

    private class PropertyInfo {

        private String propertyName;
        private Method writeMethod;
        private Method readMethod;
        private Field field;
        private Class<?> clazz;
        private Type type;
        private Map<Class<? extends Annotation>, Annotation> annotationMap;

        PropertyInfo(String propertyName, Field field, Method writeMethod, Method readMethod) {
            super();

            this.propertyName = propertyName;
            this.field = field;
            this.writeMethod = writeMethod;
            this.readMethod = readMethod;

            init();
        }

        private void init() {
            if (this.readMethod != null) {
                this.clazz = this.readMethod.getReturnType();
                this.type = this.readMethod.getGenericReturnType();
            } else if (this.writeMethod != null) {
                this.clazz = this.writeMethod.getParameterTypes()[0];
                this.type = this.writeMethod.getGenericParameterTypes()[0];
            } else if (this.field != null) {
                this.clazz = this.field.getType();
                this.type = this.field.getGenericType();
            }

            this.annotationMap = new HashMap<>();

            if (this.field != null && this.field.getDeclaredAnnotations() != null) {
                Arrays.stream(this.field.getDeclaredAnnotations()).forEach(a -> this.annotationMap.put(a.annotationType(), a));
            }
            if (this.writeMethod != null && this.writeMethod.getDeclaredAnnotations() != null) {
                Arrays.stream(this.writeMethod.getDeclaredAnnotations()).forEach(a -> this.annotationMap.put(a.annotationType(), a));
            }
            if (this.readMethod != null && this.readMethod.getDeclaredAnnotations() != null) {
                Arrays.stream(this.readMethod.getDeclaredAnnotations()).forEach(a -> this.annotationMap.put(a.annotationType(), a));
            }
        }

        public void merge(Method otherWriteMethod, Method otherReadMethod) {
            if (this.writeMethod == null) {
                this.writeMethod = otherWriteMethod;
            }
            if (otherWriteMethod != null && otherWriteMethod.getDeclaredAnnotations() != null) {
                Arrays.stream(otherWriteMethod.getDeclaredAnnotations()).filter(a -> !this.annotationMap.containsKey(a.annotationType())).forEach(a -> this.annotationMap.put(a.annotationType(), a));
            }
            if (this.readMethod == null) {
                this.readMethod = otherReadMethod;
            }
            if (otherReadMethod != null && otherReadMethod.getDeclaredAnnotations() != null) {
                Arrays.stream(otherReadMethod.getDeclaredAnnotations()).filter(a -> !this.annotationMap.containsKey(a.annotationType())).forEach(a -> this.annotationMap.put(a.annotationType(), a));
            }
        }

        private void checkBean(Object bean) {
            if (!beanClass.isInstance(bean)) {
                throw new IllegalArgumentException("Bean " + bean + " is not instance of " + beanClass);
            }
        }

        Object getValue(Object bean) {
            checkBean(bean);

            if (this.readMethod != null) {
                try {
                    return this.readMethod.invoke(bean);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new MetaBeanException("Failed to get value in " + propertyName + " for " + beanClass);
                }
            } else if (this.field != null) {
                try {
                    return this.field.get(bean);
                } catch (IllegalAccessException e) {
                    throw new MetaBeanException("Failed to get value in " + propertyName + " for " + beanClass);
                }
            } else {
                throw new IllegalArgumentException("Failed to get value in " + propertyName + " for " + beanClass);
            }
        }

        void setValue(Object bean, Object value) {
            checkBean(bean);

            if (this.writeMethod != null) {
                try {
                    this.writeMethod.invoke(bean, value);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new MetaBeanException("Failed to set value in " + propertyName + " for " + beanClass);
                }
            } else if (this.field != null) {
                try {
                    this.field.set(bean, value);
                } catch (IllegalAccessException e) {
                    throw new MetaBeanException("Failed to set value in " + propertyName + " for " + beanClass);
                }
            } else {
                throw new IllegalArgumentException("Failed to set value in " + propertyName + " for " + beanClass);
            }

        }

        boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
            return this.annotationMap.containsKey(annotationClass);
        }

        @SuppressWarnings("unchecked")
        <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
            return (A) this.annotationMap.get(annotationClass);
        }
    }
}
