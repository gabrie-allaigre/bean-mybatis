package com.talanlabs.bean.mybatis.session.meta;

public class MetaBeanException extends RuntimeException {

    public MetaBeanException() {
    }

    public MetaBeanException(String message) {
        super(message);
    }

    public MetaBeanException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetaBeanException(Throwable cause) {
        super(cause);
    }
}
