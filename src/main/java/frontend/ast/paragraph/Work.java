package frontend.ast.paragraph;

import creation.Text;
import error.MissingMemberException;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import processing.Processor;

/**
 * The work node defines the reference to work inside a paragraph that should be emphasised.
 * Note that this trims the content of leading and trailing whitespace.
 *
 * @version 1.0
 * @since 1.0
 */
@Getter
@ToString
public class Work extends ParagraphInstruction {

    /**
     * Marks the non-null, non-blank work content that should be emphasised
     */
    private final String workContent;

    /**
     * Creates a new work instruction with a String as its work content
     *
     * @param content the non-null, non-blank work content that should be emphasised
     * @throws MissingMemberException if the text content is blank
     */
    public Work(@NonNull final String content) {
        this.workContent = content.trim();
    }

    /**
     * Produces an error if the content is blank
     */
    @Override
    public void checkForWarnings() {
        if (workContent.isBlank()) throw new MissingMemberException(MissingMemberException.ERR_MSG_1);
    }

    /**
     * Converts the node into a text component
     *
     * @return the work node as a text component ready to be rendered
     */
    @Override
    public Text[] toTextComponent() {
        return new Text[]{new Text(workContent, Processor.getWorkFontData())};
    }
}
