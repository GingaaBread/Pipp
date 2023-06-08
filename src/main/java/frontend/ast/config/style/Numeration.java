package frontend.ast.config.style;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
// todo: check numeration type exists, position exists, margin is legal, skipped pages are legal
public class Numeration extends Node {
    @Setter
    private String numerationType;
    @Setter
    private String position;
    @Setter
    private String margin;

    private List<String> skippedPages = new LinkedList<>();

    @Override
    public String toString() {
        return "\n\tNumeration{" +
                "numerationType='" + numerationType + '\'' +
                ", position='" + position + '\'' +
                ", margin='" + margin + '\'' +
                ", skippedPages='" + skippedPages + '\'' +
                '}';
    }

    public void addSkippedPage(String skippedPage) {
        skippedPages.add(skippedPage);
    }

    @Override
    protected void checkForWarnings() {

    }

    @Override
    protected void checkForErrors() {

    }
}
