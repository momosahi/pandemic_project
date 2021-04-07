package foxesandrabbits.graph.fx;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The PhaseView provides a view of two populations of actors in the field as a
 * line graph over time. In its current version, it can only plot exactly two
 * different classes of animals. If further animals are introduced, they will
 * not currently be displayed.
 *
 * @author Michael Kölling and David J. Barnes
 * @author Peter Sander
 * @version 2017.03.29
 */
class PhaseView implements SimulatorView {
    private static final int MAX_FOXES = 1000;
    private static final int MAX_RABBITS = 6000;
    private static final int MAX_X = 500;
    private static final int MAX_Y = 500;
    private static final double SCALE_X = ((double) MAX_X) / MAX_RABBITS;
    private static final double SCALE_Y = ((double) MAX_Y) / MAX_FOXES;
    private static final int DELAY_BETWEEN_ITERATIONS = 100;  // msecs
    private static final int LABEL_HEIGHT = 50;
    private static final Color LIGHT_GRAY = new Color(0, 0, 0, 1.0);
    private static PhaseView instance;
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
    private LineChart.Series<Number, Number> rabbitsVsFoxesSeries;
    // A statistics object computing and storing simulation information
    private final FieldStats stats;
    private int width;
    private int height;
    private BorderPane root;
    private final int step = 0;
    private Path path;
    private double currentX;
    private double currentY;

    PhaseView() {
        stats = new FieldStats();
        classes = new HashSet<>();
        instance = this;
        // currentX = stats.getPopulationCount(field, Rabbit.class);
        // currentY = stats.getPopulationCount(field, Fox.class);
    }

    /**
     * @return reference to the current object.
     */
    static PhaseView getInstance() {
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

    // private Parent createContent() {
    //     final NumberAxis xAxis = new NumberAxis();
    //     xAxis.setForceZeroInRange(false);
    //     // final NumberAxis yAxis = new NumberAxis(-100, 100, 10);
    //     final NumberAxis yAxis = new NumberAxis(0, 5000, 100);
    //     final LineChart<Number, Number> sc = new LineChart<>(xAxis, yAxis);
    //     // setup chart
    //     sc.setTitle("Phase view: Foxes vs Rabbits");
    //     xAxis.setLabel("Rabbits");
    //     sc.setCreateSymbols(false);
    //     xAxis.setAnimated(false);
    //     yAxis.setLabel("Foxes");
    //     yAxis.setAutoRanging(false);
    //     // add starting data
    //     rabbitsVsFoxesSeries = new LineChart.Series<>();
    //     rabbitsVsFoxesSeries.setName("Rabbits vs Foxes");
    //     sc.getData().add(rabbitsVsFoxesSeries);
    //     return sc;
    // }


    Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(MAX_X, MAX_Y);
        root.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        root.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        path = new Path();
        root.getChildren().add(path);
        return root;
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
    public void showStatus(int step, Field field) {
        update(step, field, stats);
    }

    /**
     * Determine whether the simulation should continue to run.
     *
     * @return true If there is more than one species alive.
     */
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
        int rabbitCount = stats.getPopulationCount(field, Rabbit.class);
        int foxCount = stats.getPopulationCount(field, Fox.class);

        // Path path = new Path();
        // System.out.println("line: (" + currentX + ", " + currentY
        //         + ") -> (" + rabbitCount * SCALE_X + ", "
        //         + foxCount * SCALE_Y + ")");
        path.getElements().addAll(
            new MoveTo(currentX, currentY),
            new LineTo(rabbitCount * SCALE_X, foxCount * SCALE_Y));
        path.setFill(null);
        path.setStroke(Color.RED);
        path.setStrokeWidth(2);
        // root.getChildren().add(path);
        currentX = rabbitCount * SCALE_X;
        currentY = foxCount * SCALE_Y;
    }
}
