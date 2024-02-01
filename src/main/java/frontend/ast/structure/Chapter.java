package frontend.ast.structure;

import error.MissingMemberException;
import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Contains the input for a chapter instruction.
 * A chapter consists of a level (for example, a level 3 chapter could be 1.3.1) and a name.
 *
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class Chapter extends BodyNode {

    private int level;
    private String name;

    @Override
    public void checkForWarnings() {
        if (level <= 0) throw new IllegalStateException("Chapter level should never be non-positive");
        if (name != null && name.isBlank()) throw new MissingMemberException(MissingMemberException.ERR_MSG_1);
    }

    @Override
    public void handleBodyElement() {

    }
}
