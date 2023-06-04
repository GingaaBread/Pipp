package frontend.ast.config;

import error.MissingConfigurationException;
import frontend.ast.Node;
import frontend.ast.config.style.Style;
import lombok.Getter;
import lombok.Setter;

/**
 *  The configuration node groups together all configurations
 *
 *  @since 1.0
 *  @version 1.0
 */
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

    @Override
    public void checkForWarnings() {
        assessors.checkForWarnings();
        authors.checkForWarnings();
        publication.checkForWarnings();
        style.checkForWarnings();
        title.checkForWarnings();
    }

    @Override
    public void checkForErrors() {
        if (type != null && type.isBlank())
            throw new MissingConfigurationException("1: A text component cannot be blank");

        assessors.checkForErrors();
        authors.checkForErrors();
        publication.checkForErrors();
        style.checkForErrors();
        title.checkForErrors();
    }
}
