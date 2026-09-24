import java.time.LocalDate;

/**
 * Stores up to 25 Tasks and SubTasks in a single array and assigns their
 * ids. Ids are never reused.
 *
 * @author Oscar, Abdullah, Vihaan
 * @version 2026.09.24
 */
public class TaskStorage
{
    //~ Fields ................................................................

    /** Maximum number of tasks storage can hold. */
    public static final int MAX_TASKS = 25;

    private Task[] taskArray;
    private int size;
    private int newTaskId;

    //~ Constructors ..........................................................

    /**
     * Creates an empty storage with room for MAX_TASKS tasks.
     */
    public TaskStorage()
    {
        // TODO: allocate taskArray, init size and newTaskId
    }

    //~ Public Methods ........................................................

    /**
     * Creates a task with the next id and adds it.
     *
     * @param description
     *            the task description
     * @param dueDate
     *            the due date
     * @return the created task, or null if description or dueDate is invalid
     * @throws StorageFullException
     *             if storage already holds MAX_TASKS tasks
     */
    public Task add(String description, LocalDate dueDate)
        throws StorageFullException
    {
        // TODO: check full, build Task with newTaskId, store it
        return null;
    }

    /**
     * Creates a subtask with the next id and adds it.
     *
     * @param description
     *            the task description
     * @param dueDate
     *            the due date
     * @param parentTaskId
     *            the id of the parent task
     * @return the created subtask, or null if the parent does not exist, the
     *         parent is a subtask, or the description or dueDate is invalid
     * @throws StorageFullException
     *             if storage already holds MAX_TASKS tasks
     */
    public Task addSubTask(
        String description,
        LocalDate dueDate,
        int parentTaskId)
        throws StorageFullException
    {
        // TODO: check full, validate parent, build SubTask, store it
        return null;
    }

    /**
     * Removes a task from storage.
     *
     * @param task
     *            the task to remove
     * @return false if task is null or not in storage, true otherwise
     */
    public boolean remove(Task task)
    {
        // TODO: find, remove, shift left, decrement size
        // TODO: decide what happens to subtasks of a removed parent
        return false;
    }

    /**
     * Changes a task's description.
     *
     * @param task
     *            the task to edit
     * @param newDescription
     *            the new description
     * @return false if task is null or newDescription is empty
     */
    public boolean editDescription(Task task, String newDescription)
    {
        // TODO: delegate to task.setDescription
        return false;
    }

    /**
     * Changes a task's due date.
     *
     * @param task
     *            the task to edit
     * @param newDueDate
     *            the new due date
     * @return false if task or newDueDate is null
     */
    public boolean editDate(Task task, LocalDate newDueDate)
    {
        // TODO: delegate to task.setDate
        return false;
    }

    /**
     * Finds a task by id.
     *
     * @param id
     *            the id to look up
     * @return the matching task, or null if none exists
     */
    public Task getTaskFromID(int id)
    {
        // TODO: linear search on getId()
        return null;
    }

    /**
     * Gets the number of stored tasks.
     *
     * @return the number of stored tasks
     */
    public int getSize()
    {
        // TODO: return size
        return 0;
    }

    /**
     * Gets the stored tasks.
     *
     * @return a new array of length getSize() holding the stored tasks
     */
    public Task[] getTasks()
    {
        // TODO: return a copy of the occupied part of taskArray
        return null;
    }
}
