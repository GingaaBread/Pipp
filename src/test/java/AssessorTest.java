import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import processing.Assessor;

public class AssessorTest {

    /**
     *  Tests if specifying a legal whole name (a name as one string rather than specifying the first
     *  and last name individually) sets the correct first and last names.
     *  For example, the input "John Doe" turns into firstname="John", lastname="Doe"
     */
    @Test
    public void testLegalWholeNameSetsFirstAndLastName() {
        var assessor = new Assessor("John Doe", null);

        Assertions.assertEquals("John", assessor.getFirstname());
        Assertions.assertEquals("Doe", assessor.getLastname());

        assessor = new Assessor("    John    Doe   ", null);

        Assertions.assertEquals("John", assessor.getFirstname());
        Assertions.assertEquals("Doe", assessor.getLastname());

        assessor = new Assessor("\t\t    John    \tDoe\t\t   ", null);

        Assertions.assertEquals("John", assessor.getFirstname());
        Assertions.assertEquals("Doe", assessor.getLastname());

        assessor = new Assessor("\n\t    John\n    \tDoe\n\t   ", null);

        Assertions.assertEquals("John", assessor.getFirstname());
        Assertions.assertEquals("Doe", assessor.getLastname());
    }
}
