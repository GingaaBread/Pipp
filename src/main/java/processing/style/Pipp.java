package processing.style;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import processing.NumerationPosition;
import processing.NumerationType;

import java.awt.*;

public class Pipp extends StyleGuide {
    @Override
    public PDRectangle pageFormat() {
        return null;
    }

    @Override
    public PDFont font() {
        return null;
    }

    @Override
    public int fontSize() {
        return 0;
    }

    @Override
    public Color fontColour() {
        return null;
    }

    @Override
    protected double indentationPadding() {
        return 0;
    }

    @Override
    public double margin() {
        return 0;
    }

    @Override
    public double spacing() {
        return 0;
    }

    @Override
    public NumerationType numerationType() {
        return NumerationType.ARABIC;
    }

    @Override
    public NumerationPosition numerationPosition() {
        return NumerationPosition.TOP_RIGHT;
    }

    @Override
    public double numerationMargin() {
        return 0.5d;
    }

    @Override
    protected boolean allowsStrongMarkdownInTexts() {
        return false;
    }

    @Override
    protected boolean allowsItalicMarkdownInTexts() {
        return false;
    }

    @Override
    public double paragraphIndentation() {
        return 0.5d;
    }

    @Override
    public String formatText(String textBlock) {
        return null;
    }

    @Override
    public String formatParagraph(String[] formattedTextBlocks) {
        return null;
    }
}
