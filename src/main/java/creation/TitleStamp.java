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
        if (Processor.documentTitle.getTexts().isEmpty())
            throw new MissingMemberException("2: Cannot render a title if no title has been configured.");

        TextRenderer.renderText(
                Processor
                        .documentTitle
                        .getTexts()
                        .stream()
                        .map(titleText -> {
                            if (titleText.getWork() != null)
                                return new Text(titleText.getWork().getEmphasisedWork(), Processor.workFont,
                                        Processor.workFontSize, Processor.workFontColour);
                            else if (titleText.getEmphasis() != null)
                                return new Text(titleText.getEmphasis().getEmphasisedText(), Processor.emphasisFont,
                                        Processor.emphasisFontSize, Processor.emphasisFontColour);
                            else
                                return new Text(titleText.getText(), Processor.sentenceFont, Processor.sentenceFontSize,
                                        Processor.sentenceFontColour);
                        }).toList(),
                ContentAlignment.CENTER);
    }

}
