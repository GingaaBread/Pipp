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
     *  MLA uses the Times New Roman font
     */
    @Override
    public PDFont font() {
        return PDType1Font.TIMES_ROMAN;
    }

    /**
     *  MLA uses a recommended default font size of 12
     */
    @Override
    public int fontSize() {
        return 12;
    }

    /**
     *  MLA uses a black font
     */
    @Override
    public Color fontColour() {
        return Color.black;
    }

    /**
     *  MLA uses a margin of 1 inch
     */
    @Override
    public float margin() {
        return 1f;
    }

    /**
     *  MLA uses double-spacing
     */
    @Override
    public float spacing() {
        return 2f;
    }

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

    /**
     *  MLA displays the page number half an inch away from the top and right sides of the document
     */
    @Override
    public float numerationMargin() {
        return 0.5f;
    }

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

    // todo: replace by "\t" as the prefix
    @Override
    public float paragraphIndentation() {
        return 0.5f;
    }

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
     *  MLA demands that the bibliography section should appear before the endnotes section
     */
    @Override
    public StructureType requiredStructureBeforeEndnotes() {
        return StructureType.BIBLIOGRAPHY;
    }

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
                + ", " + date.getYear();
    }

}
