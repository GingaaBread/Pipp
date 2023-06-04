package frontend.ast.config;

import error.IncorrectFormatException;
import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

/// TODO If date = null take current date. If date = None don't

/**
 *  The publication node represents the publication configuration
 *
 *  @since 1.0
 *  @version 1.0
 */
@Getter
public class Publication extends Node {
    private final Title title = new Title();

    @Setter
    private String date;

    @Override
    public String toString() {
        return "\n\tPublication{" +
                "title=" + title +
                ", date='" + date + '\'' +
                '}';
    }

    @Override
    protected void checkForWarnings() {
        title.checkForWarnings();
    }

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
    }
}
