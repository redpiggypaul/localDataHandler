package jsonANDxml.LocalException;

import org.apache.log4j.Logger;

public class SCPandaException extends Exception{
    private String message;
    private StringBuffer messageB;
    public SCPandaException(String errmessage)
    {
        message = errmessage;
        System.out.println("SCException occurs:(String): " + this.getClass() + " :: ");
    }
    public SCPandaException(StringBuffer errmessage)
    {
        messageB = errmessage;
        System.out.println("SCException occurs:(StringBuffer): " + this.getClass() + " :: ");
    }

	public SCPandaException(String errmessage, Logger theLog)
	{
        message = errmessage;
        theLog.error(message);
	}


    public SCPandaException(StringBuffer errmessage, Logger theLog)
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