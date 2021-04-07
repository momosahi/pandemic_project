package foxesandrabbits.graph.fx;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * The GraphView provides a view of two populations of actors in the field as a
 * line graph over time. In its current version, it can only plot exactly two
 * different classes of animals. If further animals are introduced, they will
 * not currently be displayed.
 *
 * @author Michael Kölling and David J. Barnes
 * @author Peter Sander
 * @version 2017.03.29
 */
class GraphView implements SimulatorView {
    private static final int DELAY_BETWEEN_ITERATIONS = 100;  // msecs
    private static final int LABEL_HEIGHT = 50;
    private static final Color LIGHT_GRAY = new Color(0, 0, 0, 1.0);
    private static GraphView instance;
    // private static GraphPanel graph;
    private static Label stepLabel;
    private static Label countLabel;
    private final String STEP_PREFIX = "Step: ";
    // A map for storing colors for participants in the simulation
    private final Map<Class<? extends Animal>, Color> colors
        = new HashMap<>() {{
        put(Rabbit.class, Color.ORANGE);
        put(Fox.class, Color.BLUE);
    }};
    // The classes being tracked by this view
    private Set<Class<? extends Animal>> classes;
    private LineChart.Series<Number, Number> rabbitSeries;
    private LineChart.Series<Number, Number> foxSeries;
    // A statistics object computing and storing simulation information
    private final FieldStats stats;
    private int width;
    private int height;
    private BorderPane root;
    private final int step = 0;

    GraphView() {
        stats = new FieldStats();
        classes = new HashSet<>();
        instance = this;
    }

    /**
     * @return reference to the current object.
     */
    static GraphView getInstance() {
        return instance;
    }

    /**
     * FX application method to run the GUI.
     */
    @Override
    public void start() {
        Stage stage = new Stage();
        stage.setTitle("Foxes vs. Rabbits");
        root = new BorderPane();

        stepLabel = new Label(STEP_PREFIX);
        stepLabel.setAlignment(Pos.CENTER);
        stepLabel.setMinHeight(LABEL_HEIGHT);
        root.setTop(stepLabel);
        BorderPane.setAlignment(root.getTop(), Pos.CENTER);

        // root.setCenter(graph);
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    private Parent createContent() {
        final NumberAxis xAxis = new NumberAxis();
        xAxis.setForceZeroInRange(false);
        // final NumberAxis yAxis = new NumberAxis(-100, 100, 10);
        final NumberAxis yAxis = new NumberAxis(0, 5000, 100);
        final LineChart<Number, Number> sc = new LineChart<>(xAxis, yAxis);
        // setup chart
        sc.setTitle("Foxes vs Rabbits");
        xAxis.setLabel("X Axis");
        sc.setCreateSymbols(false);
        xAxis.setAnimated(false);
        yAxis.setLabel("Y Axis");
        yAxis.setAutoRanging(false);
        // add starting data
        rabbitSeries = new LineChart.Series<>();
        rabbitSeries.setName("Rabbits");
        // rabbitSeries.getData().add(new LineChart.Data<Number, Number>(5d, 5d));
        foxSeries = new LineChart.Series<>();
        foxSeries.setName("Foxes");
        // foxSeries.getData().add(new LineChart.Data<Number, Number>(5d, 100d));
        sc.getData().add(rabbitSeries);
        sc.getData().add(foxSeries);
        return sc;
    }

    void stop() {
        System.out.println("Well, that was fun.");
    }

    /**
     * Define a color to be used for a given class of animal.
     *
     * @param animalClass The animal's Class object.
     * @param color       The color to be used for the given class.
     */
    @Override
    public void setColor(Class<? extends Animal> animalClass, Color color) {
        colors.put(animalClass, color);
        classes = colors.keySet();
    }

    /**
     * Show the current status of the field. The status is shown by displaying a
     * line graph for two classes in the field. This view currently does not
     * work for more (or fewer) than exactly two classes. If the field contains
     * more than two different types of animal, only two of the classes will be
     * plotted.
     *
     * @param step  Which iteration step it is.
     * @param field The field whose status is to be displayed.
     */
    @Override
    public void showStatus(int step, Field field) {
        update(step, field, stats);
    }

    /**
     * Determine whether the simulation should continue to run.
     *
     * @return true If there is more than one species alive.
     */
    @Override
    public boolean isViable(Field field) {
        return stats.isViable(field);
    }

    /**
     * Prepare for a new run.
     */
    public void reset() {
        // newRun();
    }

    /**
     * Dispay a new point of data.
     */
    void update(int step, Field field, FieldStats stats) {
        stats.reset();
        int rabbitCount = stats.getPopulationCount(field,
            Rabbit.class);
        rabbitSeries.getData()
            .add(new XYChart.Data<>(step, rabbitCount));
        int foxCount = stats.getPopulationCount(field,
            Fox.class);
        foxSeries.getData()
            .add(new XYChart.Data<>(step, foxCount));

        if (rabbitSeries.getData().size() > 54 && foxSeries.getData().size() > 54) {
            rabbitSeries.getData().remove(0);
            foxSeries.getData().remove(0);
        }
    }
}
