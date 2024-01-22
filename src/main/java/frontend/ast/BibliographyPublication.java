package frontend.ast;

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
    protected void checkForWarnings() {

    }
}
