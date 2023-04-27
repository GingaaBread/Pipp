package lexical_analysis.keywords;

import lexical_analysis.DFA;

public class TableOfContents extends DFA {

    private int state;

    @Override
    public boolean transition(char character) {
        switch (state) {
            case 0:
                if (character == 't') state = 1;
                else return reject();
                return true;
            case 1:
                if (character == 'a') state = 2;
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
                if (character == 'e') state = 5;
                else return reject();
                return true;
            case 5:
                if (character == 'o') state = 6;
                else return reject();
                return true;
            case 6:
                if (character == 'f') state = 7;
                else return reject();
                return true;
            case 7:
                if (character == 'c') state = 8;
                else return reject();
                return true;
            case 8:
                if (character == 'o') state = 9;
                else return reject();
                return true;
            case 9:
                if (character == 'n') state = 10;
                else return reject();
                return true;
            case 10:
                if (character == 't') state = 11;
                else return reject();
                return true;
            case 11:
                if (character == 'e') state = 12;
                else return reject();
                return true;
            case 12:
                if (character == 'n') state = 13;
                else return reject();
                return true;
            case 13:
                if (character == 't') state = 14;
                else return reject();
                return true;
            case 14:
                if (character == 's') state = 15;
                else return reject();
                return true;
            default: return reject();
        }
    }

    private boolean reject() { state = -1; return false; }

    @Override
    public boolean inAcceptState() {
        return state == 15;
    }

}
