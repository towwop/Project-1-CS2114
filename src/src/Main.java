import java.util.Scanner;

/**
 * Entry point. Creates the TaskStorage and TUI and runs the main loop.
 *
 * @author Oscar, Abdullah, Vihaan
 * @version 2026.09.24
 */
public class Main {
    /**
     * Runs the task manager until the user quits.
     *
     * @param args
     *            command-line arguments (unused)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        TaskStorage storage = new TaskStorage();
        TUI ui = new TUI(storage, scanner);

        boolean running = true;

        while (running) {
            ui.printTaskList();
            running = ui.handleUserAction();
        }
    }
}
