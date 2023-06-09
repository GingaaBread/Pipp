package frontend.ast.config;

import frontend.ast.Node;
import lombok.Getter;
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
    @Getter
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
     *  Retrieves all texts as a single string without taking formatting into consideration
     *
     * @return - all string values of all texts as a single string separated by a space
     */
    public String getTextsUnformatted() {
        final var textBuilder = new StringBuilder();

        for (int i = 0; i < texts.size(); i++) {
            var text = texts.get(i);
            if (text.getText() != null) textBuilder.append(text.getText());
            else if (text.getEmphasis() != null) textBuilder.append(text.getEmphasis().getEmphasisedText());
            else if (text.getWork() != null) textBuilder.append(text.getWork().getEmphasisedWork());
            else throw new UnsupportedOperationException("Title text type is not yet supported!");

            if (i != texts.size() - 1) textBuilder.append(" ");
        }

        return textBuilder.toString();
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
