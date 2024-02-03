package creation.stamp;

import creation.content.ContentAlignment;
import creation.content.text.TextRenderer;
import error.MissingMemberException;
import processing.Processor;

/**
 * The TitleStamp class is used to print the document title to the current document position
 *
 * @version 1.0
 * @since 1.0
 */
public class TitleStamp {

    /**
     * Prevents instantiation
     */
    private TitleStamp() {
        throw new UnsupportedOperationException("Cannot instantiate helper class");
    }

    /**
     * Renders the document title on the current page at its current position by mapping the title text to its
     * Text object equivalent and then rendering it in the LineFactory.
     */
    public static void renderTitle() {
        var title = Processor.getDocumentTitle();
        if (title.getTexts().isEmpty())
            throw new MissingMemberException("2: Cannot render a title if no title has been configured.");

        TextRenderer.renderText(title.asTextList(), ContentAlignment.CENTER);
    }

}
