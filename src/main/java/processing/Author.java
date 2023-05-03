package processing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import processing.work.Work;

/**
 *  Represents the author of a work represented by a first and last name.
 *
 *  @see Work
 *  @version 1.0
 *  @since 1.0
 */
@Data
@AllArgsConstructor
@Builder
public class Author
{
    /**
     *  The first name of the author. If the author has multiple first names, they can all be put in the variable.
     */
    private String firstName;

    /**
     *  The last name of the author. If the author has multiple last names, they can all be put in the variable.
     */
    private String lastName;

}
