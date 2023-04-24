package lexical_analysis.keywords;

import lexical_analysis.DFA;

public class Date implements DFA {

    private int state;

    @Override
    public boolean transition(char character) {
        switch (state) {
            case 0:
                if (character == 'd') state = 1;
                else return reject();
                return true;
            case 1:
                if (character == 'a') state = 2;
                else return reject();
                return true;
            case 2:
                if (character == 't') state = 3;
                else return reject();
                return true;
            case 3:
                if (character == 'e') state = 4;
                else return reject();
                return true;
            default: return reject();
        }
    }

    private boolean reject() { state = -1; return false; }

    @Override
    public boolean inAcceptState() {
        return state == 4;
    }

    @Override
    public void reset() {
        state = 0;
    }
}