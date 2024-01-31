package creation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.apache.pdfbox.pdmodel.font.PDFont;
import processing.FontData;

import java.awt.*;

/**
 * The Text class defines text that should be rendered on the document.
 * It consists of the actual textual content, and a font style that determines how the text should look like.
 * The font style determines the base font, the font size and the font colour.
 *
 * @version 1.0
 * @since 1.0
 */
@AllArgsConstructor
@Getter
@ToString
public class Text {

    /**
     * The actual textual content, exactly how it should be rendered on the page.
     */
    private String content;
    /**
     * The base font that should be used when rendering the text.
     */
    private PDFont font;
    /**
     * The size of the font in points.
     */
    private float fontSize;
    /**
     * The colour of the font as an AWT colour.
     */
    private Color fontColour;

    public Text(@NonNull final String content, @NonNull final FontData fontData) {
        this.content = content;
        font = fontData.font();
        fontSize = fontData.fontSize();
        fontColour = fontData.fontColor();
    }

}