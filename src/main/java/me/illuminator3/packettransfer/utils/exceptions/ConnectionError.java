package me.illuminator3.packettransfer.utils.exceptions;

public class ConnectionError
    extends RuntimeException
{
    public ConnectionError()
    {
        super();
    }

    public ConnectionError(String message)
    {
        super(message);
    }

    public ConnectionError(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ConnectionError(Throwable cause)
    {
        super(cause);
    }

    protected ConnectionError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}