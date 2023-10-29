package frontend.ast.config;

import frontend.ast.Node;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 *  A title text node either contains a type of emphasis or a text. Therefore, it serves as a triple that only allows
 *  one element to be not null. Emphasis can be given using the "emphasise" instruction, or via the work instruction.
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
@Getter
@ToString
public class TitleText extends Node {

    /**
     *  The emphasis specified by the user.
     *  If used, the work and text properties may not be used.
     */
    private Emphasis emphasis;

    /**
     *  The work emphasis specified by the user.
     *  If used, the emphasis and text properties may not be used.
     */
    private Work work;

    /**
     *  The textual content specified by the user.
     *  If used, the emphasis and work properties may not be used.
     */
    private String text;

    /**
     *  Creates a new TitleText, which is used as emphasis
     *
     * @param emphasis - the emphasis specified by the user
     */
    public TitleText(@NonNull final Emphasis emphasis) {
        this.emphasis = emphasis;
    }

    /**
     *  Creates a new CitedText, which is used as a text
     *
     * @param text - the textual content specified by the user
     */
    public TitleText(@NonNull final String text) {
        this.text = text + " ";
    }

    /**
     *  Creates a new TitleText, which is used as a work emphasis
     *
     * @param work - the work emphasis specified by the user
     */
    public TitleText(@NonNull final Work work) {
        this.work = work;
    }

    /**
     *  The cited text node does not produce warnings
     */
    @Override
    protected void checkForWarnings() {
        if (emphasis != null)
            emphasis.checkForWarnings();

        if (work != null)
            work.checkForWarnings();
    }

}
