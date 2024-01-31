package processing;

import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;

public record FontData(PDFont font, float fontSize, Color fontColor) {
}