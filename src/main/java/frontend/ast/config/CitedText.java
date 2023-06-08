package frontend.ast.config;

import error.MissingMemberException;
import frontend.ast.Node;
import frontend.ast.config.style.Citation;
import lombok.Getter;

/**
 *  A cited text node either contains a citation or a text. Therefore, it serves as a pair that only allows
 *  one element to be not null.
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
@Getter
public class CitedText extends Node {

    /**
     *  The citation specified by the user.
     *  If used, the text property may not be used.
     */
    private Citation citation;

    /**
     *  The textual content specified by the user.
     *  If used, the citation property may not be used.
     */
    private String text;

    /**
     *  Creates a new CitedText, which is used as a citation
     *
     * @param citation - the citation specified by the user
     */
    public CitedText(Citation citation) {
        this.citation = citation;
    }

    /**
     *  Creates a new CitedText, which is used as a text
     *
     * @param text - the textual content specified by the user
     */
    public CitedText(String text) {
        this.text = text;
    }

    /**
     *  A textual representation of the cited text node, which contains the citation if the node is used as
     *  a citation, and the textual content if it is used as a text
     *
     * @return - the cited text node as a string
     */
    @Override
    public String toString() {
        return "\n\tCitedText{" +
                (citation != null ? "citation=" + citation
                : "text='" + text + '\'') + '}';
    }

    /**
     *  The cited text node does not produce warnings
     */
    @Override
    protected void checkForWarnings() {
        if (citation != null)
            citation.checkForWarnings();
    }

}
