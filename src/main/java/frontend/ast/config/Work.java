package frontend.ast.config;

import frontend.ast.Node;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Work extends Node {

    private String emphasisedWork;

    @Override
    protected void checkForWarnings() {

    }
}
