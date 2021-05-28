package pandemic;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import static pandemic.Event.*;

/**
 * Simulation controls.
 *
 * @author Peter Sander
 */
class Controls implements SimulatorView {
    Slider speedSlider;
    private AnimationTimer timer;

    @Override
    public void start() {
        Stage stage = new Stage();
        stage.setTitle("Simulation control panel");
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    private Parent createContent() {
        VBox main=new VBox();
        main.setPrefWidth(500);
        main.setSpacing(5);

        HBox parametre1 = new HBox();
        HBox parametre2 = new HBox();
        HBox parametre3 = new HBox();
        HBox parametre4 = new HBox();
        HBox parametre5 = new HBox();
        HBox parametre6 = new HBox();

        parametre1.setAlignment(Pos.CENTER);
        parametre2.setAlignment(Pos.CENTER);
        parametre3.setAlignment(Pos.CENTER);
        parametre4.setAlignment(Pos.CENTER);
        parametre5.setAlignment(Pos.CENTER);
        parametre6.setAlignment(Pos.CENTER);



        TextField texte1 = new TextField(String.valueOf(Simulator.HEALTHY_CREATION_PROBABILITY));
        TextField texte2 = new TextField(String.valueOf(Simulator.INFECTED_CREATION_PROBABILITY));
        TextField texte3 = new TextField(String.valueOf(INFECTION_RATE.probability));
        TextField texte4 = new TextField(String.valueOf(INFECTION_DURATION.probability));
        TextField texte5 = new TextField(String.valueOf(MORTALITY_RATE));
        TextField texte6 = new TextField(String.valueOf(Human.MAX_CONTAMINATION));

        Button Btn1 = new Button("Healthy probability");
        Button Btn2 = new Button("Infected probability");
        Button Btn3 = new Button("infection rate");
        Button Btn4 = new Button("infection duration");
        Button Btn5 = new Button("mortality rate");
        Button Btn6 = new Button("maximum contamination");

        Btn1.setMinWidth(main.getPrefWidth()/2);
        Btn2.setMinWidth(main.getPrefWidth()/2);
        Btn3.setMinWidth(main.getPrefWidth()/2);
        Btn4.setMinWidth(main.getPrefWidth()/2);
        Btn5.setMinWidth(main.getPrefWidth()/2);
        Btn6.setMinWidth(main.getPrefWidth()/2);

        texte1.setMinWidth(main.getPrefWidth()/2);
        texte2.setMinWidth(main.getPrefWidth()/2);
        texte3.setMinWidth(main.getPrefWidth()/2);
        texte4.setMinWidth(main.getPrefWidth()/2);
        texte5.setMinWidth(main.getPrefWidth()/2);
        texte6.setMinWidth(main.getPrefWidth()/2);


        parametre1.getChildren().addAll(Btn1,texte1);
        parametre2.getChildren().addAll(Btn2,texte2);
        parametre3.getChildren().addAll(Btn3,texte3);
        parametre4.getChildren().addAll(Btn4,texte4);
        parametre5.getChildren().addAll(Btn5,texte5);
        parametre6.getChildren().addAll(Btn6,texte6);



        HBox change = new HBox();
        change.setAlignment(Pos.CENTER);
        Button changes = new Button("save changes");
        changes.setOnAction(actionEvent -> {
            Simulator.setHealthyCreationProbability(Double.parseDouble(texte1.getText()));
            Simulator.setInfectedCreationProbability(Double.parseDouble(texte2.getText()));
            INFECTION_RATE.probability=Double.parseDouble(texte3.getText());
            INFECTION_DURATION.probability=Double.parseDouble(texte4.getText());
            MORTALITY_RATE.probability=Double.parseDouble(texte5.getText());
            Human.setMaxContamination(Integer.parseInt(texte6.getText()));
        });
        change.getChildren().add(changes);


        HBox root = new HBox();

        root.setAlignment(Pos.CENTER);
        Button startBtn = new Button("Start");
        startBtn.setOnAction(evt -> timer.start());
        Button pauseBtn = new Button("Pause");
        pauseBtn.setOnAction(evt -> timer.stop());


        HBox speedBox = new HBox();
        speedBox.setAlignment(Pos.CENTER);
        speedSlider = new Slider(0, 1, 0.5);  // in secs
        speedSlider.setShowTickMarks(true);
        speedSlider.setShowTickLabels(true);
        speedSlider.setMajorTickUnit(0.25f);
        speedSlider.setBlockIncrement(0.1f);
        speedBox.getChildren().addAll(new Label("Slow"), speedSlider, new Label("Fast"));
        Button quitBtn = new Button("Quit");
        quitBtn.setOnAction(evt -> Platform.exit());


        startBtn.setMinWidth(main.getPrefWidth()/4);
        pauseBtn.setMinWidth(main.getPrefWidth()/4);
        quitBtn.setMinWidth(main.getPrefWidth()/4);


        root.getChildren().addAll(startBtn,pauseBtn,quitBtn);
        main.getChildren().addAll(parametre1,parametre2,parametre3,parametre4,parametre5,parametre6,change,root,speedBox);

        return main;
    }

    void setTimer(AnimationTimer timer) {
        this.timer = timer;
    }

    /**
     * Converts speed reading from secs to msecs.
     */
    long getSpeed() {
        return 1000 - (long) (1000 * speedSlider.getValue());
    }

    @Override
    public void setColor(State state, Color color) {
    }

    @Override
    public void showStatus(int step, Field field) {
    }

    @Override
    public boolean isViable(Field field) {
        return true;
    }

    @Override
    public void reset() {
    }
}