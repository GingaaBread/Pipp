package processing.style;

import lombok.NonNull;
import lombok.Setter;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import processing.*;

import java.awt.*;
import java.time.LocalDate;

@Setter
public class MLA9 extends StyleGuide {

    /**
     *  MLA 9 uses the standardised U.S. Letter
     */
    @Override
    public PDRectangle pageFormat() {
        return PDRectangle.LETTER;
    }

    /**
     *  MLA defines any easily readable font, which has a strongly contrasting italic version.
     *  Pipp uses the Times Roman font for MLA9.
     */
    @Override
    public PDFont font() {
        return PDType1Font.TIMES_ROMAN;
    }

    /**
     *  MLA defines any easily readable font, which has a strongly contrasting default version.
     *  Pipp uses the italic Times font for MLA9.
     */
    @Override
    public PDFont emphasisedFont() {
        return PDType1Font.TIMES_ITALIC;
    }

    /// TODO: Hyphenation configuration: MLA does not allow hyphenation

    /**
     *  MLA defines a font size of anywhere between 11 and 13 points.
     *  Pipp uses a font size of 12.
     */
    @Override
    public int fontSize() {
        return 12;
    }

    /**
     *  MLA does not define a font colour.
     *  Pipp uses a black font colour.
     */
    @Override
    public Color fontColour() {
        return Color.black;
    }

    /**
     *  MLA defines a margin of one inch to the top and bottom and both sides of the text
     */
    @Override
    public float margin() {
        return 1f;
    }

    /**
     *  MLA defines double-spacing for the entire document and all its elements
     */
    @Override
    public float spacing() {
        return 2f;
    }

    // TODO: Header configurations: MLA flushes with the left margin

    // TODO: Title configurations: MLA uses double spaced centered text with no italic or underlined text. It needs to be put in quotation marks, boldface, or typed out in capital letters. capitalisation rules.

    /**
     *  MLA uses arabic numeration
     */
    @Override
    public NumerationType numerationType() {
        return NumerationType.ARABIC;
    }

    /**
     *  MLA displays the page number in the top right corner
     */
    @Override
    public NumerationPosition numerationPosition() {
        return NumerationPosition.TOP_RIGHT;
    }

    @Override
    public @NonNull NumerationAuthorName numerationAuthorName() {
        return NumerationAuthorName.LAST_NAME;
    }

    /**
     *  MLA displays the page number half an inch away from the top of the document
     */
    @Override
    public float numerationMargin() {
        return 0.5f;
    }

    // TODO: Numeration author type: MLA when multiple authors: separate by comma until it does not fit in the line any more. Then only the page number.

    // TODO: Chapter configuration. MLA allows chapter titles as long as they are equal. No internal heading should have only one instance. Heading flush left margin, not indented or centered. A line space above and below a heading. Avoid numbers and letters. same capitalisation as headings.

    /**
     *  MLA does not allow bold text
     */
    @Override
    public AllowanceType allowsBold() {
        return AllowanceType.NO;
    }

    /**
     *  MLA does only allow italic text if absolutely necessary
     */
    @Override
    public AllowanceType allowsItalic() {
        return AllowanceType.IF_NECESSARY;
    }

    /**
     *  MLA defines that the first line of a paragraph should be indented half an inch from the left margin
     */
    @Override
    public float paragraphIndentation() {
        return 0.5f;
    }

    // TODO: Block indentation configuration: MLA also indents it half an inch from the left

    /**
     *  MLA inserts a space before every sentence
     */
    @Override
    public String sentencePrefix() {
        return " ";
    }

    /**
     *  MLA allows whitespace if it has been escaped
     */
    @Override
    public WhitespaceAllowanceType allowsWhitespace() {
        return WhitespaceAllowanceType.ESCAPED;
    }

    /**
     *  MLA demands that the bibliography section should appear after the endnotes section!!!
     *  TODO: Change
     */
    @Override
    public StructureType requiredStructureBeforeEndnotes() {
        return StructureType.BIBLIOGRAPHY;
    }

    // TODO: Works cited config: 1 inch from the top. If the list has one entry: name "Work Cited". Double space between the heading and the first entry. Begin each line with left margin flush. If more than one line: indent half an inch from left margin (called hanging indent)

    // TODO: Tables and illustrations config: Table name is Table [ARABIC NUMERAL].title and description flush left separate lines above the actual table. double-space.dividing lines. Other illustrations (graph, chart, image, ...) labeled Fig. [ARABIC] also receive a caption. Musical illustrations are labeled Ex. [ARABIC]

    // TODO: Lists

    /**
     *  MLA uses a default document type of PAPER
     */
    @Override
    public DocumentType documentType() {
        return DocumentType.PAPER;
    }

    /**
     * MLA uses the following rules:
     *  A full-stop is added if the last character is not a punctuation mark
     *  Exactly one space is inserted before a new in-line sentence.
     */
    @Override
    public String formatText(String textBlock) {
        // Texts are trimmed
        textBlock = textBlock.trim();

        if (textBlock.isBlank()) throw new IllegalArgumentException("Blank text block processed.");

        // todo: use the method instead
        // White space of more than one space is silently removed within texts.
        textBlock = textBlock.replaceAll("\\s+", " ");

        // Add a full stop if there is no punctuation
        var lastChar = textBlock.charAt(textBlock.length() - 1);
        if (!StyleGuide.isPunctuation(lastChar)) {
            textBlock += ".";
        }

        // todo: Exactly one space is inserted before a new in-line sentence.

        return textBlock;
    }

    // 1. The first sentences of paragraphs are indented (using tabulation).
    @Override
    public String formatParagraph(String[] formattedTextBlocks) {
        return null;
    }

    @Override
    @NonNull
    public String dateToString(final @NonNull LocalDate date) {
        final var monthAsLowerCase = date.getMonth().toString().toLowerCase();
        final var firstUppercase = String.valueOf(monthAsLowerCase.charAt(0)).toUpperCase();
        return firstUppercase + monthAsLowerCase.substring(1) + " " + date.getDayOfMonth()
                + " " + date.getYear();
    }

}
