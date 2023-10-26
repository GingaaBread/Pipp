package creation;

import processing.Processor;

/**
 *  The TitleStamp class is used to print the document title to the current document position
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
public class TitleStamp {

    /**
     *  Renders the document title on the current page at its current position by mapping the title text to its
     *  Text object equivalent and then rendering it in the LineFactory.
     */
    public static void renderTitle() {
        TextRenderer.renderText(
                Processor
                        .documentTitle
                        .getTexts()
                        .stream()
                        .map(titleText -> {
                            if (titleText.getWork() != null)
                                return new Text(titleText.getWork().getEmphasisedWork(), TextStyle.ITALIC);
                            else if (titleText.getEmphasis() != null)
                                return new Text(titleText.getEmphasis().getEmphasisedText(), TextStyle.ITALIC);
                            else return new Text(titleText.getText(), TextStyle.NORMAL);
                        }).toList(),
                TextAlignment.CENTER);
    }

}
