package lexical_analysis.keywords;

import lexical_analysis.DFA;

public class Author implements DFA {

    private int state;

    @Override
    public boolean transition(char character) {
        switch (state) {
            case 0:
                if (character == 'a') state = 1;
                else return reject();
                return true;
            case 1:
                if (character == 'u') state = 2;
                else return reject();
                return true;
            case 2:
                if (character == 't') state = 3;
                else return reject();
                return true;
            case 3:
                if (character == 'h') state = 4;
                else return reject();
                return true;
            case 4:
                if (character == 'o') state = 5;
                else return reject();
                return true;
            case 5:
                if (character == 'r') state = 6;
                else return reject();
                return true;
            default: return reject();
        }
    }

    private boolean reject() { state = -1; return false; }

    @Override
    public boolean inAcceptState() {
        return state == 6;
    }

    @Override
    public void reset() {
        state = 0;
    }
}
