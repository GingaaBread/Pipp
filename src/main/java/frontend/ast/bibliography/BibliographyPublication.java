package frontend.ast.bibliography;

import error.MissingMemberException;
import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BibliographyPublication extends Node {

    private String name;

    private String year;

    @Override
    public void checkForWarnings() {
        if (name != null && name.isBlank()) throw new MissingMemberException(MissingMemberException.ERR_MSG_1);
        if (year != null && year.isBlank()) throw new MissingMemberException(MissingMemberException.ERR_MSG_1);
    }
}
