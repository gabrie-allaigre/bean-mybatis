package com.talanlabs.bean.mybatis.rsql.engine;

public interface ILikePolicy {

    /**
     * @return A like symbol default %
     */
    String getLikeSymbol();

    /**
     * @return Escape symbole default \
     */
    String getEscapeSymbol();

}
