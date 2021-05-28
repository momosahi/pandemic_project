package pandemic;

import pandemic.Controls;
import pandemic.Simulator;
import pandemic.SimulatorView;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Animation timer for pandemic simulation.
 * GUI via JavaFX.
 * Simulator actually manages the simulation.
 *
 * @author Peter Sander
 */
public class Animator extends Application {
    private Simulator simulator;
    private Controls controls;
    private final List<SimulatorView> views = new ArrayList<>();
    private AnimationTimer timer;
    private long zzz;
    private pandemic.Field field;
    private int step;

    /**
     * 1. Creates an Animator instance.
     * 2. Calls its init method.
     * 3. Calls its start method in the FX application thread.
     *
     * @param args Complete class names of simulation views to instantiate.
     */
    public static void main(String... args) {
        Application.launch(args);
    }

    /**
     * Instantiates simulator view objects from simulator view class names.
     * Bit of a hack to get around the checked exceptions.
     */
    @Override
    public void init() {
        controls = new Controls();
        views.add(controls);
        getParameters().getUnnamed().forEach(name
                -> {
                try {
                    views.add((SimulatorView) Class.forName(name).newInstance());
                } catch (Exception e) {
                    System.out.println("WHOOPSIhE! Careful with that cast, Eugene");
                }
            }
        );
    }

    /**
     * Sets up and runs animation timer.
     * Calls one simulation step at each time event.
     */
    @Override
    public void start(Stage primaryStage) {
        views.forEach(v -> v.start());
        simulator = new Simulator(views.toArray(new SimulatorView[0]));
        if (timer == null) {
            timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    // trying to slow down the display
                    try {
                        zzz = controls.getSpeed();
                        Thread.sleep(controls.getSpeed());
                    } catch (InterruptedException e) {
                    }
                    step = simulator.simulateOneStep();
                    if (!simulator.isViable(step)) {
                        stop();
                        System.out.println("Animation stopped");
                    }
                }
            };
            controls.setTimer(timer);
        }
        timer.start();
    }
}
