package frontend.ast;

import frontend.ast.config.Authors;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class BibliographySource extends Node {

    private final Authors authors = new Authors();
    private final BibliographyPublication publication = new BibliographyPublication();
    @Setter
    private String id;
    @Setter
    private String type;
    @Setter
    private String title;

    @Override
    public void checkForWarnings() {
        authors.checkForWarnings();
        publication.checkForWarnings();
    }

}
