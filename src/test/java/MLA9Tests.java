import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processing.style.MLA9;
import processing.style.StyleSheet;

public class MLA9Tests {

    private StyleSheet mla;

    @BeforeEach
    public void setup() {
        mla = new MLA9();
    }

    //// Text Format

    /**
     *  Tests if the MLA style removes blank characters from text blocks.
     *  For example, "   Hello." turns into "Hello."
     */
    @Test
    public void blankTextCharactersAreRemoved() {
        Assertions.assertEquals(mla.formatText("\t   \t   \n Test.  \t \n"), "Test.");
        Assertions.assertEquals(mla.formatText("\t   \n Test.  \t      \n"), "Test.");
        Assertions.assertEquals(mla.formatText("\n \n Test."), "Test.");
    }

    /**
     *  Tests if more white space after one white space is removed by the MLA style.
     *  For example, "This is      a sentence." turns into "This is a sentence."
     */
    @Test
    public void unnecessaryWhiteSpaceIsRemovedFromTexts() {

    }

    /**
     *  Tests if a full-stop (.) is added to the end of a sentence if no punctuation characters exist
     *  (. ! ? , ; :)
     *  For example, "Hello World" turns into "Hello World."
     */
    @Test
    public void fullstopIsAddedAfterEmptySentences() {

    }

    /**
     *  Tests if there is exactly one space following a punctuation character for each sentence
     *  in a text block.
     *  For example: "This.Is,      an! \n\t Example?" turns into "This. Is, an! Example?"
     */
    @Test
    public void exactlyOneSpaceBeforeNewSentencesInTexts() {

    }

    /**
     *  Tests if MLA automatically uses a capital letter after a sentence ending with an exclamation
     *  mark, full-stop or question mark.
     *  For example: "Hello.there" turns into "Hello. There."
     */
    @Test
    public void capitalLetterAfterSentenceInTexts() {

    }

    /**
     *  Tests if unnecessary spaces are removed before punctuation characters in text blocks.
     *  For example, "Hello , there \t!" turns into "Hello, there!"
     */
    @Test
    public void unnecessarySpaceBeforePunctuationIsRemovedInTexts() {

    }

    //// Paragraph Format

}
