package frontend.ast.config.style;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

/**
 *  The structure node groups together the structure configuration
 *
 *  @since 1.0
 *  @version 1.0
 */
@Getter
@Setter
public class Structure extends Node {
    private final Paragraph paragraph = new Paragraph();
    private final Sentence sentence = new Sentence();
    private final Endnotes endnotes = new Endnotes();

    @Override
    public String toString() {
        return "\n\tStructure{" +
                "paragraph=" + paragraph +
                ", sentence=" + sentence +
                ", endnotes=" + endnotes +
                '}';
    }

    @Override
    protected void checkForWarnings() {
        paragraph.checkForWarnings();
        sentence.checkForWarnings();
        endnotes.checkForWarnings();
    }

    @Override
    protected void checkForErrors() {
        paragraph.checkForErrors();
        sentence.checkForErrors();
        endnotes.checkForErrors();
    }
}
