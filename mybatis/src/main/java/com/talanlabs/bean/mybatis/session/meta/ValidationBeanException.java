package com.talanlabs.bean.mybatis.session.meta;

public class ValidationBeanException extends Exception {

    public ValidationBeanException() {
    }

    public ValidationBeanException(String message) {
        super(message);
    }

    public ValidationBeanException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationBeanException(Throwable cause) {
        super(cause);
    }
}
