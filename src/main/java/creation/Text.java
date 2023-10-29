package creation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 *  The Text class resembles text that should be rendered on the document.
 *  It consists of the actual textual content, and a text style that determines how the text should look like.
 *
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
     *  Note that if wanting to edit the content (for example, in order to add a space before the content),
     *  this needs to be done prior to creating this text component.
     */
    private String content;

    /**
     *  The style of the content, which determines what kind of font is used during the render
     */
    private TextStyle style;

}