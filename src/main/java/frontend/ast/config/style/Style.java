package frontend.ast.config.style;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

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
        return "\nStyle{" +
                "baseStyle='" + baseStyle + '\'' +
                ", layout=" + layout +
                ", font=" + font +
                ", structure=" + structure +
                ", numeration=" + numeration +
                '}';
    }
}
