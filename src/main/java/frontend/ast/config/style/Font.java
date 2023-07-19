package frontend.ast.config.style;

import error.IncorrectFormatException;
import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;
import warning.UnlikelinessWarning;
import warning.WarningQueue;

@Getter
@Setter
public class Font extends Node {

    /**
     *  The non-blank name of the font provided by the user.
     *  Note that only the supported font names can be used.
     */
    private String name;

    /**
     *  The non-blank colour represented by a hexadecimal using a hashtag followed by six integers.
     *  For example, #000000 represents black.
     */
    private String colour;

    /**
     *  The non-blank size of the font using an integer larger than 0.
     *  The unit of the font size are points (pt).
     */
    private String size;

    /**
     *  A textual representation of the font node, which contains all formatted properties
     *
     * @return - the properties of the font node as a string
     */
    @Override
    public String toString() {
        return "\n\tFont{" +
                "name='" + name + '\'' +
                ", colour='" + colour + '\'' +
                ", size='" + size + '\'' +
                '}';
    }

    /**
     *  Produces a warning if a font size of more than 96 is used
     */
    @Override
    protected void checkForWarnings() {
        if (size != null) {
            try {
                if (Integer.parseInt(size) > 96)
                    WarningQueue.getInstance().enqueue(new UnlikelinessWarning("You are using a font size" +
                        " bigger than 96. Do you really want to use such a large font?"));
            } catch (IllegalArgumentException e) {
                throw new IncorrectFormatException("13: Integer larger than zero expected.");
            }
        }
    }

}
