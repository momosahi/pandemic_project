package foxesandrabbits.graph.fx;

import java.util.*;

/**
 * A simple predator-prey simulator, based on a rectangular field containing
 * rabbits and foxes.
 *
 * @author David J. Barnes and Michael Kölling
 * @author Peter Sander
 * @version 2017.03.24
 */
class Simulator {
    static final int DEFAULT_WIDTH = 120;
    static final int DEFAULT_DEPTH = 80;
    static final int ITERATIONS = 5000;
    private static final double FOX_CREATION_PROBABILITY = 0.02;
    private static final double RABBIT_CREATION_PROBABILITY = 0.08;

    private final List<Animal> animals;
    private final Field field;
    private int step;
    private final List<SimulatorView> views = new ArrayList<>();

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
        animals = new ArrayList<>();
        field = new Field(depth, width);
        Arrays.asList(views).forEach(v -> this.views.add(v));
        reset();
    }

    boolean isViable(int step) {
        return step <= ITERATIONS && views.get(0).isViable(field);
    }

    /**
     * Run the simulation from its current state for a single step. Iterate over
     * the whole field updating the state of each animal.
     *
     * @return Current step.
     */
    int simulateOneStep() {
        step++;
        List<Animal> newAnimals = new ArrayList<>();
        for (Iterator<Animal> it = animals.iterator(); it.hasNext(); ) {
            Animal animal = it.next();
            animal.act(newAnimals);
            if (!animal.isAlive()) {
                it.remove();
            }
        }
        animals.addAll(newAnimals);
        updateViews();
        return step;
    }

    /**
     * Reset the simulation to a starting position.
     */
    void reset() {
        step = 0;
        animals.clear();
        views.forEach(v -> v.reset());
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
     * Randomly populate the field with foxes and rabbits.
     */
    private void populate() {
        Random rand = Randomizer.getRandom();
        field.clear();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                if (rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Fox fox = new Fox(true, field, location);
                    animals.add(fox);
                } else if (rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Rabbit rabbit = new Rabbit(true, field, location);
                    animals.add(rabbit);
                }
                // else leave the location empty.
            }
        }
    }
}
