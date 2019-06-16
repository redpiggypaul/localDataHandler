package jsonANDxml.LocalException;

import org.apache.log4j.Logger;

public class PageException extends Exception{
    private String message;
    private StringBuffer messageB;
    public PageException(String errmessage)
    {
        message = errmessage;
        System.out.println("PageException occurs :(String): " + this.getClass() + " :: ");
    }
    public PageException(StringBuffer errmessage)
    {
        messageB = errmessage;
        System.out.println("PageException occurs :(StringBuffer): " + this.getClass() + " :: ");
    }

	public PageException(String errmessage, Logger theLog)
	{
        message = errmessage;
        theLog.error(message);
	}


    public PageException(StringBuffer errmessage, Logger theLog)
    {
        messageB = errmessage;
        theLog.error(message);
    }

    public String getMessage()
    {
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