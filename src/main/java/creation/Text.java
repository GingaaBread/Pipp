package creation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;

/**
 *  The Text class resembles text that should be rendered on the document.
 *  It consists of the actual textual content, and a font style that determines how the text should look like.
 *  The font style determines the base font, the font size and the font colour.
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
@AllArgsConstructor
@Getter
@ToString
public class Text {

    /**
     *  The actual textual content, exactly how it should be rendered on the page.
     */
    private String content;

    /**
     *  The base font that should be used when rendering the text.
     */
    private PDFont font;

    /**
     *  The size of the font in points.
     */
    private float fontSize;

    /**
     *  The colour of the font as an AWT colour.
     */
    private Color fontColour;

}