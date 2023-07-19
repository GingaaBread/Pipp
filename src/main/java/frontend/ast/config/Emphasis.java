package frontend.ast.config;

import frontend.ast.Node;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 *  The emphasis node contains text that should be emphasised in the document.
 *  An example use case for this could be referencing foreign words in titles.
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
public class Emphasis extends Node {

    /**
     *  The text that should be emphasised in the document
     */
    @Getter
    private final String emphasisedText;

    public Emphasis(@NonNull final String emphasisedText) {
        this.emphasisedText = emphasisedText + " ";
    }

    /**
     *  A textual representation of the "emphasis" node, which contains the text that should be emphasised
     *
     * @return - the "emphasis" node as a string
     */
    @Override
    public String toString() {
        return "\n\t\tEmphasis{" +
                "emphasisedText=" + emphasisedText +
                '}';
    }


    /**
     *  The emphasis node does not produce warnings
     */
    @Override
    protected void checkForWarnings() {}

}
