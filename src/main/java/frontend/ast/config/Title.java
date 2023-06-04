package frontend.ast.config;

import error.MissingMemberException;
import frontend.ast.Node;

import java.util.ArrayList;

/**
 *  The title node contains the title of the document
 *
 *  @since 1.0
 *  @version 1.0
 */
public class Title extends Node {
    private final ArrayList<CitedText> texts = new ArrayList<>();

    public void add(CitedText text) {
        texts.add(text);
    }

    @Override
    public String toString() {
        return "\n\tTitle{" +
                "texts=" + texts +
                '}';
    }

    @Override
    protected void checkForWarnings() {
        for (var text : texts) text.checkForWarnings();
    }

    @Override
    protected void checkForErrors() {
        if (texts.isEmpty())
            throw new MissingMemberException("1: A text component cannot be blank");

        for (var text : texts) text.checkForErrors();
    }
}
