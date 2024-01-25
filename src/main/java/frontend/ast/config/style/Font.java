package frontend.ast.config.style;

import error.IncorrectFormatException;
import error.MissingMemberException;
import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import warning.UnlikelinessWarning;
import warning.WarningQueue;
import warning.WarningSeverity;

/**
 * The font configuration contains information about its family name (Arial, Impact, etc.), colour as a hex-string,
 * and its size.
 *
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class Font extends Node {

    /**
     * The non-blank name of the font provided by the user.
     * Note that only the supported font names can be used.
     */
    private String name;

    /**
     * The non-blank colour represented by a hexadecimal using a hashtag followed by six integers.
     * For example, #000000 represents black.
     */
    private String colour;

    /**
     * The non-blank size of the font using an integer larger than 0.
     * The unit of the font size are points (pt).
     */
    private String size;

    /**
     * Produces a warning if a font size of more than 96 is used and an error if any field is blank
     */
    @Override
    public void checkForWarnings() {
        if (name != null && name.isBlank() ||
                colour != null && colour.isBlank() ||
                size != null && size.isBlank())
            throw new MissingMemberException(MissingMemberException.ERR_MSG_1);

        if (size != null) {
            try {
                if (Integer.parseInt(size) > 96)
                    WarningQueue.enqueue(new UnlikelinessWarning("2: You are using a font size" +
                            " bigger than 96. Do you really want to use such a large font?", WarningSeverity.HIGH));
            } catch (IllegalArgumentException e) {
                throw new IncorrectFormatException("13: Integer larger than zero expected.");
            }
        }
    }

}
