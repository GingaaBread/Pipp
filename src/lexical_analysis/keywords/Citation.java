package lexical_analysis.keywords;

import lexical_analysis.DFA;

public class Citation implements DFA {

    private int state;

    @Override
    public boolean transition(char character) {
        switch (state) {
            case 0:
                if (character == 'c') state = 1;
                else return reject();
                return true;
            case 1:
                if (character == 'i') state = 2;
                else return reject();
                return true;
            case 2:
                if (character == 't') state = 3;
                else return reject();
                return true;
            case 3:
                if (character == 'a') state = 4;
                else return reject();
                return true;
            case 4:
                if (character == 't') state = 5;
                else return reject();
                return true;
            case 5:
                if (character == 'i') state = 6;
                else return reject();
                return true;
            case 6:
                if (character == 'o') state = 7;
                else return reject();
                return true;
            case 7:
                if (character == 'n') state = 8;
                else return reject();
                return true;
            default: return reject();
        }
    }

    private boolean reject() { state = -1; return false; }

    @Override
    public boolean inAcceptState() {
        return state == 8;
    }

    @Override
    public void reset() {
        state = 0;
    }
}
