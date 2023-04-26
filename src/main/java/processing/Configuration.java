package processing;

import lombok.Data;

/**
 *  Specifies the configuration marked by the "config" instruction in the document.
 *
 *  @since 1.0
 *  @version 1.0
 */
@Data
public class Configuration {

    /**
     *  Determines the authors of the document.
     *  Allowed arguments:
     *  - (listed) of [args]: A list of authors
     * <p>
     *  Allowed arguments of authors:
     *  - (requires "lastname"; not allowed if "name" is used) firstname: The first name of the author
     *  - (requires "firstname"; not allowed if "name" is used) lastname: The last name of the author
     *  - (not allowed if either "firstname" or "lastname" is used) name:
     *      The first and last name of the author combined in one string
     *  - id: Identifies the author
     *  @see Author
     */
    private Author[] authors;

    /**
     *  Determines the assessors of the document.
     *  Allowed arguments:
     *  - (listed) of [args]: A list of assessors
     * <p>
     *  Allowed arguments of assessors:
     *  - (requires "lastname"; not allowed if "name" is used) firstname: The first name of the assessor
     *  - (requires "firstname"; not allowed if "name" is used) lastname: The last name of the assessor
     *  - (not allowed if either "firstname" or "lastname" is used) name:
     *      The first and last name of the assessor combined in one string
     *  - role: Specifies the role of the assessor
     */
    private Assessor[] assessors;


    /**
     *  Determines details about the publication of the document.
     *  Allowed arguments:
     *  - (listed) title [args]: The document title. The list includes strings and "citation" blocks.
     *  - date: The publication date of the document
     */
    private Publication publication;

    /**
     *  Defines the used version of the document declared by the user
     *  No allowed arguments
     */
    private String version;

}
