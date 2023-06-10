package frontend.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import processing.StructureType;

/**
 *  The no argument structure node contains a structure, which receives no arguments.
 *  This is because the structure is used to build content based on the user's configurations.
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
@Setter
@Getter
@AllArgsConstructor
public class NoArgumentStructure extends Node {

    /**
     *  The type of the structure.
     *  For example, "bibliography" is a structure with no arguments.
     */
    private StructureType type;

    /**
     *  A textual representation of the no argument structure node, which contains all formatted properties
     *
     * @return - the properties of the no argument structure node as a string
     */
    @Override
    public String toString() {
        return "NoArgumentStructure{" +
                "type=" + type +
                '}';
    }

    /**
     *  The No argument structure node does not produce errors
     */
    @Override
    protected void checkForWarnings() { }
}
