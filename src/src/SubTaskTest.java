import java.time.LocalDate;

/**
 * Tests for SubTask.
 *
 * @author Oscar, Abdullah, Vihaan
 * @version 2026.09.25
 */
public class SubTaskTest
    extends student.TestCase
{
    //~ Fields ................................................................

    private SubTask subTask;
    private LocalDate date;

    //~ Setup .................................................................

    /**
     * Creates a subtask before each test.
     */
    public void setUp()
    {
        date = LocalDate.of(2026, 10, 1);
        subTask = new SubTask(5, "Get cash", date, 2);
    }

    //~ Tests .................................................................

    /**
     * The constructor stores all four values.
     */
    public void testConstructorAndGetters()
    {
        assertEquals(5, subTask.getId());
        assertEquals("Get cash", subTask.getDescription());
        assertEquals(date, subTask.getDate());
        assertEquals(2, subTask.getParentTaskId());
    }

    /**
     * The setters inherited from Task still work.
     */
    public void testInheritedSetters()
    {
        LocalDate newDate = LocalDate.of(2027, 3, 4);

        assertTrue(subTask.setDescription("Get more cash"));
        assertTrue(subTask.setDate(newDate));
        assertEquals("Get more cash", subTask.getDescription());
        assertEquals(newDate, subTask.getDate());

        assertFalse(subTask.setDescription(" "));
        assertFalse(subTask.setDate(null));
        assertEquals("Get more cash", subTask.getDescription());
        assertEquals(newDate, subTask.getDate());
    }

    /**
     * toString shows parent id, then own id, then description and date.
     */
    public void testToString()
    {
        assertEquals("[2->5]: Get cash (Due: 10/01/2026)", subTask.toString());
    }

    /**
     * A SubTask can be used as a Task, and toString still uses the SubTask
     * version.
     */
    public void testIsATask()
    {
        Task asTask = subTask;
        assertEquals(5, asTask.getId());
        assertEquals("[2->5]: Get cash (Due: 10/01/2026)", asTask.toString());
    }
}
