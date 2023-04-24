package processing.work;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *  Represents a role of a person having worked on a work other than the author
 *
 *  @see Work
 *  @see Author
 *
 *  @since 1.0
 *  @version 1.0
 */
@Data
@AllArgsConstructor
public class Role {

    /**
     *  The name of the person having this role
     */
    private String name;

    /**
     *  The name of the role, for example "Editor" or "Programmer"
     */
    private String roleName;

    /**
     *  The description of the role used for cases where the role name is unknown and cannot be automatically generated
     */
    private String roleDescription;

    /**
     *  Creates a new role excluding the role description.
     *  This is used for common roles like editors or producers, which will have automatically generated role
     *  descriptions
     * @param name - the name of the person having this role
     * @param roleName - The name of the role
     */
    public Role(String name, String roleName) {
        this.name = name;
        this.roleName = roleName;
    }

}
