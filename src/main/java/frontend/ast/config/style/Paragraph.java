package frontend.ast.config.style;

import frontend.ast.Node;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Paragraph extends Node {
    private String indentation;

    @Override
    public String toString() {
        return "\nParagraph{" +
                "indentation='" + indentation + '\'' +
                '}';
    }
}
