package frontend.ast.paragraph;

import error.MissingMemberException;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import processing.Processor;

/**
 * The Text node defines textual content inside a paragraph.
 * Note that this trims the content of leading and trailing whitespace.
 *
 * @version 1.0
 * @since 1.0
 */
@Getter
@ToString
public class Text extends ParagraphInstruction {

    /**
     * Marks the non-null, non-blank content of the text block.
     */
    private final String content;

    /**
     * Creates a new text block with a String as its content
     *
     * @param content the non-null, non-blank content
     * @throws MissingMemberException if the text content is blank
     */
    public Text(@NonNull final String content) {
        this.content = content.trim();
    }

    /**
     * Produces an error if the content is blank
     */
    @Override
    public void checkForWarnings() {
        if (content.isBlank()) throw new MissingMemberException(MissingMemberException.ERR_MSG_1);
    }

    /**
     * Converts the node into a text component
     *
     * @return the text node as a text component ready to be rendered
     */
    @Override
    public creation.Text[] toTextComponent() {
        return new creation.Text[]{new creation.Text(content, Processor.getSentenceFont(),
                Processor.getSentenceFontSize(), Processor.getSentenceFontColour())};
    }
}
