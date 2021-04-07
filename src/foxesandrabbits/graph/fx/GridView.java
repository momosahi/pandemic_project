package foxesandrabbits.graph.fx;

import javafx.geometry.Dimension2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * A graphical view of the simulation grid. The view displays a colored
 * rectangle for each location representing its contents. Colors for each type
 * of species can be defined using the setColor method.
 *
 * @author Michael Kölling and David J. Barnes
 * @author Peter Sander
 * @version 2017.03.24
 */
class GridView implements SimulatorView {
    private static final int DELAY_BETWEEN_ITERATIONS = 100;  // msecs
    private static final int LABEL_HEIGHT = 50;
    // Colors used for empty locations.
    private static final Color EMPTY_COLOR = Color.WHITE;
    // Color used for objects that have no defined color.
    private static final Color UNKNOWN_COLOR = Color.GRAY;
    private static GridView instance;
    private final String STEP_PREFIX = "Step: ";
    private final String POPULATION_PREFIX = "Population: ";
    // A map for storing colors for participants in the simulation
    private final Map<Class<? extends Animal>, Color> colors
        = new HashMap<>() {{
        put(Rabbit.class, Color.ORANGE);
        put(Fox.class, Color.BLUE);
    }};
    private Label stepLabel;
    private Label populationLbl;
    private final FieldView fieldView;
    // A statistics object computing and storing simulation information
    private final FieldStats stats;
    private final int width;
    private final int height;
    private BorderPane root;
    private final int step = 0;

    GridView() {
        this(Simulator.DEFAULT_DEPTH, Simulator.DEFAULT_WIDTH);
    }

    /**
     * Create a view of the given width and height.
     *
     * @param height The simulation's height.
     * @param width  The simulation's width.
     */
    GridView(int height, int width) {
        this.width = width;
        this.height = height;
        stats = new FieldStats();
        fieldView = new FieldView(height, width);
        instance = this;
    }

    /**
     * @return reference to the current object.
     */
    static GridView getInstance() {
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

        populationLbl = new Label(POPULATION_PREFIX);
        populationLbl.setAlignment(Pos.CENTER);
        populationLbl.setMinHeight(LABEL_HEIGHT);
        root.setBottom(populationLbl);
        BorderPane.setAlignment(root.getBottom(), Pos.CENTER);

        root.setCenter(fieldView);
        stage.setScene(new Scene(root,
            width * FieldView.GRID_VIEW_SCALING_FACTOR,
            height * FieldView.GRID_VIEW_SCALING_FACTOR + 2 * LABEL_HEIGHT));
        stage.show();
    }

    void stop() {
        System.out.println("Well, that was fun.");
    }

    /**
     * Define a color to be used for a given class of animal.
     *
     * @param animalClass The animal's Class<Animal> object.
     * @param color       The color to be used for the given class.
     */
    @Override
    public void setColor(Class<? extends Animal> animalClass, Color color) {
        colors.put(animalClass, color);
    }

    /**
     * @return The color to be used for a given class of animal.
     */
    private Color getColor(Class<?> animalClass) {
        Color col = colors.get(animalClass);
        return col == null ? UNKNOWN_COLOR : col;
    }

    /**
     * Show the current status of the field.
     * Incidentally draws protagonists in place.
     *
     * @param step  Which iteration step it is.
     * @param field The field whose status is to be displayed.
     */
    @Override
    public void showStatus(int step, Field field) {
        stepLabel.setText(STEP_PREFIX + step);
        stats.reset();
        fieldView.preparePaint();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Animal animal = field.getAnimalAt(row, col);
                if (animal != null) {
                    stats.incrementCount(animal.getClass());
                    fieldView.drawMark(col,
                        row, getColor(animal.getClass()));
                } else {
                    fieldView.drawMark(col, row, EMPTY_COLOR);
                }
            }
        }
        stats.countFinished();
        populationLbl.setText(stats.getPopulationDetails(field));
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
    @Override
    public void reset() {
        // always a pleasure
    }


    /**
     * Provide a graphical view of a rectangular field. This is a nested class
     * (a class defined inside a class) which defines a custom component for the
     * user interface. This component displays the field. This is rather
     * advanced GUI stuff - you can ignore this for your project if you like.
     */
    private class FieldView extends Canvas {
        private static final int GRID_VIEW_SCALING_FACTOR = 6;

        private final int gridWidth;
        private final int gridHeight;
        private int xScale, yScale;
        private final Dimension2D size;
        private final GraphicsContext g;

        /**
         * Create a new FieldView component.
         */
        FieldView(int height, int width) {
            super(width * GRID_VIEW_SCALING_FACTOR,
                height * GRID_VIEW_SCALING_FACTOR);
            gridHeight = height;
            gridWidth = width;
            size = new Dimension2D(width * GRID_VIEW_SCALING_FACTOR,
                height * GRID_VIEW_SCALING_FACTOR);
            g = getGraphicsContext2D();
        }

        /**
         * Prepare for a new round of painting. Since the component may be
         * resized, compute the scaling factor again.
         */
        void preparePaint() {
            g.clearRect(0, 0, size.getWidth(), size.getHeight());
            xScale = (int) size.getWidth() / gridWidth;
            yScale = (int) size.getHeight() / gridHeight;
        }

        /**
         * Paint on grid location on this field in a given color.
         */
        void drawMark(int x, int y, Color color) {
            g.setFill(color);
            g.fillRect(x * xScale, y * yScale, xScale - 1, yScale - 1);
        }
    }
}
