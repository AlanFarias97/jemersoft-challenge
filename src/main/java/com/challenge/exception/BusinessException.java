package com.challenge.exception;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Locale;

public class BusinessException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    private final String exceptionCode;
    private final Locale locale;
    private final Serializable[] data;


    public BusinessException(String exceptionCode, String message, Locale locale, Serializable... data) {
        super(message);
        this.exceptionCode = exceptionCode;
        this.locale = locale;
        this.data = data;
    }

    public BusinessException(GeneralMessageCode messageCode) {
        this((GeneralMessageCode)messageCode, (Locale)null, (Serializable[])null);
    }

    public BusinessException(GeneralMessageCode messageCode, Locale locale, Serializable... data) {
        super(messageCode.getMessage());
        this.exceptionCode = messageCode.getExceptionCode();
        this.locale = locale;
        this.data = data;
    }

    public String toString() {
        return MessageFormat.format(super.toString(), (Object[])this.data);
    }

    public String getMessage() {
        return MessageFormat.format(super.getMessage(), (Object[])this.data);
    }

}
