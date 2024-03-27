package frontend.ast.config.style;

import creation.content.text.Text;
import error.MissingMemberException;
import frontend.ast.paragraph.ParagraphInstruction;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import processing.Processor;

/**
 * The citation node represents a citation, which needs to have a source, the cited content, and
 * some form of numeration (for example, the page number).
 *
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class Citation extends ParagraphInstruction {

    /**
     * The source of the citation as provided by the user.
     * The source may not be blank and must exist in the user's bibliography.
     */
    private String source;

    /**
     * The non-blank content provided by the user, which is cited.
     */
    private String citedContent;

    /**
     * The non-blank numeration type provided by the user.
     * For example, a book would use a page number as a citation numeration (page number 25).
     * A TV-show would use a season an episode as a citation numeration (season 1, episode 5).
     */
    private String numeration;

    /**
     * Checks if any fields are blank or if significant fields do not exist
     */
    @Override
    public void checkForWarnings() {
        if (source == null) throw new MissingMemberException("10: Cannot use a citation without referencing a " +
                "source from the bibliography.");
        if (source.isBlank()) throw new MissingMemberException(MissingMemberException.ERR_MSG_1);
        if (citedContent != null && citedContent.isBlank())
            throw new MissingMemberException(MissingMemberException.ERR_MSG_1);
        if (numeration != null && numeration.isBlank())
            throw new MissingMemberException(MissingMemberException.ERR_MSG_1);
    }

    @Override
    public Text[] toTextComponent() {
        return Processor.processCitation(this);
    }
}
