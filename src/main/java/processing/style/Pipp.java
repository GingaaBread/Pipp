package processing.style;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import processing.*;

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
    protected float indentationPadding() {
        return 0f;
    }

    @Override
    public float margin() {
        return 0f;
    }

    @Override
    public float spacing() {
        return 0f;
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
    public float numerationMargin() {
        return 0.5f;
    }

    @Override
    public AllowanceType allowsBold() {
        return null;
    }

    @Override
    public AllowanceType allowsItalic() {
        return null;
    }

    @Override
    public float paragraphIndentation() {
        return 0.5f;
    }

    @Override
    public String sentencePrefix() {
        return null;
    }

    @Override
    public WhitespaceAllowance allowsWhitespace() {
        return null;
    }

    @Override
    public StructureType requiredStructureBeforeEndnotes() {
        return null;
    }

    @Override
    public DocumentType documentType() {
        return null;
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
