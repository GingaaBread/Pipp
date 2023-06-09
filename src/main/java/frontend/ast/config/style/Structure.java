package frontend.ast.config.style;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

/**
 *  The structure node groups together the structure configuration
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
@Getter
@Setter
public class Structure extends Node {

    /**
     *  The paragraph node contains information about how paragraph structures should be styled.
     *  This includes adding a prefix string before each paragraph, such as a "tab", for example.
     */
    private final Paragraph paragraph = new Paragraph();

    /**
     *  The sentence node contains information about how sentence structures should be styled.
     *  This includes allowing or not allowing the user to use bold text in sentences, for example.
     */
    private final Sentence sentence = new Sentence();

    /**
     *  The endnotes node contains information about how endnotes structures should be styled.
     *  This includes making sure that endnotes only appear after bibliographies, for example.
     */
    private final Endnotes endnotes = new Endnotes();

    /**
     *  A textual representation of the structure node, which contains all formatted properties.
     *
     * @return - the properties of the structure node as a string
     */
    @Override
    public String toString() {
        return "\n\tStructure{" +
                "paragraph=" + paragraph +
                ", sentence=" + sentence +
                ", endnotes=" + endnotes +
                '}';
    }

    /**
     *  The structure node does not produce warnings
     */
    @Override
    protected void checkForWarnings() {
        paragraph.checkForWarnings();
        sentence.checkForWarnings();
        endnotes.checkForWarnings();
    }

}
