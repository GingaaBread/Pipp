package frontend.ast.config;

import frontend.ast.Node;
import frontend.ast.config.style.Style;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Configuration extends Node {
    private Assessors assessors;
    private Authors authors;
    private Publication publication;
    private Style style;
    private Title title;
    private String type;

    public Configuration() {
        assessors = new Assessors();
        authors = new Authors();
        publication = new Publication();
        style = new Style();
        title = new Title();
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "assessors=" + assessors +
                ", authors=" + authors +
                ", publication=" + publication +
                ", style=" + style +
                ", title=" + title +
                ", type='" + type + '\'' +
                '}';
    }
}
