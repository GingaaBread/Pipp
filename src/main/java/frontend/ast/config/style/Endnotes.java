package frontend.ast.config.style;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Endnotes extends Node {
    private String allowBeforeStructure;

    @Override
    public String toString() {
        return "\nEndnotes{" +
                "allowBeforeStructure='" + allowBeforeStructure + '\'' +
                '}';
    }
}
