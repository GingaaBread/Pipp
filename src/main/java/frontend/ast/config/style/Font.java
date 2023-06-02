package frontend.ast.config.style;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Font extends Node {
    private String name;
    private String colour;
    private String size;

    @Override
    public String toString() {
        return "\nFont{" +
                "name='" + name + '\'' +
                ", colour='" + colour + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}
