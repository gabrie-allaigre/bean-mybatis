package com.talanlabs.bean.mybatis.session.defaults;

import com.talanlabs.bean.mybatis.session.factory.ITypeHandlerFactory;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

public class DefaultTypeHandlerFactory implements ITypeHandlerFactory {

    @Override
    public <F extends TypeHandler<?>> F create(Class<F> typeHandlerClass) {
        try {
            return typeHandlerClass.newInstance();
        } catch (Exception e) {
            throw new TypeException("Failed to create instance for typeHandler " + typeHandlerClass, e);
        }
    }
}
