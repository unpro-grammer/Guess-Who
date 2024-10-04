package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import javafx.scene.shape.Rectangle;

public class LeadScientistController extends RoomController {
  @FXML private static ImageView suspectThinking;
  @FXML private static ImageView suspectSpeaking;
  @FXML private ImageView leadscientist;
  @FXML private ImageView leadscientistturned;
  @FXML private Label timerLabel;
  @FXML private ImageView pauseButton;
  @FXML private ImageView guessRequirementImg;
  @FXML private Rectangle beforeGuess;

  public void initialize() {
    super.initialize();
    hideTurned();
  }

  @FXML
  public void onPauseClick(MouseEvent event) {
    super.onPauseClick(event);
  }

  @FXML
  public void showTurned() {
    if (!App.isChatOpen()) {
      leadscientist.setVisible(false);
      leadscientistturned.setVisible(true);
    }
  }

  @FXML
  public void hideTurned() {
    if (!App.isChatOpen()) {
      leadscientist.setVisible(true);
      leadscientistturned.setVisible(false);
    }
  }

  @FXML
  protected void onGuessRequirements() {
    super.onGuessRequirements();
  }

  @FXML
  protected void onGuessRequirementsExit() {
    super.onGuessRequirementsExit();
  }

  @FXML
  @Override
  protected void handleRectangleClick(MouseEvent event) throws IOException {
    super.handleRectangleClick(event);
  }
}
