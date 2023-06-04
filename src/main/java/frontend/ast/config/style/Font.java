package frontend.ast.config.style;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

// todo: Check font colour format, size likeliness, size format, blankness
@Getter
@Setter
public class Font extends Node {
    private String name;
    private String colour;
    private String size;

    @Override
    public String toString() {
        return "\n\tFont{" +
                "name='" + name + '\'' +
                ", colour='" + colour + '\'' +
                ", size='" + size + '\'' +
                '}';
    }

    @Override
    protected void checkForWarnings() {

    }

    @Override
    protected void checkForErrors() {

    }
}
