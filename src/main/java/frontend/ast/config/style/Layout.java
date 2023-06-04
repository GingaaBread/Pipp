package frontend.ast.config.style;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

// todo: check number formats
@Getter
@Setter
public class Layout extends Node {
    private String width;
    private String height;
    private String margin;
    private String spacing;

    @Override
    public String toString() {
        return "\n\tLayout{" +
                "width='" + width + '\'' +
                ", height='" + height + '\'' +
                ", margin='" + margin + '\'' +
                ", spacing='" + spacing + '\'' +
                '}';
    }

    @Override
    protected void checkForWarnings() {

    }

    @Override
    protected void checkForErrors() {

    }
}
