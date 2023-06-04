package frontend.ast.config;

import error.IllegalConfigurationException;
import error.MissingConfigurationException;
import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

/**
 *  The author node represents an author of the document
 *
 *  @since 1.0
 *  @version 1.0
 */
@Getter
@Setter
public class Author extends Node {
    private String name;
    private String firstname;
    private String lastname;
    private String id;

    @Override
    public String toString() {
        return "\n\tAuthor{" +
                "name='" + name + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    @Override
    protected void checkForWarnings() {

    }

    @Override
    protected void checkForErrors() {
        if (name == null && firstname == null && lastname == null)
            throw new MissingConfigurationException("6: An author requires a name configuration, but neither " +
                    "name, firstname nor lastname has been configured.");
        else if (name != null && (firstname != null || lastname != null))
            throw new IllegalConfigurationException("7: An author can only be given a name configuration " +
                    "OR a firstname and lastname configuration.");
        else if (name == null && firstname != null && lastname == null)
            throw new IllegalConfigurationException("8: An author cannot only have a firstname configuration. " +
                    "Either also provide a lastname configuration or only use the name configuration.");
    }
}
