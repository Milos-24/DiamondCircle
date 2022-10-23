package exception;

public class DuplicateException extends Exception{

    public DuplicateException()
    {
        super("Username already exists!");
    }

    public DuplicateException(String msg)
    {
        super(msg);
    }
}
