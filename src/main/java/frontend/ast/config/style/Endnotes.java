package frontend.ast.config.style;

import error.MissingMemberException;
import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;
import processing.StructureType;

/**
 *  The endnotes node represents the endnotes style configuration
 *
 *  @since 1.0
 *  @version 1.0
 */
@Getter
@Setter
public class Endnotes extends Node {
    private String allowBeforeStructure;

    @Override
    public String toString() {
        return "\n\tEndnotes{" +
                "allowBeforeStructure='" + allowBeforeStructure + '\'' +
                '}';
    }

    @Override
    protected void checkForWarnings() { }

    @Override
    protected void checkForErrors() {
        if (allowBeforeStructure == null)
            throw new MissingMemberException("2: A structure component is missing");

        try {
            StructureType.valueOf(allowBeforeStructure.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new MissingMemberException("2: A structure component is missing");
        }
    }
}
