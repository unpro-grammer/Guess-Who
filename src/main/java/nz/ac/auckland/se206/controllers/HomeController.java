package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class HomeController {

  @FXML private ImageView backstory;
  @FXML private Label timerLabel;
  @FXML private ImageView timer;

  @FXML
  void initialize() {
    backstory.setVisible(false);
    timerLabel.setVisible(false);
    timer.setVisible(false);
  }

  @FXML
  void showFolders() {
    backstory.setVisible(true);
    timerLabel.setVisible(true);
    timer.setVisible(true);

    timerLabel.setText(App.getTimer().formatTime(App.getTimer().getCurrentTime()));
    App.getTimer().setLabel(timerLabel);
    App.getTimer().startTimer();

    Font font = Font.loadFont(getClass().getResourceAsStream("/fonts/sonoMedium.ttf"), 27);
    System.out.println(font);
    timerLabel.setFont(font);
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
