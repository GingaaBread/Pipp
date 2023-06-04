package warning;

import lombok.Getter;

import java.util.ArrayDeque;
import java.util.Queue;

public class WarningQueue {

    @Getter
    private static WarningQueue instance;

    private final Queue<Warning> warnings;

    public WarningQueue() {
        if (instance == null) instance = this;
        else throw new IllegalStateException("There is already a WarningQueue instance");

        warnings = new ArrayDeque<>();
    }

    public void enqueue(Warning warning) {
        warnings.add(warning);
    }

    public void printAll() {
        warnings.forEach(System.out::println);
    }
}
