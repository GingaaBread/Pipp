package frontend.ast.config;

import creation.content.text.Text;
import frontend.ast.Node;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import processing.Processor;

import java.util.ArrayList;
import java.util.List;

/**
 * The title node contains the title of the document and consists of a variable amount of text components that are
 * allowed to be put in a title, called "TitleTexts".
 *
 * @version 1.0
 * @since 1.0
 */
@Getter
@ToString
public class Title extends Node {

    /**
     * A list of textual content within the title.
     * The title can include a mix of texts and citations.
     * To add an element to the list, use the add-method of this node.
     */
    private final ArrayList<TitleText> texts = new ArrayList<>();

    /**
     * Adds either a citation or a text to the title list
     *
     * @param text the text or citation as a cited text, which should be added to the text list
     */
    @NonNull
    public void add(@NonNull final TitleText text) {
        texts.add(text);
    }

    /**
     * Converts the list of title texts into a list of text components ready to be rendered on the document.
     *
     * @return the list of title texts as a list of text components
     */
    public @NonNull List<Text> asTextList() {
        return texts
                .stream()
                .map(titleText -> {
                    if (titleText.getWork() != null)
                        return new Text(titleText.getWork().getEmphasisedWork(), Processor.getWorkFontData());
                    else if (titleText.getEmphasis() != null)
                        return new Text(titleText.getEmphasis().getEmphasisedText(), Processor.getEmphasisFontData());
                    else
                        return new Text(titleText.getText(), Processor.getSentenceFontData());
                }).toList();
    }

    /**
     * Retrieves all texts as a single string without taking formatting into consideration
     *
     * @return all string values of all texts as a single string separated by a space
     */
    public String getTextsSeparated() {
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
     * The title node does not produce warnings, but prompts the title texts to check for warnings
     */
    @Override
    public void checkForWarnings() {
        for (var text : texts) text.checkForWarnings();
    }

}
