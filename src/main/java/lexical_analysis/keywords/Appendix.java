package lexical_analysis.keywords;

import lexical_analysis.DFA;

public class Appendix implements DFA {

    private int state;

    @Override
    public boolean transition(char character) {
        switch (state) {
            case 0:
                if (character == 'a') state = 1;
                else return reject();
                return true;
            case 1:
                if (character == 'p') state = 2;
                else return reject();
                return true;
            case 2:
                if (character == 'p') state = 3;
                else return reject();
                return true;
            case 3:
                if (character == 'e') state = 4;
                else return reject();
                return true;
            case 4:
                if (character == 'n') state = 5;
                else return reject();
                return true;
            case 5:
                if (character == 'd') state = 6;
                else return reject();
                return true;
            case 6:
                if (character == 'i') state = 7;
                else return reject();
                return true;
            case 7:
                if (character == 'x') state = 8;
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
