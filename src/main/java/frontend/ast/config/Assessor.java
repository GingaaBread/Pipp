package frontend.ast.config;

import error.IllegalConfigurationException;
import error.MissingConfigurationException;
import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

/**
 *  The assessor node contains properties for setting the name and role of the assessor
 *  It should always only use either name or first + lastname, but never both
 *  Role should never be supplied without having set up the name properly
 *
 *  @since 1.0
 *  @version 1.0
 */
@Getter
@Setter
public class Assessor extends Node {
    private String name;
    private String firstname;
    private String lastname;
    private String role;

    @Override
    public String toString() {
        return "\n\tAssessor{" +
                "name='" + name + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    @Override
    protected void checkForWarnings() { }

    @Override
    protected void checkForErrors() {
        if (name == null && firstname == null && lastname == null)
            throw new MissingConfigurationException("1: An assessor requires a name configuration, but neither " +
                    "name, firstname nor lastname has been configured.");

        if (name != null && (firstname != null || lastname != null))
            throw new IllegalConfigurationException("2: An assessor can only be given a name configuration " +
                    "OR a firstname and lastname configuration.");

        if (name == null && firstname != null && lastname == null)
            throw new IllegalConfigurationException("3: An assessor cannot only have a firstname configuration. " +
                    "Either also provide a lastname configuration or only use the name configuration.");
    }
}
