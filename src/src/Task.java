import java.time.LocalDate;

/**
 * A single task with a unique id, a description, and a due date. Ids are
 * assigned by TaskStorage. Never reads user input.
 *
 * @author Oscar, Abdullah, Vihaan
 * @version 2026.09.24
 */
public class Task
{
    //~ Fields ................................................................

    private int id;
    private String description;
    private LocalDate dueDate;

    //~ Constructors ..........................................................

    /**
     * Creates a task. Id uniqueness is guaranteed by TaskStorage.
     *
     * @param id
     *            the unique task id
     * @param description
     *            the task description
     * @param dueDate
     *            the due date
     */
    public Task(int id, String description, LocalDate dueDate)
    {
        // TODO: store id, description, dueDate
        // not implemented
    }

    //~ Public Methods ........................................................

    /**
     * Gets the task id.
     *
     * @return the task id
     */
    public int getId()
    {
        // TODO: return id
        return 0; // not implemented
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription()
    {
        // TODO: return description
        return null; // not implemented
    }

    /**
     * Sets the description.
     *
     * @param description
     *            the new description
     * @return false if description is null or empty, true otherwise
     */
    public boolean setDescription(String description)
    {
        // TODO: reject null/empty, otherwise set
        return false; // not implemented
    }

    /**
     * Gets the due date.
     *
     * @return the due date
     */
    public LocalDate getDate()
    {
        // TODO: return dueDate
        return null; // not implemented
    }

    /**
     * Sets the due date.
     *
     * @param newDueDate
     *            the new due date
     * @return false if newDueDate is null, true otherwise
     */
    public boolean setDate(LocalDate newDueDate)
    {
        // TODO: reject null, otherwise set
        return false; // not implemented
    }

    /**
     * Returns a one-line display string for this task.
     *
     * @return the display string
     */
    @Override
    public String toString()
    {
        // TODO: format id, description, due date
        return null; // not implemented
    }
}
