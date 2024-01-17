package processing.style;

import creation.ContentAlignment;
import lombok.NonNull;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import processing.*;

import java.awt.*;
import java.time.LocalDate;

public class TUDO extends StyleGuide {
    @Override
    public DocumentType documentType() {
        return DocumentType.PAPER;
    }

    @Override
    public ContentAlignment defaultImageAlignment() {
        return ContentAlignment.RIGHT;
    }

    @Override
    public PDRectangle pageFormat() {
        return PDRectangle.A3;
    }

    @Override
    public float margin() {
        return 2;
    }

    @Override
    public float spacing() {
        return 1;
    }

    @Override
    public NumerationType numerationType() {
        return NumerationType.ROMAN;
    }

    @Override
    public NumerationPosition numerationPosition() {
        return NumerationPosition.TOP_LEFT;
    }

    @Override
    public float numerationMargin() {
        return 0;
    }

    @Override
    public PDFont font() {
        return PDType1Font.COURIER;
    }

    @Override
    public PDFont emphasisFont() {
        return PDType1Font.HELVETICA_BOLD;
    }

    @Override
    public PDFont workFont() {
        return PDType1Font.HELVETICA_BOLD;
    }

    @Override
    public int fontSize() {
        return 11;
    }

    @Override
    public int emphasisFontSize() {
        return 11;
    }

    @Override
    public int workFontSize() {
        return 11;
    }

    @Override
    public Color fontColour() {
        return Color.black;
    }

    @Override
    public Color emphasisFontColour() {
        return Color.red;
    }

    @Override
    public Color workFontColour() {
        return Color.blue;
    }

    @Override
    public String sentencePrefix() {
        return null;
    }

    @Override
    public AllowanceType allowsEmphasis() {
        return AllowanceType.NO;
    }

    @Override
    public float paragraphIndentation() {
        return 0;
    }

    @Override
    public String formatText(String textBlock) {
        return null;
    }

    @Override
    public @NonNull String dateToString(LocalDate date) {
        return date.getYear() + "";
    }

    @Override
    public @NonNull NumerationAuthorName numerationAuthorName() {
        return NumerationAuthorName.FIRST_NAME;
    }

    @Override
    public Integer numerationLimit() {
        return 1;
    }
}
