package creation.stamp;

import creation.content.ContentAlignment;
import creation.content.text.Text;
import creation.content.text.TextRenderer;
import processing.Processor;

/**
 * Used to add a header to the document, which includes the names of all authors and assessors, the title of the
 * document, and the publication date.
 *
 * @version 1.0
 * @since 1.0
 */
public class HeaderStamp extends IntroductoryStamp {

    /**
     * Prevents instantiation
     */
    private HeaderStamp() {
        throw new UnsupportedOperationException("Cannot instantiate helper class");
    }

    /**
     * Renders the header stamp to the current position in the document
     */
    public static void renderHeader() {
        renderInstitutionIfExists();
        renderChairIfExists();
        renderAuthorsIfAnyExist();
        renderAssessorsIfAnyExist();
        renderTitleIfExists();
        renderDateOrSemesterIfExist();
    }
}
