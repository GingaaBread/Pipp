package frontend.ast.config;

import error.MissingMemberException;
import frontend.ast.Node;
import frontend.ast.config.style.Citation;
import lombok.Getter;

/**
 *  A cited text node either contains a citation or a text. Therefore, it serves as a pair that only allows
 *  one element to be not null.
 *
 *  @since 1.0
 *  @version 1.0
 */
@Getter
public class CitedText extends Node {
    private Citation citation;
    private String text;

    public CitedText(Citation citation) {
        this.citation = citation;
    }

    public CitedText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "\n\tCitedText{" +
                "citation=" + citation +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    protected void checkForWarnings() {
        if (citation != null)
            citation.checkForWarnings();
    }

    @Override
    protected void checkForErrors() {
        if (text != null && text.isBlank())
            throw new MissingMemberException("1: A text component cannot be blank");

        if (citation != null)
            citation.checkForErrors();
    }
}
