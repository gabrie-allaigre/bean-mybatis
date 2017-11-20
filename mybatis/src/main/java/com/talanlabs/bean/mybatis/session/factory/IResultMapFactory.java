package com.talanlabs.bean.mybatis.session.factory;

import org.apache.ibatis.mapping.ResultMap;

public interface IResultMapFactory {

    /**
     * Accept key
     *
     * @param key key at verify
     * @return true or false
     */
    boolean acceptKey(String key);

    /**
     * Create result map for key
     *
     * @param key a valid key
     * @return ResultMap
     */
    ResultMap createResultMap(String key);

}
