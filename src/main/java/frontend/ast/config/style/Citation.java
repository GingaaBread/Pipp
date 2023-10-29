package frontend.ast.config.style;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *  The citation node represents a citation, which needs to have a source, the cited content, and
 *  some form of numeration (for example, the page number).
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
@Getter
@Setter
@ToString
public class Citation extends Node {

    /**
     *  The source of the citation as provided by the user.
     *  The source may not be blank and must exist in the user's bibliography.
     */
    private String source;

    /**
     *  The non-blank content provided by the user, which is cited.
     */
    private String citedContent;

    /**
     *  The non-blank numeration type provided by the user.
     *  For example, a book would use a page number as a citation numeration (page number 25).
     *  A TV-show would use a season an episode as a citation numeration (season 1, episode 5).
     */
    private String numeration;

    /**
     *  A citation node does not produce warnings
     */
    @Override
    public void checkForWarnings() { }

}
