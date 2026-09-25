import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        this.id = id;
        this.description = description;
        this.dueDate = dueDate;
    }

    //~ Public Methods ........................................................

    /**
     * Gets the task id.
     *
     * @return the task id
     */
    public int getId()
    {
        return id;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription()
    {
        return description;
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
        if (description == null || description.isBlank())
        {
            return false;
        }
        this.description = description;
        return true;
    }

    /**
     * Gets the due date.
     *
     * @return the due date
     */
    public LocalDate getDate()
    {
        return dueDate;
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
        if (newDueDate == null)
        {
            return false;
        }
        this.dueDate = newDueDate;
        return true;
    }

    /**
     * Returns a one-line display string for this task.
     *
     * @return the display string
     */
    @Override
    public String toString()
    {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(
            "MM/dd/yyyy");
        return "[" + id + "]: " + description + " (Due: "
            + dueDate.format(dateFormat) + ")";
    }
}