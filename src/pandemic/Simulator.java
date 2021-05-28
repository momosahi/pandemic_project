package pandemic;

import java.util.*;

import static pandemic.State.*;

/**
 * A simple pandemic simulator, based on a rectangular field containing
 * susceptible, infected, recovered and dead people.
 *
 * @author Sahi Gonsangbeu
 * @author Peter Sander
 * @version 2021.04.14
 */
class Simulator {
    static final int DEFAULT_WIDTH = 100;
    static final int DEFAULT_DEPTH = 100;
    static final int ITERATIONS = 5000;
    static double INFECTED_CREATION_PROBABILITY=0.4;
    static double HEALTHY_CREATION_PROBABILITY=0.6;

    private final List<Human> humans;
    private final Field field;
    private int step;
    private final List<SimulatorView> views = new ArrayList<>();
    private int infectedCounter;

    /**
     * Construct a simulation field with default size.
     */
    Simulator(SimulatorView... views) {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH, views);
    }

    /**
     * Create a simulation field with the given size.
     *
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    Simulator(int depth, int width, SimulatorView... views) {
        if (width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        humans = new ArrayList<>();
        field = new Field(depth, width);
        this.views.addAll(Arrays.asList(views));
        reset();
    }

    boolean isViable(int step) {
        return step <= ITERATIONS && views.get(0).isViable(field) && infectedCounter!=0;
    }

    /**
     * Run the simulation from its current state for a single step. Iterate over
     * the whole field updating the state of each animal.
     *
     * @return Current step.
     */
    int simulateOneStep() {
        step++;
        infectedCounter=0;
        for (Human human : humans) {
            if (human.getStatus() != DEAD) {
                human.move();
            }
            if (human.getStatus() == INFECTED) {
                human.infectedBehaviour();
                infectedCounter += 1;
            } else {
                human.act();
            }

        }
        updateViews();
        return step;
    }

    /**
     * Reset the simulation to a starting position.
     */
    void reset() {
        step = 0;
        humans.clear();
        views.forEach(SimulatorView::reset);
        populate();
        updateViews();
    }

    /**
     * Update all existing views.
     */
    private void updateViews() {
        views.forEach(v -> v.showStatus(step, field));
    }

    /**
     * Randomly populate the field with people.
     */
    private void populate() {
        Random rand = Randomizer.getRandom();
        field.clear();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                if (rand.nextDouble() <= INFECTED_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Human infected= new Human( field, location, INFECTED);
                    humans.add(infected);
                } else if (rand.nextDouble() <= HEALTHY_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Human healthy = new Human(field, location,HEALTHY);
                    humans.add(healthy);
                }
                // else leave the field empty

            }
        }
    }
    static void setInfectedCreationProbability(double proba){
        INFECTED_CREATION_PROBABILITY = proba;
    }

    static void setHealthyCreationProbability(double proba){
        HEALTHY_CREATION_PROBABILITY = proba;
    }
}
