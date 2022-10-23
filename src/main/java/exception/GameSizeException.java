package exception;

public class GameSizeException extends Exception{
    public GameSizeException()
    {
        super("Please select game size and number of players!");
    }

    public GameSizeException(String msg)
    {
        super(msg);
    }
}
