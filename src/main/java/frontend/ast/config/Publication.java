package frontend.ast.config;

import error.IncorrectFormatException;
import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

/// TODO If date = null take current date. If date = None don't

/**
 *  The publication node represents the publication configuration
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
@Getter
public class Publication extends Node {

    /**
     *  The title node contains the title of the publication.
     *  For example, this could be the name of a course.
     *  The title is its own node, instead of a string, because the user can use citations within the title.
     */
    private final Title title = new Title();

    /**
     *  The date of the publication describing when the document was published.
     *  If the date is left empty (null), the date of document creation will be used automatically, unless
     *  the date is set to "None".
     */
    @Setter
    private String date;

    /**
     *  A textual representation of the publication node, which contains all formatted properties
     *
     * @return - the publication node as a string
     */
    @Override
    public String toString() {
        return "\n\tPublication{" +
                "title=" + title +
                ", date='" + date + '\'' +
                '}';
    }

    /**
     *  The publication node does not produce warnings
     */
    @Override
    protected void checkForWarnings() {
        title.checkForWarnings();
    }

    /*
    @Override
    protected void checkForErrors() {
        if (date != null && !date.equals("None")) {
                if (date.length() != 10)
                    throw new IncorrectFormatException("1: The specified date is not 'None' and does not adhere to the " +
                            "British date format: 'dd/MM/yyyy' For example, June 3, 2023, is 03/06/2023. Date: " +
                            date);

                for (int i = 0; i < 10; i++) {
                    if (i == 2 || i == 5) {
                        if (date.charAt(i) != '/') {
                            throw new IncorrectFormatException("1: The specified date is not 'None' and does not" +
                                    " adhere to the British date format: 'dd/MM/yyyy'" +
                                    " For example, June 3, 2023, is 03/06/2023. Date: " + date);
                        }
                    } else if (!Character.isDigit(date.charAt(i))) {
                        throw new IncorrectFormatException("1: The specified date is not 'None' and does" +
                                " not adhere to the British date format: 'dd/MM/yyyy'" +
                                " For example, June 3, 2023, is 03/06/2023. Date: " + date);
                    }
                }
        }

        title.checkForErrors();
    }*/
}
