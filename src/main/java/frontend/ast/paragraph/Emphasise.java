package frontend.ast.paragraph;

import creation.Text;
import error.ConfigurationException;
import error.MissingMemberException;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import processing.AllowanceType;
import processing.Processor;
import warning.SelfCheckWarning;
import warning.WarningQueue;
import warning.WarningSeverity;

/**
 * The Emphasis node defines textual content inside a paragraph that should be emphasised.
 * Note that this trims the content of leading and trailing whitespace.
 *
 * @version 1.0
 * @since 1.0
 */
@Getter
@ToString
public class Emphasise extends ParagraphInstruction {

    /**
     * Marks the non-null, non-blank content that should be emphasised
     */
    private final String content;

    /**
     * Creates a new emphasis instruction with a String as its content
     *
     * @param content the non-null, non-blank content that should be emphasised
     * @throws MissingMemberException if the text content is blank
     */
    public Emphasise(@NonNull final String content) {
        this.content = content.trim();
    }

    /**
     * Produces an error if the content is blank
     */
    @Override
    public void checkForWarnings() {
        if (content.isBlank()) throw new MissingMemberException(MissingMemberException.ERR_MSG_1);
    }

    /**
     * Tries to convert the node into a text component if the used style allows the use of emphasis
     *
     * @return the emphasis node as a text component ready to be rendered
     */
    @Override
    public Text[] toTextComponent() {
        if (Processor.getAllowEmphasis() == AllowanceType.NO)
            throw new ConfigurationException(ConfigurationException.ERR_MSG_9);
        else if (Processor.getAllowEmphasis() == AllowanceType.IF_NECESSARY)
            WarningQueue.enqueue(new SelfCheckWarning(SelfCheckWarning.WARNING_MSG_1, WarningSeverity.LOW));

        return new Text[]{new Text(content, Processor.getEmphasisFontData())};
    }
}
