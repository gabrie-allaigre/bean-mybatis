package com.talanlabs.bean.mybatis.session.factory;

import org.apache.ibatis.mapping.MappedStatement;

public interface IMappedStatementFactory {

    /**
     * Accept key
     *
     * @param key key at verify
     * @return true or false
     */
    boolean acceptKey(String key);

    /**
     * Create Mapped statement
     *
     * @param key a valid key
     * @return Mapped statement
     */
    MappedStatement createMappedStatement(String key);

}
