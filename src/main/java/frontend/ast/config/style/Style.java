package frontend.ast.config.style;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

// todo: check base style legal, not blank
@Getter
@Setter
public class Style extends Node {
    private String baseStyle;
    private final Layout layout = new Layout();
    private final Font font = new Font();
    private final Structure structure = new Structure();
    private final Numeration numeration = new Numeration();

    @Override
    public String toString() {
        return "\n\tStyle{" +
                "baseStyle='" + baseStyle + '\'' +
                ", layout=" + layout +
                ", font=" + font +
                ", structure=" + structure +
                ", numeration=" + numeration +
                '}';
    }

    @Override
    public void checkForWarnings() {
        layout.checkForWarnings();
        font.checkForWarnings();
        structure.checkForWarnings();
        numeration.checkForWarnings();
    }

    @Override
    public void checkForErrors() {
        layout.checkForErrors();
        font.checkForErrors();
        structure.checkForErrors();
        numeration.checkForErrors();
    }
}
