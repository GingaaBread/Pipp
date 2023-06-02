package frontend.ast.config;

import frontend.ast.Node;

import java.util.ArrayList;

public class Title extends Node {
    private final ArrayList<CitedText> texts = new ArrayList<>();

    public void add(CitedText text) {
        texts.add(text);
    }

    @Override
    public String toString() {
        return "\nTitle{" +
                "texts=" + texts +
                '}';
    }
}
