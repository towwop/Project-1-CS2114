import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
        this.storage = storage;
        this.scanner = scanner;
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
        }
        else if (c == 'e') {
            handleEditTask();
        }
        else if (c == 'r') {
            handleRemoveTask();
        }

        return false;
    }


    /**
     * Asks for a description and due date (MM/DD/YYYY), then adds the task
     * to storage.
     */
    public void handleAddTask() {
        System.out.print("Would you like to add it as a subtask (y/N): ");

        String input = scanner.nextLine().trim();

        if (input.length() == 0) {
            return;
        }

        char c = Character.toLowerCase(input.charAt(0));

        if (c == 'y') {
            handleAddSubtask();
            return;
        }

        String description = getUserTaskDescription();

        if (description == null) {
            return;
        }

        LocalDate date = getUserTaskDate();

        if (date == null) {
            return;
        }

        try {
            storage.add(description, date);
        }

        catch (StorageFullException e) {
            System.out.println("Task storage is full. Remove a task first.");
        }

        return;
    }


    /**
     * Asks for a description, due date, and parent task id, then adds the
     * subtask to storage. The parent cannot be a subtask.
     */
    public void handleAddSubtask() {
        int parentId = 0;

        while (true) {
            System.out.print(
                "Input the parent task id (cannot be a subtask): ");

            String input = scanner.nextLine().trim();

            if (input.length() == 0) {
                return;
            }

            try {
                parentId = Integer.parseInt(input);
            }

            catch (NumberFormatException e) {
                System.out.println("Please input a number.");
                continue;
            }

            Task parent = storage.getTaskFromID(parentId);

            if (parent == null) {
                System.out.println("Id does not belong to a valid task.");
                continue;
            }

            else if (parent instanceof SubTask) {
                System.out.println("Parent cannot be a subtask.");
                continue;
            }

            break;
        }

        String description = getUserTaskDescription();

        if (description == null) {
            return;
        }

        LocalDate date = getUserTaskDate();

        if (date == null) {
            return;
        }

        try {
            storage.addSubTask(description, date, parentId);
        }

        catch (StorageFullException e) {
            System.out.println("Task storage is full. Remove a task first.");
        }

        return;
    }


    /**
     * Asks for a task id and removes that task. Re-prompts on an invalid id.
     */
    public void handleRemoveTask() {
        while (true) {
            System.out.print("Enter the id of the task to remove: ");

            String input = scanner.nextLine().trim();

            if (input.length() == 0) {
                return;
            }

            Task task = null;

            try {
                int taskId = Integer.parseInt(input);

                task = storage.getTaskFromID(taskId);
            }

            catch (NumberFormatException e) {
                System.out.println("Please input a number.");
                continue;
            }

            if (task == null) {
                System.out.println("Task id does not belong to a valid task");

                continue;
            }

            storage.remove(task);
            return;
        }
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


    private String getUserTaskDescription() {
        System.out.print("Input the task description: ");

        String input = scanner.nextLine().trim();

        if (input.length() != 0) {
            return input;
        }

        return null;
    }


    private LocalDate getUserTaskDate() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(
            "MM/dd/yyyy");

        while (true) {
            System.out.print("Input the task due date [mm/dd/yyyy]: ");

            String input = scanner.nextLine().trim();

            if (input.length() == 0) {
                return null;
            }

            try {
                LocalDate date = LocalDate.parse(input, dateFormat);

                return date;
            }

            catch (DateTimeParseException e) {
                System.out.println("Incorrect date format. Try again.");
            }
        }
    }
}
