package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;

public class HomeController {

  @FXML private ImageView backstory;
  @FXML private Label timerLabel;
  @FXML private ImageView timer;
  @FXML private ImageView homeImage;
  @FXML private Button startBtn;

  @FXML
  void initialize() {
    // backstory.setVisible(false);
    // timerLabel.setVisible(false);
    // timer.setVisible(false);
  }

  @FXML
  void showFolders() {
    startBtn.setDisable(true);
    ParallelTransition parallelTransition = new ParallelTransition();
    parallelTransition
        .getChildren()
        .addAll(
            createTranslateTransition(homeImage, homeImage, 1500),
            createTranslateTransition(homeImage, startBtn, 1500));
    parallelTransition.setOnFinished(
        event -> {
          homeImage.setVisible(false);
          startBtn.setVisible(false);
        });
    parallelTransition.play();

    // startBtn.setVisible(false);
    // homeImage.setVisible(false);
    backstory.setVisible(true);
    timerLabel.setVisible(true);
    timer.setVisible(true);

    timerLabel.setText(App.getTimer().formatTime(App.getTimer().getCurrentTime()));
    App.getTimer().setLabel(timerLabel);

    Font font = Font.loadFont(getClass().getResourceAsStream("/fonts/sonoMedium.ttf"), 27);
    System.out.println(font);
    timerLabel.setFont(font);

    App.getTimer().startTimer();
  }

  private TranslateTransition createTranslateTransition(
      Node reference, Node toMove, double duration) {
    TranslateTransition transition = new TranslateTransition(Duration.millis(duration), toMove);
    transition.setByY(-reference.getLayoutY() - reference.getBoundsInParent().getHeight());
    return transition;
  }

  @FXML
  private void onStart() {
    try {

      App.actuallyStart();

    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("Starting game");
  }
}
