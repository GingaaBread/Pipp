package lexical_analysis;

/**
 *  Represents an interface for Deterministic Finite Automatas (DFAs) that, reading an input character,
 *  transition from one state to another, determine if it is in an accepting state, and reset its state to the
 *  starting state q0
 */
public interface DFA {

    /**
     * Reading the new character, tries to transition to the next state.
     * If it exists, it will be in the new state and returns true.
     * If it doesn't, returns false.
     * @param character - the newly read character
     * @return - true if a transition exists and has been done, false if no transition using the character exists
     */
    boolean transition(char character);

    /**
     * Determines if the current state is an accepting state
     * @return - true if in an accepting state, false if in an rejecting state
     */
    boolean inAcceptState();

    /**
     *  Resets the state to the starting state q0
     */
    void reset();
}
