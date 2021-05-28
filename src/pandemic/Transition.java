package pandemic;


import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static pandemic.Event.*;
import static pandemic.State.*;

/**
 * Determines how a sapiens passes from one state to
 * its next state depending on a random event happening.
 * Creates a two-key map (state, event) with values which are
 * probabilistic transition functions yielding a state.
 * The probabilities of passing from one state to the next
 * are determined by a the event.
 * This class is a non-instantiable utility class
 * with only static methods.
 * @author Sahi Gonsangbeu
 */
final class Transition {
    // set up a two-key map from a one-key map whose values are
    //  themselves one-key maps
    private static final Map<State, Map<Event, Supplier<State>>> TRANSITIONS
            = new HashMap<>();

    // initializes the map: (STATE, EVENT) -> STATE
    // this is a static block which is run on loading the class, before running the
    //  constructor to create an object
    static {
        // (SUSCEPTIBLE, CONTACT) -> SUSCEPTIBLE | INFECTED
        TRANSITIONS.put(HEALTHY, new HashMap<>());
        TRANSITIONS.get(HEALTHY)
            .put(INFECTION_RATE, () -> happens(INFECTION_RATE.probability) ? INFECTED : HEALTHY);
        // (INFECTED, TIMEOUT) -> RECOVERED | DEAD
        TRANSITIONS.put(INFECTED, new HashMap<>());
        TRANSITIONS.get(INFECTED)
            .put(MORTALITY_RATE, () -> happens(MORTALITY_RATE.probability) ? DEAD : RECOVERED);
        // (INFECTED, DURATION) -> RECOVERED | DEAD
        TRANSITIONS.put(INFECTED, new HashMap<>());
        TRANSITIONS.get(INFECTED)
                .put(INFECTION_DURATION, () -> happens(INFECTION_DURATION.probability) ? DEAD : RECOVERED);
    }

    // private constructor prevents object creation
    private Transition() {}

     /**
     * Gets a transition function value from the two-key map for given
     * state and event keys.
     * @param state Key.
     * @param event Key.
     * @return A probabilistic function (see the initialization) if
     * the keys correspond to a map entry; otherwise null.
     */
    static private Supplier get(State state, Event event) {
        Map<Event, Supplier<State>> map = TRANSITIONS.get(state);
        return map != null ? map.get(event) : null;    }

    /**
     * Determines the next state from the current state and a random event.
     * @param state The current state.
     * @param event The event which may have happened.
     * @return The next state.
     */
    static State nextState(State state, Event event) {
        Supplier<State> fn = get(state, event);
        // don't change state if there's no map entry for the keys
        return fn != null ? fn.get() : state;
    }

    /**
     * Determines whether a transition happens (or not) based on a
     * random value.
     * @param chance Probability of the transition happening.
     * @return true if the event happened, false otherwise.
     */
    private static boolean happens(double chance) {
        return Math.random() < chance;
    }

}
