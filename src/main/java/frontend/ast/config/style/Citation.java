package frontend.ast.config.style;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

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
}
