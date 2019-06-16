package jsonANDxml.LocalException;

import org.apache.log4j.Logger;

public class PageTitleException extends Exception{
    private String message;
    private StringBuffer messageB;
    public PageTitleException(String errmessage)
    {
        message = errmessage;
        System.out.println("PageException occurs :(String): " + this.getClass() + " :: ");
    }
    public PageTitleException(StringBuffer errmessage)
    {
        messageB = errmessage;
        System.out.println("PageException occurs :(StringBuffer): " + this.getClass() + " :: ");
    }

	public PageTitleException(String errmessage, Logger theLog)
	{
        message = errmessage;
        theLog.error(message);
	}


    public PageTitleException(StringBuffer errmessage, Logger theLog)
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