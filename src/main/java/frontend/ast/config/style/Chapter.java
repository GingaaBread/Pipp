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
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class Chapter extends Node {

    /**
     * Allows changing the sentence font type separately from the other font types.
     * This allows using default sentences in chapters, and changing the individual sentence chapter level fonts.
     */
    private final Font sentenceFont = new Font();

    /**
     * Allows changing the emphasis font type separately from the other font types.
     * This allows using emphasised sentences in chapters, and changing the individual sentence chapter level fonts.
     */
    private final Font emphasisFont = new Font();

    /**
     * Allows changing the work font type separately from the other font types.
     * This allows referencing other works in chapters, and changing the individual sentence chapter level fonts.
     */
    private final Font workFont = new Font();

    /**
     * Determines the chapter level that is affected by this configuration.
     * Levels start at zero and must be integers.
     */
    private String affectedLevel;

    /**
     * Throws a warning if the affected level is blank and check the font nodes
     */
    @Override
    public void checkForWarnings() {
        if (affectedLevel != null && affectedLevel.isBlank())
            throw new MissingMemberException(MissingMemberException.ERR_MSG_1);

        sentenceFont.checkForWarnings();
        emphasisFont.checkForWarnings();
        workFont.checkForWarnings();
    }

}
