package pandemic;

/**
 * State-changing events.
 *
 * @author Peter Sander
 * @author Sahi Gonsangbeu
 */
enum Event {
    // different probabilities can be associated with
    // different event identifiers
    INFECTION_RATE(0.33),
    MORTALITY_RATE(0.1),
    INFECTION_DURATION(0.4);
    // yes, this should normally be private - it's package-private
    //  because I'm too lazy to write a getter and a setter  :-(
    // and I trust my group members  :-)
    double probability;
    // the constructor to initialize the event identifiers
    // note that the constructor is private by default,
    //  and can only be private
    Event(double probability) {
        this.probability = probability;
    }
}