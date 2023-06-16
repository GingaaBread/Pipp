package frontend.ast.config;

import frontend.ast.Node;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Emphasis extends Node {

    private String emphasisedText;

    @Override
    protected void checkForWarnings() {

    }
}
