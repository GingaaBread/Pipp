package frontend.ast.config;

import frontend.ast.Node;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 *  The emphasis node contains text that should be emphasised in the document.
 *  An example use case for this could be referencing foreign words in titles.
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
@ToString
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
     *  The emphasis node does not produce warnings
     */
    @Override
    protected void checkForWarnings() {}

}
