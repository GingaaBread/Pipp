package frontend.ast.paragraph;

import error.MissingMemberException;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * The work node defines the reference to work inside a paragraph that should be emphasised.
 *
 * @author Gino Glink
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
        if (content.isBlank()) throw new MissingMemberException("1: A text component cannot be blank");
        this.workContent = content.trim();
    }

    /**
     * Does not produce warnings
     */
    @Override
    protected void checkForWarnings() {
        // No warnings
    }

}
