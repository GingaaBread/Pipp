package processing.style;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class MLA9 extends StyleSheet {

    /**
     *  MLA 9 uses the standardised U.S. Letter
     */
    @Override
    protected PDRectangle pageFormat() {
        return PDRectangle.LETTER;
    }

    /**
     *  MLA uses the Times New Roman font
     */
    @Override
    protected PDFont font() {
        return PDType1Font.TIMES_ROMAN;
    }

    /**
     *  MLA uses a recommended default font size of 12
     */
    @Override
    protected int fontSize() {
        return 12;
    }

    /**
     *  MLA uses an indentation padding of 0.5 inches
     */
    @Override
    protected double indentationPadding() {
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
        if (!StyleSheet.isPunctuation(lastChar)) {
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
