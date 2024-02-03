package frontend.ast.config.style;

import error.MissingMemberException;
import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Contains all configurations of general chapters.
 * These configurations affect all chapters, no matter the chapter level.
 *
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class Chapters extends Node {

    /**
     * The content alignment used for all chapter types
     */
    private String chapterAlignment;

    /**
     * Configures if there is an empty line before, after or before and after a chapter
     */
    private String lineSpacing;

    /**
     * Throws an error exception if either field is blank
     */
    @Override
    public void checkForWarnings() {
        if (chapterAlignment != null && chapterAlignment.isBlank() ||
                lineSpacing != null && lineSpacing.isBlank())
            throw new MissingMemberException(MissingMemberException.ERR_MSG_1);
    }

}
