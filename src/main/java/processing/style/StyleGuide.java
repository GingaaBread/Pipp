package processing.style;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import processing.NumerationPosition;
import processing.NumerationType;

import java.awt.*;

/**
 *  Defines traits and rules that a formal style sheet uses.
 *  An example style sheet is the MLA style in its 9th edition.
 *  To create a custom style sheet, extend this class and specify the values.
 */
public abstract class StyleGuide {

    /**
     *  Helper method to determine if a character is a punctuation character.
     *  A punctuation character is a full-stop, question mark, exclamation mark,
     *  comma, semicolon or colon.
     * @param character - the character that should be checked
     * @return - true if the character is a punctuation character, false if not
     */
    public static boolean isPunctuation(char character) {
        return character == '.' || character == '?' || character == '!' ||
                character == ',' || character == ';' || character == ':';
    }

    /* ********************
        General Layout
     */ ///////////////////

    // Todo: page numeration
    // todo: endnotes

    /**
     *  Defines the page format of the style guide.
     *  For example, MLA9 uses standardised American Post paper.
     * @return - the {@link PDRectangle} constant that the creator class should apply
     */
    public abstract PDRectangle pageFormat();

    /**
     *  Determines the main font of the style guide.
     *  Note that at the moment, only one font type is supported.
     * @return - the {@link PDFont} that should be used by the creator class
     */
    public abstract PDFont font();

    /**
     *  Determines the main font size of the style guide.
     *  For example, the recommended font size in MLA is 12 pt.
     * @return - the font size as an integer
     */
    public abstract int fontSize();

    /**
     *  Determines the main font colour of the style guide.
     * @return - the colour representation as a PDColour
     */
    public abstract Color fontColour();

    /**
     *  Determines the distance of a tabulation (\t).
     *  For example, MLA uses an indentation padding of 0.5 inches
     * @return - the non-negative padding as a double
     */
    protected abstract double indentationPadding();

    /**
     *  Determines the margin to the sides of the document.
     * @return - the non-negative margin as a double
     */
    public abstract double margin();

    /**
     *  Determines the spacing of sentences.
     * @return - the non-negative spacing as a double
     */
    public abstract double spacing();

    /**
     *  Determines which type of numeration is used for page numbers
     * @return - the numeration type value
     */
    public abstract NumerationType numerationType();

    /**
     *  Determines where the page number is located at
     * @return - the numeration position value
     */
    public abstract NumerationPosition numerationPosition();

    /**
     *  Determines how far away the page number is from the edges
     * @return - the non-negative numeration margin as a double
     */
    public abstract double numerationMargin();

    /**
     *  Determines if the style sheet allows the use of **strong** markdown
     *  to create bold text or not. If the style sheet does not allow the use of
     *  strong markdown, but the user uses it, Pipp will halt and return an error.
     * @return - true if strong markdown is supported, false if not.
     */
    protected abstract boolean allowsStrongMarkdownInTexts();

    /**
     *  Determines if the style sheet allows the use of _italic_ markdown
     *  to create italic text or not. If the style sheet does not allow the use of
     *  italic markdown, but the user uses it, Pipp will halt and return an error.
     * @return - true if italic markdown is supported, false if not.
     */
    protected abstract boolean allowsItalicMarkdownInTexts();

    public abstract double paragraphIndentation();

    /**
     *  Defines how the style sheet formats a simple text block.
     *  Examples could include adding exactly one whitespace after a punctuation mark.
     * @param textBlock - the text block content with the enclosing quotation marks removed
     * @return - returns the formatted text content as it will be forwarded to the paragraph formatter.
     */
    public abstract String formatText(String textBlock);

    /**
     *  Defines how the style sheet formats a paragraph block.
     *  Examples could include indenting the first sentence.
     * @param formattedTextBlocks - All text blocks of the paragraph already formatted.
     * @return - the formatted paragraph content as it will be rendered in the document.
     */
    public abstract String formatParagraph(String[] formattedTextBlocks);

}
