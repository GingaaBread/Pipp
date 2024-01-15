package frontend.ast.paragraph;

import error.MissingMemberException;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * The Emphasis node defines textual content inside a paragraph that should be emphasised.
 *
 * @version 1.0
 * @since 1.0
 */
@Getter
@ToString
public class Emphasise extends ParagraphInstruction {

    /**
     * Marks the non-null, non-blank content that should be emphasised
     */
    private final String content;

    /**
     * Creates a new emphasis instruction with a String as its content
     *
     * @param content the non-null, non-blank content that should be emphasised
     * @throws MissingMemberException if the text content is blank
     */
    public Emphasise(@NonNull final String content) {
        if (content.isBlank()) throw new MissingMemberException("1: A text component cannot be blank");
        this.content = content.trim();
    }

    /**
     * Does not produce warnings
     */
    @Override
    protected void checkForWarnings() {
        // No warnings
    }

}
