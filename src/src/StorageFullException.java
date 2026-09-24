/**
 * Thrown when adding to a TaskStorage that already holds the maximum number
 * of tasks.
 *
 * @author Oscar, Abdullah, Vihaan
 * @version 2026.09.24
 */
public class StorageFullException
    extends Exception
{
    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception with a message.
     *
     * @param message
     *            the detail message
     */
    public StorageFullException(String message)
    {
        super(message);
    }
}
