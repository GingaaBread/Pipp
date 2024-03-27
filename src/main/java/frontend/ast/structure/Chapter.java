package frontend.ast.structure;

import creation.handler.ChapterHandler;
import frontend.ast.config.Title;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Contains the input for a chapter instruction.
 * A chapter consists of a level (for example, a level 3 chapter could be 1.3.1) and a name consisting of textual
 * elements (text, work references, emphasis, ...).
 *
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class Chapter extends BodyNode {

    /**
     * The indentation level of the chapter is also its chapter level.
     * If the keyword has been indented three times, it is considered a level-3 chapter, or in other words a
     * sub-subchapter.
     */
    private int level;

    /**
     * Allows the use of title textual elements (work references, emphasis, text), etc. instead of
     * just using a single string for the chapter name.
     */
    private Title title = new Title();

    /**
     * Throws a compiler exception if the level should be non-positive.
     * Note that this is not a user error, but a program error if this happens.
     */
    @Override
    public void checkForWarnings() {
        if (level < 0) throw new IllegalStateException("Chapter level should never be negative");
        title.checkForWarnings();
    }

    /**
     * If handled in the creation stage, this prompts the chapter handler to handle this chapter instance
     */
    @Override
    public void handleBodyElement() {
        ChapterHandler.handleChapterDeclaration(this);
    }

}
