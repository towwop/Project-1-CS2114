import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Handles all terminal display and input. This is the only class that reads
 * user input, validates it, and re-prompts. It owns no task data.
 *
 * @author Oscar, Abdullah, Vihaan
 * @version 2026.09.24
 */
public class TUI {
    // ~ Fields ................................................................

    private TaskStorage storage;
    private Scanner scanner;

    // ~ Constructors ..........................................................

    /**
     * Creates a TUI backed by the given storage and input scanner.
     *
     * @param storage
     *            the task storage to read and modify
     * @param scanner
     *            the scanner to read user input from
     */
    public TUI(TaskStorage storage, Scanner scanner) {
        // TODO: store storage and scanner
        // not implemented
    }

    // ~ Public Methods ........................................................


    /**
     * Prints every task in storage using each task's toString().
     */
    public void printTaskList() {
        Task[] tasks = storage.getTasks();

        for (int i = 0; i < tasks.length; i++) {
            if (tasks[i] != null) {
                printTask(tasks[i]);
            }
        }
    }


    /**
     * Asks the user for an action (add, add subtask, edit, remove, quit) and
     * calls the matching handler.
     *
     * @return false if the user chose to quit, true otherwise
     */
    public boolean handleUserAction() {
        System.out.print(
            "Would you like to (a)dd, (e)dit, or (r)emove a task: ");
        
        String input = scanner.nextLine().trim();
        
        if (input.length() == 0) {
            return true;
        }
        
        char c = Character.toLowerCase(input.charAt(0));
        
        if (c == 'a') {
            handleAddTask();
        } else if (c == 'e') {
            handleEditTask();
        } else if (c == 'r') {
            handleRemoveTask();
        }

        return false;
    }


    /**
     * Asks for a description and due date (MM/DD/YYYY), then adds the task
     * to storage.
     */
    public void handleAddTask() {
        // TODO: prompt, validate, re-prompt, call storage.add, catch
        // StorageFullException
        // not implemented
    }


    /**
     * Asks for a description, due date, and parent task id, then adds the
     * subtask to storage. The parent cannot be a subtask.
     */
    public void handleAddSubtask() {
        // TODO: prompt, validate, re-prompt, call storage.addSubTask
        // not implemented
    }


    /**
     * Asks for a task id and removes that task. Re-prompts on an invalid id.
     */
    public void handleRemoveTask() {
        // TODO: prompt for id, call storage.getTaskFromID and storage.remove
        // not implemented
    }


    /**
     * Asks for a task id, then a new description and date. A blank
     * description or date leaves that field unchanged.
     */
    public void handleEditTask() {
        // TODO: prompt, call storage.editDescription and storage.editDate
        // not implemented
    }


    private void printTask(Task task) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(
            "(MM/dd/yyyy)");

        String idString = "[" + task.getId() + "]: ";
        String dateString = task.getDate().format(dateFormat);

        System.out.println(idString + task.getDescription() + " " + dateString);
    }
}
