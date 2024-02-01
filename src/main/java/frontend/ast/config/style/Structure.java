package frontend.ast.config.style;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

/**
 * The structure node groups together the structure configuration, consisting of paragraph, sentence, work, and
 * emphasis structure configurations.
 *
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class Structure extends Node {

    /**
     * The paragraph node contains information about how paragraph structures should be styled.
     * This includes adding a prefix string before each paragraph, such as a "tab", for example.
     */
    private final Paragraph paragraph = new Paragraph();

    /**
     * The sentence node contains information about how sentence structures should be styled.
     * This includes allowing or not allowing the user to use bold text in sentences, for example.
     */
    private final Sentence sentence = new Sentence();

    /**
     * This work structure node contains information about how work structures should be styled.
     */
    private final WorkStructure work = new WorkStructure();

    /**
     * This emphasis structure node contains information about how emphasise structures should be styled.
     */
    private final EmphasisStructure emphasis = new EmphasisStructure();

    /**
     * Contains all configured chapter levels
     */
    private final List<Chapter> chapters = new LinkedList<>();

    /**
     * The structure node does not produce warnings, but prompts the structure nodes to check for warnings
     */
    @Override
    public void checkForWarnings() {
        paragraph.checkForWarnings();
        sentence.checkForWarnings();
        work.checkForWarnings();
        emphasis.checkForWarnings();
        chapters.forEach(Chapter::checkForWarnings);
    }

}
