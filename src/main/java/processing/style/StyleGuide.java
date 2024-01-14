package processing.style;

import lombok.NonNull;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import processing.*;

import java.awt.*;
import java.time.LocalDate;

/**
 * Defines traits and rules that a formal style guide uses.
 * An example style guide is the MLA style in its 9th edition.
 * To create a custom style sheet, extend this class and specify all values.
 *
 * @author Gino Glink
 * @version 1.0
 * @since 1.0
 */
public abstract class StyleGuide {

    /**
     * Helper method to determine if a character is a punctuation character.
     * A punctuation character is a full-stop, question mark, exclamation mark,
     * comma, semicolon or colon.
     *
     * @param character - the character that should be checked
     * @return - true if the character is a punctuation character, false if it is not
     */
    public static boolean isPunctuation(char character) {
        return character == '.' || character == '?' || character == '!' ||
                character == ',' || character == ';' || character == ':';
    }


    //// General Layout ////


    // TODO: page numeration

    /**
     * Determines the default document type specified by the style guide.
     * For most style guides, this would be PAPER.
     *
     * @return - the default document type as a {@link DocumentType} constant.
     */
    public abstract DocumentType documentType();

    /**
     * Defines the page format of the style guide.
     * For example, MLA9 uses standard, white 8.5 x 11-inch paper.
     *
     * @return - the {@link PDRectangle} constant that the creator class should apply
     */
    public abstract PDRectangle pageFormat();

    /**
     * Determines the margin to all four sides of the document
     *
     * @return - the non-negative margin as a float
     */
    public abstract float margin();

    /**
     * Determines the spacing between sentences
     *
     * @return - the non-negative spacing as a float
     */
    public abstract float spacing();


    //// PAGE NUMERATION ////


    /**
     * Determines which type of numeration should be used for page numbers.
     * For most style guides this would be ARABIC.
     *
     * @return - the numeration type value
     */
    public abstract NumerationType numerationType();

    /**
     * Determines where the page number is located at
     *
     * @return - the numeration position value
     */
    public abstract NumerationPosition numerationPosition();

    /**
     * Determines how far away the page numbers are from the sides of the document
     *
     * @return - the non-negative numeration margin as a float
     */
    public abstract float numerationMargin();


    //// FONT ///


    /**
     * Determines the main font of the style guide.
     * The main font is used for the default text provided by the user.
     *
     * @return - the {@link PDFont} that should be used by the creator class
     */
    public abstract PDFont font();

    /**
     * Determines the font used for emphasised text.
     * An example use is using a foreign language in the document.
     *
     * @return - the {@link PDFont} that should be used for emphasised text
     */
    public abstract PDFont emphasisFont();

    /**
     * Determines the font used for work references.
     * This is usually the same as the emphasised font, but Pipp offers the ability to change them individually.
     *
     * @return - the {@link PDFont} that should be used for work references
     */
    public abstract PDFont workFont();

    /**
     * Determines the main font size of the style guide.
     * For example, the recommended font size in MLA is 12 pt.
     *
     * @return - the font size as an integer
     */
    public abstract int fontSize();

    /**
     * Determines the font size of the style guide used for emphasis.
     * For example, the recommended font size in MLA is 12 pt.
     *
     * @return - the emphasis font size as an integer
     */
    public abstract int emphasisFontSize();

    /**
     * Determines the font size of the style guide used for work references.
     * For example, the recommended font size in MLA is 12 pt.
     *
     * @return - the work font size as an integer
     */
    public abstract int workFontSize();

    /**
     * Determines the main font colour of the style guide.
     * In most style guides this would simply be black
     *
     * @return - the colour representation as a PDColour
     */
    public abstract Color fontColour();

    /**
     * Determines the font colour of the style guide used for emphasis.
     * In most style guides this would simply be black
     *
     * @return - the colour representation as a PDColour
     */
    public abstract Color emphasisFontColour();

    /**
     * Determines the font colour of the style guide used for work references.
     * In most style guides this would simply be black
     *
     * @return - the colour representation as a PDColour
     */
    public abstract Color workFontColour();

    //// PARAGRAPH ////


    /**
     * Determines the string that will be inserted before every single sentence.
     * In most style guides this is used to insert a space or tab before every sentence.
     *
     * @return - the prefix as a String
     */
    public abstract String sentencePrefix();

    /**
     * Determines if the style guide allows emphasised text in sentences
     *
     * @return - a specification as an {@link AllowanceType}
     */
    public abstract AllowanceType allowsEmphasis();

    /**
     * Determines if the style guide allows whitespace in sentences
     *
     * @return - a specification as an {@link WhitespaceAllowanceType}
     */
    public abstract WhitespaceAllowanceType allowsWhitespace();

    public abstract float paragraphIndentation();


    //// Formatter Methods ////

    /**
     * Defines how the style sheet formats a simple text.
     * Examples could include adding exactly one whitespace after a punctuation mark.
     *
     * @param textBlock - the text block content with the enclosing quotation marks removed
     * @return - returns the formatted text content as it will be forwarded to the paragraph formatter.
     */
    public abstract String formatText(String textBlock);

    /**
     * Defines how the style sheet represents a date.
     * It may choose to use its default representation or create a custom string.
     * For example, MLA would use June 3, 1998.
     *
     * @param date - the date as a LocalDate, which needs to be turned into a string
     * @return - the date in the text format specified by the style guide
     */
    @NonNull
    public abstract String dateToString(final LocalDate date);

    /**
     * Defines how the style sheet renders the author names in the page numeration.
     *
     * @return - the non-null numeration value
     */
    @NonNull
    public abstract NumerationAuthorName numerationAuthorName();

    /**
     * Defines how many authors should be rendered in the numeration before rendering "et al."
     * If there is no limitation to this, this should be null.
     */
    public abstract Integer numerationLimit();
}
