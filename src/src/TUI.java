import java.util.Scanner;

/**
 * Handles all terminal display and input. This is the only class that reads
 * user input, validates it, and re-prompts. It owns no task data.
 *
 * @author Oscar, Abdullah, Vihaan
 * @version 2026.09.24
 */
public class TUI
{
    //~ Fields ................................................................

    private TaskStorage storage;
    private Scanner scanner;

    //~ Constructors ..........................................................

    /**
     * Creates a TUI backed by the given storage and input scanner.
     *
     * @param storage
     *            the task storage to read and modify
     * @param scanner
     *            the scanner to read user input from
     */
    public TUI(TaskStorage storage, Scanner scanner)
    {
        // TODO: store storage and scanner
        // not implemented
    }

    //~ Public Methods ........................................................

    /**
     * Prints every task in storage using each task's toString().
     */
    public void printTaskList()
    {
        // TODO: print storage.getTasks() without instanceof checks
        // not implemented
    }

    /**
     * Asks the user for an action (add, add subtask, edit, remove, quit) and
     * calls the matching handler.
     *
     * @return false if the user chose to quit, true otherwise
     */
    public boolean handleUserAction()
    {
        // TODO: read action and dispatch to handler methods
        return false; // not implemented
    }

    /**
     * Asks for a description and due date (MM/DD/YYYY), then adds the task
     * to storage.
     */
    public void handleAddTask()
    {
        // TODO: prompt, validate, re-prompt, call storage.add, catch
        // StorageFullException
        // not implemented
    }

    /**
     * Asks for a description, due date, and parent task id, then adds the
     * subtask to storage. The parent cannot be a subtask.
     */
    public void handleAddSubtask()
    {
        // TODO: prompt, validate, re-prompt, call storage.addSubTask
        // not implemented
    }

    /**
     * Asks for a task id and removes that task. Re-prompts on an invalid id.
     */
    public void handleRemoveTask()
    {
        // TODO: prompt for id, call storage.getTaskFromID and storage.remove
        // not implemented
    }

    /**
     * Asks for a task id, then a new description and date. A blank
     * description or date leaves that field unchanged.
     */
    public void handleEditTask()
    {
        // TODO: prompt, call storage.editDescription and storage.editDate
        // not implemented
    }
}
