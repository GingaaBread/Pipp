package frontend.ast.config;

import frontend.ast.Node;
import frontend.ast.config.style.Citation;
import lombok.Getter;

@Getter
public class CitedText extends Node {
    private Citation citation;
    private String text;

    public CitedText(Citation citation) {
        this.citation = citation;
    }

    public CitedText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "\nCitedText{" +
                "citation=" + citation +
                ", text='" + text + '\'' +
                '}';
    }
}
