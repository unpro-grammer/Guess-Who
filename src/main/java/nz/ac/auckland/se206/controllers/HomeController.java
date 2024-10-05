package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.FadeTransition;
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
    backstory.setVisible(false);
    timerLabel.setVisible(false);
    timer.setVisible(false);
  }

  @FXML
  void showFolders() {

    fadeInNode(backstory);
    fadeInNode(timerLabel);
    fadeInNode(timer);
    startBtn.setVisible(false);
    homeImage.setVisible(false);
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

  private void fadeInNode(Node node) {
    node.setOpacity(0);
    node.setVisible(true);
    FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), node);
    fadeTransition.setFromValue(0);
    fadeTransition.setToValue(1);
    fadeTransition.play();
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
