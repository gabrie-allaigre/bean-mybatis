package com.talanlabs.bean.mybatis.helper;

import com.talanlabs.bean.mybatis.factory.IdFactory;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.sql.Statement;

public class IdKeyGenerator implements KeyGenerator {

    private final String idPropertyName;

    public IdKeyGenerator(Configuration configuration, String id, Class<?> beanClass, String idPropertyName) {
        super();

        this.idPropertyName = idPropertyName;
    }

    @Override
    public void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
        try {
            BeanUtils.setProperty(parameter, idPropertyName, IdFactory.getInstance().newId());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new KeyGeneratorException("Failed to set id in " + idPropertyName + " for " + parameter, e);
        }
    }

    @Override
    public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
        // Nothing
    }
}
