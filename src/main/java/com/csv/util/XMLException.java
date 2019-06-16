package com.csv.util;

import org.apache.log4j.Logger;

import java.io.IOException;

public class XMLException extends IOException {
    private String message;
    private StringBuffer messageB;

    public XMLException(String errmessage) {
        message = errmessage;
        System.out.println("XMLException occurs:(String): " + this.getClass() + " :: ");
    }

    public XMLException(StringBuffer errmessage) {
        messageB = errmessage;
        System.out.println("XMLException occurs:(StringBuffer): " + this.getClass() + " :: ");
    }

    public XMLException(String errmessage, Logger theLog) {
        message = errmessage;
        theLog.error(message);
    }


    public XMLException(StringBuffer errmessage, Logger theLog) {
        messageB = errmessage;
        theLog.error(message);
    }

    public String getMessage() {
        return message;
    }

    public void getMessage(Throwable cause, Logger theLogger) {
        message = cause.toString();
        theLogger.error(message);
    }

    public void getClassError(String cause, Logger theLogger) {
        message = cause;
        theLogger.error(message);
    }

}