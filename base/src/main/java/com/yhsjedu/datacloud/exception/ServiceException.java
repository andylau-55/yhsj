package com.yhsjedu.datacloud.exception;

/**
 * Service层异常类. 继承自RuntimeException,在函数中抛出会触发Spring的事务.
 */
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 4146548369811741865L;
    private String item = null;

    public String getItem() {
        return item;
    }

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String item, String message) {
        super(message);
        this.item = item;
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
