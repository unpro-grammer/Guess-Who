package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import nz.ac.auckland.se206.App;

public abstract class ClueController {

  @FXML private Label timerLabel;

  /** Initialises the clue controller for any clue scene. */
  @FXML
  void initialize() {
    timerLabel.setText(App.getTimer().formatTime(App.getTimer().getCurrentTime()));
    App.getTimer().setLabel(timerLabel);
    Font font = Font.loadFont(getClass().getResourceAsStream("/fonts/sonoMedium.ttf"), 27);
    timerLabel.setFont(font);
  }
}
