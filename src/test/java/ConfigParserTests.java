import frontend.FrontEndBridge;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConfigParserTests {

    /**
     *  Tests if the parser accepts a single title configuration without additional configurations set.
     *  This test uses the textual configuration, instead of the citation.
     */
    @Test
    public void titleConfigOnlyDoesNotThrowException() {
        Assertions.assertDoesNotThrow(() -> new FrontEndBridge("""
                config
                \ttitle "Hello World!"
                """));
    }

    /**
     *  Tests if the parser accepts a single title configuration without additional configurations set.
     *  This test uses the textual configuration, instead of the citation.
     */
    @Test
    public void citedTitleConfigOnlyDoesNotThrowException() {
        Assertions.assertDoesNotThrow(() -> new FrontEndBridge("""
                config
                \ttitle
                \tcitation
                \t\t\tof "Hello World!"
                \t\t\tin "Example"
                \t\t\tnumeration "1"
                """));
    }

}
