package jsonANDxml.LocalException;

import org.apache.log4j.Logger;

public class ParameterException extends Exception{
    private String message;
    private StringBuffer messageB;
    public ParameterException(String errmessage)
    {
        message = errmessage;
        System.out.println("ParameterException occurs:(String): " + this.getClass() + " :: ");
    }
    public ParameterException(StringBuffer errmessage)
    {
        messageB = errmessage;
        System.out.println("ParameterException occurs:(StringBuffer): " + this.getClass() + " :: ");
    }

	public ParameterException(String errmessage, Logger theLog)
	{
        message = errmessage;
        theLog.error(message);
	}


    public ParameterException(StringBuffer errmessage, Logger theLog)
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