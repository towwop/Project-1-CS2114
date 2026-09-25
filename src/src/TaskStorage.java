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
        taskArray = new Task[MAX_TASKS];
        size = 0;
        newTaskId = 1;
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
        if (size == MAX_TASKS)
        {
            throw new StorageFullException(
                "Storage is full (" + MAX_TASKS + " tasks)");
        }
        if (description == null || description.isBlank() || dueDate == null)
        {
            return null;
        }

        Task task = new Task(newTaskId, description, dueDate);
        taskArray[size] = task;
        size++;
        // Only bump the id after a successful add. It never goes down, so
        // ids are never reused, even after removals.
        newTaskId++;
        return task;
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
        if (size == MAX_TASKS)
        {
            throw new StorageFullException(
                "Storage is full (" + MAX_TASKS + " tasks)");
        }

        // Subtasks only go one level deep, so the parent must be a plain Task.
        Task parent = getTaskFromID(parentTaskId);
        if (parent == null || parent instanceof SubTask)
        {
            return null;
        }
        if (description == null || description.isBlank() || dueDate == null)
        {
            return null;
        }

        Task subTask =
            new SubTask(newTaskId, description, dueDate, parentTaskId);
        taskArray[size] = subTask;
        size++;
        newTaskId++;
        return subTask;
    }

    /**
     * Removes a task from storage.
     * If task is a parent, its subtasks are removed too.
     *
     * @param task
     *            the task to remove
     * @return false if task is null or not in storage, true otherwise
     */
    public boolean remove(Task task)
    {
        if (task == null)
        {
            return false;
        }

        // Look for this exact object (==), not just a matching id, so a Task
        // made outside storage isn't treated as stored.
        int index = -1;
        for (int i = 0; i < size; i++)
        {
            if (taskArray[i] == task)
            {
                index = i;
            }
        }
        if (index == -1)
        {
            return false;
        }

        removeAt(index);

        // Cascade: a plain Task may have subtasks, so remove every SubTask
        // whose parent id matches. A SubTask can't have children, so skip it.
        if (!(task instanceof SubTask))
        {
            int i = 0;
            while (i < size)
            {
                if (taskArray[i] instanceof SubTask
                    && ((SubTask)taskArray[i]).getParentTaskId() == task
                        .getId())
                {
                    // Don't advance i: removeAt slid the next task into
                    // slot i, and it still needs to be checked.
                    removeAt(i);
                }
                else
                {
                    i++;
                }
            }
        }
        return true;
    }

    /**
     * Changes a task's description.
     *
     * @param task
     *            the task to edit
     * @param newDescription
     *            the new description
     * @return false if task is null or not in storage, or newDescription is
     *         null or blank
     */
    public boolean editDescription(Task task, String newDescription)
    {
        // Same-object check: the task must be the one actually stored.
        if (task == null || getTaskFromID(task.getId()) != task)
        {
            return false;
        }
        if (newDescription == null || newDescription.isBlank())
        {
            return false;
        }
        return task.setDescription(newDescription);
    }

    /**
     * Changes a task's due date.
     *
     * @param task
     *            the task to edit
     * @param newDueDate
     *            the new due date
     * @return false if task is null or not in storage, or newDueDate is null
     */
    public boolean editDate(Task task, LocalDate newDueDate)
    {
        if (task == null || getTaskFromID(task.getId()) != task)
        {
            return false;
        }
        if (newDueDate == null)
        {
            return false;
        }
        return task.setDate(newDueDate);
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
        for (int i = 0; i < size; i++)
        {
            if (taskArray[i].getId() == id)
            {
                return taskArray[i];
            }
        }
        return null;
    }

    /**
     * Gets the number of stored tasks.
     *
     * @return the number of stored tasks
     */
    public int getSize()
    {
        return size;
    }

    /**
     * Gets the stored tasks.
     *
     * @return a new array of length getSize() holding the stored tasks
     */
    public Task[] getTasks()
    {
        // Return a copy, not taskArray itself. Otherwise a caller could put
        // nulls or gaps into our array and size would no longer be accurate.
        Task[] copy = new Task[size];
        for (int i = 0; i < size; i++)
        {
            copy[i] = taskArray[i];
        }
        return copy;
    }

    //~ Private Methods .......................................................

    /**
     * Removes the task at index and shifts everything after it one slot
     * left so there are no gaps.
     *
     * @param index
     *            the index to remove, between 0 and size - 1
     */
    private void removeAt(int index)
    {
        // Shift left: each later task moves down one slot, overwriting the
        // removed one.
        for (int i = index; i < size - 1; i++)
        {
            taskArray[i] = taskArray[i + 1];
        }
        // The last slot now holds a duplicate of the last task, so clear it.
        taskArray[size - 1] = null;
        size--;
    }
}
