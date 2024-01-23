package creation;

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

        TextRenderer.renderText(title
                        .getTexts()
                        .stream()
                        .map(titleText -> {
                            if (titleText.getWork() != null)
                                return new Text(titleText.getWork().getEmphasisedWork(), Processor.getWorkFont(),
                                        Processor.getWorkFontSize(), Processor.getWorkFontColour());
                            else if (titleText.getEmphasis() != null)
                                return new Text(titleText.getEmphasis().getEmphasisedText(), Processor.getEmphasisFont(),
                                        Processor.getEmphasisFontSize(), Processor.getEmphasisFontColour());
                            else
                                return new Text(titleText.getText(), Processor.getSentenceFont(),
                                        Processor.getSentenceFontSize(), Processor.getSentenceFontColour());
                        }).toList(),
                ContentAlignment.CENTER
        );
    }

}
