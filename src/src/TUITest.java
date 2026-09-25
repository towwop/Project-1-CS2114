import java.time.LocalDate;
import java.util.Scanner;

/**
 * Tests for TUI. Each test feeds the TUI a Scanner over a fixed string of
 * input lines, then checks storage and the printed output.
 *
 * @author Oscar, Abdullah, Vihaan
 * @version 2026.09.25
 */
public class TUITest
    extends student.TestCase
{
    //~ Fields ................................................................

    private TaskStorage storage;
    private LocalDate date;

    //~ Setup .................................................................

    /**
     * Creates an empty storage before each test.
     */
    public void setUp()
    {
        storage = new TaskStorage();
        date = LocalDate.of(2026, 10, 1);
        // systemOut() only starts recording once it's first called, so call
        // it here. Otherwise the first test's output is never captured.
        systemOut().clearHistory();
    }

    //~ Helpers ...............................................................

    /**
     * Creates a TUI whose input is the given lines, one per nextLine().
     *
     * @param lines
     *            the input lines, in order
     * @return the TUI
     */
    private TUI tui(String... lines)
    {
        return new TUI(storage, new Scanner(String.join("\n", lines) + "\n"));
    }

    /**
     * Adds tasks directly to storage until it is full.
     *
     * @throws StorageFullException
     *             never, since it stops at MAX_TASKS
     */
    private void fill()
        throws StorageFullException
    {
        while (storage.getSize() < TaskStorage.MAX_TASKS)
        {
            storage.add("T", date);
        }
    }

    //~ handleUserAction ......................................................

    /**
     * A blank action means quit.
     */
    public void testUserActionBlankQuits()
    {
        assertFalse(tui("").handleUserAction());
    }

    /**
     * An unknown action does nothing but keeps the program running.
     */
    public void testUserActionUnknown()
    {
        assertTrue(tui("x").handleUserAction());
        assertEquals(0, storage.getSize());
    }

    /**
     * "a" (any case) goes to adding a task.
     */
    public void testUserActionAdd()
    {
        assertTrue(tui("A", "", "Buy milk", "10/01/2026").handleUserAction());
        assertEquals(1, storage.getSize());
        assertEquals("Buy milk", storage.getTaskFromID(1).getDescription());
        assertEquals(date, storage.getTaskFromID(1).getDate());
    }

    /**
     * "e" goes to editing a task.
     *
     * @throws StorageFullException
     *             never
     */
    public void testUserActionEdit()
        throws StorageFullException
    {
        storage.add("Old", date);
        assertTrue(tui("e", "1", "New", "12/25/2026").handleUserAction());
        assertEquals("New", storage.getTaskFromID(1).getDescription());
        assertEquals(
            LocalDate.of(2026, 12, 25),
            storage.getTaskFromID(1).getDate());
    }

    /**
     * "r" goes to removing a task.
     *
     * @throws StorageFullException
     *             never
     */
    public void testUserActionRemove()
        throws StorageFullException
    {
        storage.add("A", date);
        assertTrue(tui("r", "1").handleUserAction());
        assertEquals(0, storage.getSize());
    }

    //~ handleAddTask .........................................................

    /**
     * Answering "n" adds a plain task.
     */
    public void testAddTaskNo()
    {
        tui("n", "Homework", "10/01/2026").handleAddTask();
        assertEquals(1, storage.getSize());
        assertFalse(storage.getTaskFromID(1) instanceof SubTask);
    }

    /**
     * A blank description cancels the add.
     */
    public void testAddTaskBlankDescription()
    {
        tui("n", "").handleAddTask();
        assertEquals(0, storage.getSize());
    }

    /**
     * A blank date cancels the add.
     */
    public void testAddTaskBlankDate()
    {
        tui("n", "Homework", "").handleAddTask();
        assertEquals(0, storage.getSize());
    }

    /**
     * A badly formatted date re-prompts until a good one is entered.
     */
    public void testAddTaskBadDateReprompts()
    {
        tui("n", "Homework", "2026-10-01", "Oct 1", "10/01/2026")
            .handleAddTask();
        assertEquals(1, storage.getSize());
        assertEquals(date, storage.getTaskFromID(1).getDate());
        assertTrue(
            systemOut().getHistory().contains("Incorrect date format"));
    }

    /**
     * An impossible date (02/30) is rejected and re-prompts, and a real leap
     * day (02/29/2028) is accepted.
     */
    public void testAddTaskImpossibleDateRejected()
    {
        tui("n", "Leap", "02/30/2026", "02/29/2028").handleAddTask();
        assertEquals(1, storage.getSize());
        assertEquals(
            LocalDate.of(2028, 2, 29),
            storage.getTaskFromID(1).getDate());
        assertTrue(
            systemOut().getHistory().contains("Incorrect date format"));
    }

    /**
     * Adding when storage is full prints a message instead of crashing.
     *
     * @throws StorageFullException
     *             never from fill
     */
    public void testAddTaskWhenFull()
        throws StorageFullException
    {
        fill();
        tui("n", "Extra", "10/01/2026").handleAddTask();
        assertEquals(TaskStorage.MAX_TASKS, storage.getSize());
        assertTrue(systemOut().getHistory().contains("storage is full"));
    }

    /**
     * Answering "y" goes to adding a subtask.
     *
     * @throws StorageFullException
     *             never
     */
    public void testAddTaskYesMakesSubtask()
        throws StorageFullException
    {
        storage.add("Parent", date);
        tui("y", "1", "Child", "10/02/2026").handleAddTask();

        Task sub = storage.getTaskFromID(2);
        assertTrue(sub instanceof SubTask);
        assertEquals(1, ((SubTask)sub).getParentTaskId());
        assertEquals("Child", sub.getDescription());
    }

    //~ handleAddSubtask ......................................................

    /**
     * A blank parent id cancels.
     *
     * @throws StorageFullException
     *             never
     */
    public void testAddSubtaskBlankId()
        throws StorageFullException
    {
        storage.add("Parent", date);
        tui("").handleAddSubtask();
        assertEquals(1, storage.getSize());
    }

    /**
     * A non-number, a missing id, and a subtask id each re-prompt, then a
     * valid parent works.
     *
     * @throws StorageFullException
     *             never
     */
    public void testAddSubtaskRepromptsParent()
        throws StorageFullException
    {
        storage.add("Parent", date);
        storage.addSubTask("Child", date, 1);

        tui("abc", "99", "2", "1", "Child 2", "10/01/2026")
            .handleAddSubtask();

        String out = systemOut().getHistory();
        assertTrue(out.contains("Please input a number"));
        assertTrue(out.contains("Id does not belong to a valid task"));
        assertTrue(out.contains("Parent cannot be a subtask"));
        assertEquals(3, storage.getSize());
        assertEquals(1, ((SubTask)storage.getTaskFromID(3)).getParentTaskId());
    }

    /**
     * A blank description cancels the subtask.
     *
     * @throws StorageFullException
     *             never
     */
    public void testAddSubtaskBlankDescription()
        throws StorageFullException
    {
        storage.add("Parent", date);
        tui("1", "").handleAddSubtask();
        assertEquals(1, storage.getSize());
    }

    /**
     * A blank date cancels the subtask.
     *
     * @throws StorageFullException
     *             never
     */
    public void testAddSubtaskBlankDate()
        throws StorageFullException
    {
        storage.add("Parent", date);
        tui("1", "Child", "").handleAddSubtask();
        assertEquals(1, storage.getSize());
    }

    /**
     * Adding a subtask when storage is full prints a message.
     *
     * @throws StorageFullException
     *             never from fill
     */
    public void testAddSubtaskWhenFull()
        throws StorageFullException
    {
        fill();
        tui("1", "Child", "10/01/2026").handleAddSubtask();
        assertEquals(TaskStorage.MAX_TASKS, storage.getSize());
        assertTrue(systemOut().getHistory().contains("storage is full"));
    }

    //~ handleRemoveTask ......................................................

    /**
     * A blank id cancels the remove.
     *
     * @throws StorageFullException
     *             never
     */
    public void testRemoveBlank()
        throws StorageFullException
    {
        storage.add("A", date);
        tui("").handleRemoveTask();
        assertEquals(1, storage.getSize());
    }

    /**
     * A non-number and a missing id re-prompt, then a valid id is removed.
     *
     * @throws StorageFullException
     *             never
     */
    public void testRemoveReprompts()
        throws StorageFullException
    {
        storage.add("A", date);
        storage.add("B", date);

        tui("abc", "99", "1").handleRemoveTask();

        String out = systemOut().getHistory();
        assertTrue(out.contains("Please input a number"));
        assertTrue(out.contains("Task id does not belong to a valid task"));
        assertEquals(1, storage.getSize());
        assertNull(storage.getTaskFromID(1));
    }

    /**
     * Removing a parent through the TUI also removes its subtasks.
     *
     * @throws StorageFullException
     *             never
     */
    public void testRemoveCascade()
        throws StorageFullException
    {
        storage.add("Parent", date);
        storage.addSubTask("Child", date, 1);
        storage.add("Other", date);

        tui("1").handleRemoveTask();
        assertEquals(1, storage.getSize());
        assertNotNull(storage.getTaskFromID(3));
    }

    //~ handleEditTask ........................................................

    /**
     * A blank id cancels the edit.
     *
     * @throws StorageFullException
     *             never
     */
    public void testEditBlankId()
        throws StorageFullException
    {
        storage.add("A", date);
        tui("").handleEditTask();
        assertEquals("A", storage.getTaskFromID(1).getDescription());
    }

    /**
     * A non-number id prints a message and cancels.
     *
     * @throws StorageFullException
     *             never
     */
    public void testEditNotANumber()
        throws StorageFullException
    {
        storage.add("A", date);
        tui("abc").handleEditTask();
        assertTrue(systemOut().getHistory().contains("Please input a number"));
        assertEquals("A", storage.getTaskFromID(1).getDescription());
    }

    /**
     * A missing id prints a message and cancels.
     */
    public void testEditMissingId()
    {
        tui("99").handleEditTask();
        assertTrue(systemOut().getHistory()
            .contains("Task id does not belong to a valid task"));
    }

    /**
     * A blank date keeps the old date and only changes the description.
     *
     * @throws StorageFullException
     *             never
     */
    public void testEditDescriptionOnly()
        throws StorageFullException
    {
        storage.add("Old", date);
        tui("1", "New", "").handleEditTask();
        assertEquals("New", storage.getTaskFromID(1).getDescription());
        assertEquals(date, storage.getTaskFromID(1).getDate());
    }

    /**
     * A blank description keeps the old description and only changes the
     * date.
     *
     * @throws StorageFullException
     *             never
     */
    public void testEditDateOnly()
        throws StorageFullException
    {
        storage.add("Old", date);
        tui("1", "", "12/25/2026").handleEditTask();
        assertEquals("Old", storage.getTaskFromID(1).getDescription());
        assertEquals(
            LocalDate.of(2026, 12, 25),
            storage.getTaskFromID(1).getDate());
    }

    /**
     * Editing a subtask works the same way.
     *
     * @throws StorageFullException
     *             never
     */
    public void testEditSubtask()
        throws StorageFullException
    {
        storage.add("Parent", date);
        storage.addSubTask("Child", date, 1);
        tui("2", "Renamed", "").handleEditTask();
        assertEquals("Renamed", storage.getTaskFromID(2).getDescription());
    }

    //~ printTaskList .........................................................

    /**
     * An empty list still prints the separator lines.
     */
    public void testPrintEmptyList()
    {
        tui().printTaskList();
        assertTrue(systemOut().getHistory().contains("----------"));
    }

    /**
     * Tasks and subtasks are printed using their toString().
     *
     * @throws StorageFullException
     *             never
     */
    public void testPrintTaskList()
        throws StorageFullException
    {
        storage.add("Buy milk", date);
        storage.addSubTask("Get cash", date, 1);

        tui().printTaskList();

        String out = systemOut().getHistory();
        assertTrue(out.contains("[1]: Buy milk (Due: 10/01/2026)"));
        assertTrue(out.contains("[1->2]: Get cash (Due: 10/01/2026)"));
    }
}
