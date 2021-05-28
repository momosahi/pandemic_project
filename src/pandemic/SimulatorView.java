package pandemic;

import javafx.scene.paint.Color;

/**
 * A graphical view of the simulation grid. This interface defines all possible
 * different views.
 *
 * @author Michael Kölling and David J. Barnes
 * @author Peter Sander
 * @author Sahi Gonsangbeu
 * @version 2021.04.22
 */
interface SimulatorView {
    /**
     * Define a color to be used for a given class of people.
     *
     * @param peopleState The people's state.
     * @param color       The color to be used for the given class.
     */
    void setColor(State peopleState, Color color);

    /**
     * Determine whether the simulation should continue to run.
     *
     * @return true If there is more than one species alive.
     */
    boolean isViable(Field field);

    /**
     * Show the current status of the field.
     *
     * @param step  Which iteration step it is.
     * @param field The field whose status is to be displayed.
     */
    void showStatus(int step, Field field);

    /**
     * Prepare for a new run.
     */
    void reset();

    /**
     * Sets up GUI.
     */
    void start();
}