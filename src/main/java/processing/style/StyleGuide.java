package processing.style;

import creation.content.ContentAlignment;
import creation.content.text.Text;
import lombok.NonNull;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import processing.AllowanceType;
import processing.FontData;
import processing.bibliography.BibliographySource;
import processing.numeration.NumerationAuthorName;
import processing.numeration.NumerationPosition;
import processing.numeration.NumerationType;

import java.time.LocalDate;

/**
 * Defines traits and rules that a formal style guide uses.
 * An example style guide is the MLA style in its 9th edition.
 * To create a custom style sheet, extend this class and specify all values.
 *
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

    /**
     * Determines the default alignment of images that is applied when the user defines no alignment.
     *
     * @return the alignment as an enumeration constant
     */
    public abstract ContentAlignment defaultImageAlignment();

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


    public abstract FontData sentenceFontData();

    public abstract FontData workFontData();

    public abstract FontData emphasisFontData();

    public abstract FontData[] chapterSentenceFontData();

    public abstract FontData[] chapterWorkFontData();

    public abstract FontData[] chapterEmphasisFontData();


    //// PARAGRAPH ////

    /**
     * Determines if the style guide allows emphasised text in sentences
     *
     * @return - a specification as an {@link AllowanceType}
     */
    public abstract AllowanceType allowsEmphasis();

    public abstract float paragraphIndentation();


    //// Formatter Methods ////

    /**
     * Defines how the style sheet formats a simple text.
     * Examples could include adding exactly one whitespace after a punctuation mark.
     *
     * @param textBlock - the text block content with the enclosing quotation marks removed
     * @return - returns the formatted text content as it will be forwarded to the paragraph formatter.
     */
    public abstract String formatText(final String textBlock);

    /**
     * Defines how the style guide formats a citation.
     *
     * @param referenceSource the bibliography source that should be cited. The style guide may change its format
     *                        depending on the source type (for example, a book may be different from a poem)
     * @param content         the textual content that should be cited from the source. The style guide may insert quotation
     *                        marks, etc.
     * @param numeration      the numeration of the citation. For example, page numbers. The style guide may wrap it in
     *                        parentheses.
     * @return the result as an array of text components, which enables the backend to format different parts in
     * different font styles
     */
    public abstract Text[] formatCitation(final BibliographySource referenceSource, final String content,
                                          final String numeration);

    public abstract String formatBibliographyTitle(final BibliographySource[] sources);

    public abstract Text[] formatBibliographyEntry(final BibliographySource entry);

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
