import java.time.LocalDate;

/**
 * Tests for Task.
 *
 * @author Oscar, Abdullah, Vihaan
 * @version 2026.09.25
 */
public class TaskTest
    extends student.TestCase
{
    //~ Fields ................................................................

    private Task task;
    private LocalDate date;

    //~ Setup .................................................................

    /**
     * Creates a task before each test.
     */
    public void setUp()
    {
        date = LocalDate.of(2026, 10, 1);
        task = new Task(7, "Homework", date);
    }

    //~ Tests .................................................................

    /**
     * The constructor stores the id, description, and date.
     */
    public void testConstructorAndGetters()
    {
        assertEquals(7, task.getId());
        assertEquals("Homework", task.getDescription());
        assertEquals(date, task.getDate());
    }

    /**
     * setDescription accepts a real description.
     */
    public void testSetDescription()
    {
        assertTrue(task.setDescription("Study"));
        assertEquals("Study", task.getDescription());
    }

    /**
     * setDescription rejects null, empty, and blank, keeping the old value.
     */
    public void testSetDescriptionInvalid()
    {
        assertFalse(task.setDescription(null));
        assertFalse(task.setDescription(""));
        assertFalse(task.setDescription("   "));
        assertEquals("Homework", task.getDescription());
    }

    /**
     * setDate accepts a real date.
     */
    public void testSetDate()
    {
        LocalDate newDate = LocalDate.of(2027, 1, 5);
        assertTrue(task.setDate(newDate));
        assertEquals(newDate, task.getDate());
    }

    /**
     * setDate rejects null, keeping the old date.
     */
    public void testSetDateInvalid()
    {
        assertFalse(task.setDate(null));
        assertEquals(date, task.getDate());
    }

    /**
     * toString shows the id, description, and a zero-padded MM/DD/YYYY date.
     */
    public void testToString()
    {
        assertEquals("[7]: Homework (Due: 10/01/2026)", task.toString());

        Task padded = new Task(1, "X", LocalDate.of(2027, 1, 5));
        assertEquals("[1]: X (Due: 01/05/2027)", padded.toString());
    }

    /**
     * toString reflects edits.
     */
    public void testToStringAfterEdit()
    {
        task.setDescription("Study");
        task.setDate(LocalDate.of(2026, 12, 25));
        assertEquals("[7]: Study (Due: 12/25/2026)", task.toString());
    }
}
