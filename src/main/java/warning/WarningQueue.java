package warning;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 *  This class is used to collect all warnings that occur during compilation and print them afterwards.
 *  To enqueue a warning, the static enqueue method is used.
 *  To print all warnings, the static printAll method is used.
 * @author Gino Glink
 * @version 1.0
 * @since 1.0
 */
public class WarningQueue {

    /**
     *  Collects all warnings in order of occurrence
     */
    private static final Queue<Warning> warnings = new ArrayDeque<>();

    /**
     *  Adds a warning to the back of the queue
     * @param warning the warning object that should be enqueued
     */
    public static void enqueue(Warning warning) {
        warnings.add(warning);
    }

    /**
     *  Prints the toString method of all warnings in the queue.
     *  Used at the end of compilation to render all warnings.
     */
    public static void printAll() {
        warnings.forEach(System.out::println);
    }

}
