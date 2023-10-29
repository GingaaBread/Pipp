package frontend.ast.paragraph;

import error.MissingMemberException;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 *  The Text node defines textual content inside a paragraph.
 * @author Gino Glink
 * @version 1.0
 * @since 1.0
 */
@Getter
@ToString
public class Text extends ParagraphInstruction {

    /**
     *  Marks the non-null, non-blank content of the text block.
     */
    private final String content;

    /**
     *  Creates a new text block with a String as its content
     * @param content the non-null, non-blank content
     * @throws MissingMemberException if the text content is blank
     */
    public Text(@NonNull final String content) {
        if (content.isBlank()) throw new MissingMemberException("1: A text component cannot be blank");
        this.content = content;
    }

    /**
     *  Does not produce warnings
     */
    @Override
    protected void checkForWarnings() { }

}
