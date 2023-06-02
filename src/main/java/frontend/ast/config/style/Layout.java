package frontend.ast.config.style;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Layout extends Node {
    private String width;
    private String height;
    private String margin;
    private String spacing;

    @Override
    public String toString() {
        return "\nLayout{" +
                "width='" + width + '\'' +
                ", height='" + height + '\'' +
                ", margin='" + margin + '\'' +
                ", spacing='" + spacing + '\'' +
                '}';
    }
}
