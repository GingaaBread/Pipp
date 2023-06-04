package frontend.ast.config.style;

import error.MissingMemberException;
import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

/**
 *  The citation node represents a citation, which needs to have a source, the cited content, and
 *  some form of numeration (for example, the page number).
 *
 *  @since 1.0
 *  @version 1.0
 */
@Getter
@Setter
public class Citation extends Node {
    private String source;
    private String citedContent;
    private String numeration;

    @Override
    public String toString() {
        return "\nCitation{" +
                "source='" + source + '\'' +
                ", citedContent='" + citedContent + '\'' +
                ", numeration='" + numeration + '\'' +
                '}';
    }

    @Override
    public void checkForWarnings() { }

    @Override
    public void checkForErrors() {
        if (source != null && source.isBlank())
            throw new MissingMemberException("1: A text component cannot be blank. Component: Source");
        else if (citedContent != null && citedContent.isBlank())
            throw new MissingMemberException("1: A text component cannot be blank. Component: Cited Content");
        else if (numeration != null && numeration.isBlank())
            throw new MissingMemberException("1: A text component cannot be blank. Component: Numeration");
    }
}
