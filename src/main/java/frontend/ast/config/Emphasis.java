package frontend.ast.config;

import error.MissingMemberException;
import frontend.ast.Node;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * The emphasis node contains text that should be emphasised in the document.
 * An example use case for this could be referencing foreign words in titles.
 *
 * @version 1.0
 * @since 1.0
 */
@ToString
public class Emphasis extends Node {

    /**
     * The text that should be emphasised in the document
     */
    @Getter
    private final String emphasisedText;

    /**
     * Creates emphasis using the specified text as its content
     *
     * @param emphasisedText the non-null emphasised textual content
     */
    public Emphasis(@NonNull final String emphasisedText) {
        this.emphasisedText = emphasisedText;
    }

    /**
     * The emphasis node does not produce warnings
     */
    @Override
    public void checkForWarnings() {
        if (emphasisedText.isBlank()) throw new MissingMemberException(MissingMemberException.ERR_MSG_1);
    }

}
