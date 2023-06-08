package frontend.ast.config;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

/**
 *  The assessor node contains properties for setting the name and role of the assessor
 *  It should always only use either name or first + lastname, but never both
 *  Role should never be supplied without having set up the name properly
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
@Getter
@Setter
public class Assessor extends Node {

    /**
     *  The non-blank name of the assessor provided by the user.
     *  If the name is used, the first and last name configurations should not be used as the
     *  name is used to create these automatically.
     */
    private String name;

    /**
     *  The non-blank first name of the assessor provided by the user.
     *  If the first name is used, the last name must also be used, and the name may not be used.
     */
    private String firstname;

    /**
     *  The non-blank last name of the assessor provided by the user.
     *  If the last name is used, the first name must also be used, and the name may not be used.
     */
    private String lastname;

    /**
     *  The non-blank role of the assessor provided by the user.
     *  The role could be "Instructor", "Professor", "Examiner", etc.
     */
    private String role;

    /**
     *  A textual representation of the Assessor node, which contains the properties of the node
     *  separated in a visual manner
     *
     * @return - the properties of the Assessor node as a String
     */
    @Override
    public String toString() {
        return "\n\tAssessor{" +
                "name='" + name + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    /**
     *  The Assessor node does not produce warnings
     */
    @Override
    protected void checkForWarnings() { }

}
