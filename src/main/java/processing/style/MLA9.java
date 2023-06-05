package processing.style;

import lombok.Setter;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import processing.NumerationPosition;
import processing.NumerationType;

import java.awt.*;

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

    @Override
    public Color fontColour() {
        return Color.black;
    }

    /**
     *  MLA uses an indentation padding of 0.5 inches
     */
    @Override
    protected double indentationPadding() {
        return 0.5d;
    }

    /**
     *  MLA uses a margin of 1 inch
     */
    @Override
    public double margin() {
        return 1;
    }

    /**
     *  MLA uses double-spacing
     */
    @Override
    public double spacing() {
        return 2d;
    }

    /**
     *  MLA uses arabic numeration
     */
    @Override
    public NumerationType numerationType() {
        return NumerationType.ARABIC;
    }

    /**
     *  MLA uses arabic numeration
     *  TODO Check accuracy
     */
    @Override
    public NumerationPosition numerationPosition() {
        return NumerationPosition.BOTTOM;
    }

    @Override
    public double numerationMargin() {
        return 0.5d;
    }

    /**
     *  MLA does not allow strong markdown
     */
    @Override
    protected boolean allowsStrongMarkdownInTexts() {
        return false;
    }

    /**
     *  MLA allows italic markdown if absolutely necessary
     */
    @Override
    protected boolean allowsItalicMarkdownInTexts() {
        return true;
    }

    // todo: check
    @Override
    public double paragraphIndentation() {
        return 0.5d;
    }

    /**
     * MLA uses the following rules:
     *  White space of more than one space is silently removed within texts.
     *  A full-stop is added if the last character is not a punctuation mark
     *  Exactly one space is inserted before a new in-line sentence.
     */
    @Override
    public String formatText(String textBlock) {
        // Texts are trimmed
        textBlock = textBlock.trim();

        if (textBlock.isBlank()) throw new IllegalArgumentException("Blank text block processed.");

        // White space of more than one space is silently removed within texts.
        textBlock = textBlock.replaceAll("\\s+", " ");

        // Add a full stop if there is no punctuation
        var lastChar = textBlock.charAt(textBlock.length() - 1);
        if (!StyleGuide.isPunctuation(lastChar)) {
            textBlock += ".";
        }

        // todo: White space separating a character from an unescaped punctuation mark is silently removed.

        // todo: Exactly one space is inserted before a new in-line sentence.

        return textBlock;
    }

    // 1. The first sentences of paragraphs are indented (using tabulation).
    @Override
    public String formatParagraph(String[] formattedTextBlocks) {
        return null;
    }
}
