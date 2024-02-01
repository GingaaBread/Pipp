package frontend.ast.config.style;

import error.MissingMemberException;
import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Contains all configurations for a single chapter configuration.
 * All chapter configurations affect a single chapter level.
 *
 * @since 1.0
 * @version 1.0
 */
@Getter
@Setter
@ToString
public class Chapter extends Node {

    private String affectedLevel;
    private Font font = new Font();

    @Override
    public void checkForWarnings() {
        if (affectedLevel != null && affectedLevel.isBlank())
            throw new MissingMemberException(MissingMemberException.ERR_MSG_1);
        font.checkForWarnings();
    }

}
