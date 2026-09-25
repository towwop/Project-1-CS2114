import java.time.LocalDate;

/**
 * Tests for TaskStorage.
 *
 * @author Oscar, Abdullah, Vihaan
 * @version 2026.09.25
 */
public class TaskStorageTest
    extends student.TestCase
{
    //~ Fields ................................................................

    private TaskStorage storage;
    private LocalDate date;

    //~ Setup .................................................................

    /**
     * Creates an empty storage and a due date before each test.
     */
    public void setUp()
    {
        storage = new TaskStorage();
        date = LocalDate.of(2026, 10, 1);
    }

    //~ Helpers ...............................................................

    /**
     * Builds a space-separated list of the stored ids, in order.
     *
     * @return the ids, like "1 2 3"
     */
    private String ids()
    {
        String result = "";
        Task[] tasks = storage.getTasks();
        for (int i = 0; i < tasks.length; i++)
        {
            if (i > 0)
            {
                result += " ";
            }
            result += tasks[i].getId();
        }
        return result;
    }

    /**
     * Adds tasks until storage is full.
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

    //~ Tests .................................................................

    /**
     * A new storage is empty.
     */
    public void testConstructor()
    {
        assertEquals(0, storage.getSize());
        assertEquals(0, storage.getTasks().length);
    }

    /**
     * add stores a Task with the given values and ids start at 1.
     *
     * @throws StorageFullException
     *             never
     */
    public void testAdd()
        throws StorageFullException
    {
        Task a = storage.add("A", date);
        Task b = storage.add("B", date);

        assertEquals(1, a.getId());
        assertEquals(2, b.getId());
        assertEquals("A", a.getDescription());
        assertEquals(date, a.getDate());
        assertFalse(a instanceof SubTask);
        assertEquals(2, storage.getSize());
    }

    /**
     * add returns null for a bad description or date, and a rejected add
     * doesn't use up an id.
     *
     * @throws StorageFullException
     *             never
     */
    public void testAddInvalid()
        throws StorageFullException
    {
        assertNull(storage.add(null, date));
        assertNull(storage.add("", date));
        assertNull(storage.add("   ", date));
        assertNull(storage.add("A", null));
        assertEquals(0, storage.getSize());

        assertEquals(1, storage.add("A", date).getId());
    }

    /**
     * addSubTask stores a SubTask linked to its parent.
     *
     * @throws StorageFullException
     *             never
     */
    public void testAddSubTask()
        throws StorageFullException
    {
        storage.add("Parent", date);
        Task sub = storage.addSubTask("Child", date, 1);

        assertTrue(sub instanceof SubTask);
        assertEquals(2, sub.getId());
        assertEquals(1, ((SubTask)sub).getParentTaskId());
        assertEquals("Child", sub.getDescription());
        assertEquals(2, storage.getSize());
    }

    /**
     * addSubTask returns null for a missing parent, a subtask parent, or a
     * bad description or date.
     *
     * @throws StorageFullException
     *             never
     */
    public void testAddSubTaskInvalid()
        throws StorageFullException
    {
        storage.add("Parent", date);
        storage.addSubTask("Child", date, 1);

        assertNull(storage.addSubTask("X", date, 99));
        assertNull(storage.addSubTask("X", date, 2));
        assertNull(storage.addSubTask(null, date, 1));
        assertNull(storage.addSubTask("  ", date, 1));
        assertNull(storage.addSubTask("X", null, 1));
        assertEquals(2, storage.getSize());
    }

    /**
     * Storage holds exactly MAX_TASKS, and the next add throws.
     *
     * @throws StorageFullException
     *             never from fill
     */
    public void testAddWhenFull()
        throws StorageFullException
    {
        fill();
        assertEquals(TaskStorage.MAX_TASKS, storage.getSize());

        Exception thrown = null;
        try
        {
            storage.add("Extra", date);
        }
        catch (StorageFullException e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertEquals(TaskStorage.MAX_TASKS, storage.getSize());
    }

    /**
     * addSubTask also throws when full.
     *
     * @throws StorageFullException
     *             never from fill
     */
    public void testAddSubTaskWhenFull()
        throws StorageFullException
    {
        fill();

        Exception thrown = null;
        try
        {
            storage.addSubTask("Extra", date, 1);
        }
        catch (StorageFullException e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
    }

    /**
     * The full check happens before validation, so even bad input throws.
     *
     * @throws StorageFullException
     *             never from fill
     */
    public void testFullCheckedBeforeValidation()
        throws StorageFullException
    {
        fill();

        Exception thrown = null;
        try
        {
            storage.add(null, null);
        }
        catch (StorageFullException e)
        {
            thrown = e;
        }
        assertNotNull(thrown);

        thrown = null;
        try
        {
            storage.addSubTask(null, null, 999);
        }
        catch (StorageFullException e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
    }

    /**
     * After removing from a full storage, adding works again.
     *
     * @throws StorageFullException
     *             if remove didn't free a slot
     */
    public void testAddAfterFullAndRemove()
        throws StorageFullException
    {
        fill();
        assertTrue(storage.remove(storage.getTaskFromID(10)));
        assertNotNull(storage.add("New", date));
        assertEquals(TaskStorage.MAX_TASKS, storage.getSize());
    }

    /**
     * Removing a middle task shifts the rest left with no gaps.
     *
     * @throws StorageFullException
     *             never
     */
    public void testRemoveShiftsLeft()
        throws StorageFullException
    {
        storage.add("A", date);
        Task b = storage.add("B", date);
        storage.add("C", date);

        assertTrue(storage.remove(b));
        assertEquals("1 3", ids());
        assertEquals(2, storage.getSize());
        assertNull(storage.getTaskFromID(2));
    }

    /**
     * Removing the first and last tasks works.
     *
     * @throws StorageFullException
     *             never
     */
    public void testRemoveEnds()
        throws StorageFullException
    {
        Task a = storage.add("A", date);
        storage.add("B", date);
        Task c = storage.add("C", date);

        assertTrue(storage.remove(a));
        assertTrue(storage.remove(c));
        assertEquals("2", ids());
    }

    /**
     * remove returns false for null, a task not in storage, a different
     * object with a stored id, and a task that was already removed.
     *
     * @throws StorageFullException
     *             never
     */
    public void testRemoveInvalid()
        throws StorageFullException
    {
        Task a = storage.add("A", date);

        assertFalse(storage.remove(null));
        assertFalse(storage.remove(new Task(99, "X", date)));
        assertFalse(storage.remove(new Task(1, "Copy", date)));
        assertEquals(1, storage.getSize());

        assertTrue(storage.remove(a));
        assertFalse(storage.remove(a));
    }

    /**
     * Removing a subtask leaves its parent alone.
     *
     * @throws StorageFullException
     *             never
     */
    public void testRemoveSubTask()
        throws StorageFullException
    {
        storage.add("Parent", date);
        Task sub = storage.addSubTask("Child", date, 1);

        assertTrue(storage.remove(sub));
        assertEquals("1", ids());
    }

    /**
     * Removing a parent also removes all of its subtasks, including two in a
     * row, but not other tasks or other parents' subtasks.
     *
     * @throws StorageFullException
     *             never
     */
    public void testRemoveCascade()
        throws StorageFullException
    {
        Task a = storage.add("A", date);        // 1
        storage.addSubTask("A1", date, 1);      // 2
        storage.add("B", date);                 // 3
        storage.addSubTask("A2", date, 1);      // 4
        storage.addSubTask("A3", date, 1);      // 5, right after 4
        storage.addSubTask("B1", date, 3);      // 6

        assertTrue(storage.remove(a));
        assertEquals("3 6", ids());
        assertEquals(2, storage.getSize());
    }

    /**
     * Ids are never reused after a removal.
     *
     * @throws StorageFullException
     *             never
     */
    public void testIdsNotReused()
        throws StorageFullException
    {
        storage.add("A", date);
        Task b = storage.add("B", date);
        storage.remove(b);

        assertEquals(3, storage.add("C", date).getId());
    }

    /**
     * editDescription changes a stored task's description.
     *
     * @throws StorageFullException
     *             never
     */
    public void testEditDescription()
        throws StorageFullException
    {
        Task a = storage.add("A", date);

        assertTrue(storage.editDescription(a, "New"));
        assertEquals("New", a.getDescription());
    }

    /**
     * editDescription rejects a bad task or description and keeps the old
     * value.
     *
     * @throws StorageFullException
     *             never
     */
    public void testEditDescriptionInvalid()
        throws StorageFullException
    {
        Task a = storage.add("A", date);

        assertFalse(storage.editDescription(null, "New"));
        assertFalse(storage.editDescription(new Task(1, "Copy", date), "New"));
        assertFalse(storage.editDescription(a, null));
        assertFalse(storage.editDescription(a, ""));
        assertFalse(storage.editDescription(a, "   "));
        assertEquals("A", a.getDescription());
    }

    /**
     * editDate changes a stored task's date.
     *
     * @throws StorageFullException
     *             never
     */
    public void testEditDate()
        throws StorageFullException
    {
        Task a = storage.add("A", date);
        LocalDate newDate = LocalDate.of(2027, 1, 1);

        assertTrue(storage.editDate(a, newDate));
        assertEquals(newDate, a.getDate());
    }

    /**
     * editDate rejects a bad task or date and keeps the old value.
     *
     * @throws StorageFullException
     *             never
     */
    public void testEditDateInvalid()
        throws StorageFullException
    {
        Task a = storage.add("A", date);
        LocalDate newDate = LocalDate.of(2027, 1, 1);

        assertFalse(storage.editDate(null, newDate));
        assertFalse(storage.editDate(new Task(1, "Copy", date), newDate));
        assertFalse(storage.editDate(a, null));
        assertEquals(date, a.getDate());
    }

    /**
     * getTaskFromID finds stored tasks and returns null otherwise.
     *
     * @throws StorageFullException
     *             never
     */
    public void testGetTaskFromID()
        throws StorageFullException
    {
        assertNull(storage.getTaskFromID(1));

        Task a = storage.add("A", date);
        Task b = storage.add("B", date);

        assertSame(a, storage.getTaskFromID(1));
        assertSame(b, storage.getTaskFromID(2));
        assertNull(storage.getTaskFromID(3));
    }

    /**
     * getTasks returns a new array of length size, and changing it doesn't
     * change storage.
     *
     * @throws StorageFullException
     *             never
     */
    public void testGetTasksIsCopy()
        throws StorageFullException
    {
        Task a = storage.add("A", date);
        storage.add("B", date);

        Task[] copy = storage.getTasks();
        assertEquals(2, copy.length);
        assertSame(a, copy[0]);

        copy[0] = null;
        assertSame(a, storage.getTasks()[0]);
        assertEquals(2, storage.getSize());
        assertNotSame(storage.getTasks(), storage.getTasks());
    }
}
