package lexical_analysis.keywords;

import lexical_analysis.DFA;

public class Bibliography implements DFA {

    private int state;

    @Override
    public boolean transition(char character) {
        switch (state) {
            case 0:
                if (character == 'b') state = 1;
                else return reject();
                return true;
            case 1:
                if (character == 'i') state = 2;
                else return reject();
                return true;
            case 2:
                if (character == 'b') state = 3;
                else return reject();
                return true;
            case 3:
                if (character == 'l') state = 4;
                else return reject();
                return true;
            case 4:
                if (character == 'i') state = 5;
                else return reject();
                return true;
            case 5:
                if (character == 'o') state = 6;
                else return reject();
                return true;
            case 6:
                if (character == 'g') state = 7;
                else return reject();
                return true;
            case 7:
                if (character == 'r') state = 8;
                else return reject();
                return true;
            case 8:
                if (character == 'a') state = 9;
                else return reject();
                return true;
            case 9:
                if (character == 'p') state = 10;
                else return reject();
                return true;
            case 10:
                if (character == 'h') state = 11;
                else return reject();
                return true;
            case 11:
                if (character == 'y') state = 12;
                else return reject();
                return true;
            default: return reject();
        }
    }

    private boolean reject() { state = -1; return false; }

    @Override
    public boolean inAcceptState() {
        return state == 12;
    }

    @Override
    public void reset() {
        state = 0;
    }
}
