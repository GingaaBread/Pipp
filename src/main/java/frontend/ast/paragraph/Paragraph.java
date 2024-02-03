package frontend.ast.paragraph;

import creation.stamp.ParagraphStamp;
import frontend.ast.structure.BodyNode;
import frontend.ast.Node;
import lombok.NonNull;
import lombok.ToString;

import java.util.LinkedList;

/**
 * The paragraph node represents a paragraph, which consists of one or more paragraph instruction nodes.
 * When a paragraph is parsed, an object of this node should be created, and each parsed paragraph instruction
 * that is a child of that paragraph should be enqueued to this object. Once the paragraph has been parsed
 * completely, the object should be added to the AST.
 *
 * @version 1.0
 * @since 1.0
 */
@ToString
public class Paragraph extends BodyNode {

    /**
     * Contains all instructions in the order of appearance in a paragraph.
     * Note that a paragraph cannot be empty.
     */
    private final LinkedList<ParagraphInstruction> paragraphInstructions = new LinkedList<>();

    /**
     * Adds the paragraph instruction to the back of the queue
     *
     * @param instruction the non-null instruction that should be added
     */
    public void enqueueParagraphInstruction(@NonNull final ParagraphInstruction instruction) {
        paragraphInstructions.addLast(instruction);
    }

    /**
     * If handled, renders the paragraph on the current page.
     * Throws an IllegalStateException if the paragraph instruction list is empty, i.e. there are no instructions
     * (this would be an internal compiler error, not a user error), hence the exception type.
     */
    @Override
    public void handleBodyElement() {
        if (paragraphInstructions.isEmpty()) throw new IllegalStateException("Should not get an empty paragraph");
        ParagraphStamp.renderParagraph(paragraphInstructions);
    }

    /**
     * Does not produce warnings itself, but prompts the instructions inside the paragraph
     */
    @Override
    public void checkForWarnings() {
        paragraphInstructions.forEach(Node::checkForWarnings);
    }

}
