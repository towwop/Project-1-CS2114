import java.time.LocalDate;

/**
 * A task that belongs to a parent task. The parent must exist and cannot
 * itself be a subtask (enforced by TaskStorage). Never reads user input.
 *
 * @author Oscar, Abdullah, Vihaan
 * @version 2026.09.24
 */
public class SubTask
    extends Task
{
    //~ Fields ................................................................

    private int parentTaskId;

    //~ Constructors ..........................................................

    /**
     * Creates a subtask.
     *
     * @param id
     *            the unique task id
     * @param description
     *            the task description
     * @param dueDate
     *            the due date
     * @param parentTaskId
     *            the id of the parent task
     */
    public SubTask(
        int id,
        String description,
        LocalDate dueDate,
        int parentTaskId)
    {
        super(id, description, dueDate);
        // TODO: store parentTaskId
        // not implemented
    }

    //~ Public Methods ........................................................

    /**
     * Gets the parent task id.
     *
     * @return the parent task id
     */
    public int getParentTaskId()
    {
        // TODO: return parentTaskId
        return 0; // not implemented
    }

    /**
     * Returns a one-line display string for this subtask, including its
     * parent id.
     *
     * @return the display string
     */
    @Override
    public String toString()
    {
        // TODO: format like Task.toString() plus parent id
        return null; // not implemented
    }
}
