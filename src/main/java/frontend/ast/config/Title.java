package frontend.ast.config;

import frontend.ast.Node;
import lombok.NonNull;

import java.util.ArrayList;

/**
 *  The title node contains the title of the document
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
public class Title extends Node {

    /**
     *  A list of textual content within the title.
     *  The title can include a mix of texts and citations.
     *  To add an element to the list, use the add-method of this node.
     */
    private final ArrayList<TitleText> texts = new ArrayList<>();

    /**
     *  Adds either a citation or a text to the title list
     *
     * @param text - the text or citation as a cited text, which should be added to the text list
     */
    @NonNull
    public void add(@NonNull final TitleText text) {
        texts.add(text);
    }

    /**
     *  A textual representation of the title node, which contains the formatted text list
     *
     * @return - the title node as a string
     */
    @Override
    public String toString() {
        return "\n\tTitle{" +
                "texts=" + texts +
                '}';
    }

    /**
     *  The title node does not produce warnings
     */
    @Override
    protected void checkForWarnings() {
        for (var text : texts) text.checkForWarnings();
    }

}
