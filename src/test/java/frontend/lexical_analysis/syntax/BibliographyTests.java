package frontend.lexical_analysis.syntax;

import frontend.FrontEndBridge;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BibliographyTests {

    @Test
    void bibliography_Keyword_is_Required() {
        final var input =
                """
                        bibliography
                        	id "HP1"
                        		type "Book"
                        """;
        Assertions.assertDoesNotThrow(() -> new FrontEndBridge(input, true));

        final var noBibInput =
                """
                        id "HP1"
                        	type "Book"
                        """;
        Assertions.assertThrows(IllegalArgumentException.class, () -> new FrontEndBridge(noBibInput, true));
    }

}
