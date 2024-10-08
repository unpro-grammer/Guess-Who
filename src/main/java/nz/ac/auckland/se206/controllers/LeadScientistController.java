package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;

public class LeadScientistController extends RoomController {
  @FXML private static ImageView suspectThinking;
  @FXML private static ImageView suspectSpeaking;
  @FXML private ImageView leadscientist;
  @FXML private ImageView leadscientistturned;
  @FXML private Label timerLabel;
  @FXML private ImageView pauseButton;
  @FXML private ImageView guessRequirementImg;
  @FXML private Rectangle beforeGuess;

  /** Initialise the room in which the lead scientist is. */
  public void initialize() {
    super.initialize();
    hideTurned();
  }

  /** Pause the music in the background. */
  @FXML
  public void onPauseClick(MouseEvent event) {
    super.onPauseClick(event);
  }

  /** Show the lead scientist turned around. */
  @FXML
  public void showTurned() {
    if (!App.isChatOpen()) {
      leadscientist.setVisible(false);
      leadscientistturned.setVisible(true);
    }
  }

  /** Hide the lead scientist turned around. */
  @FXML
  public void hideTurned() {
    if (!App.isChatOpen()) {
      leadscientist.setVisible(true);
      leadscientistturned.setVisible(false);
    }
  }

  /** Show the guess requirements on hover. */
  @FXML
  protected void onGuessRequirements() {
    super.onGuessRequirements();
  }

  /** Hide the guess requirements on exit. */
  @FXML
  protected void onGuessRequirementsExit() {
    super.onGuessRequirementsExit();
  }

  /** Handle the rectangle click of the lead scientist. */
  @FXML
  @Override
  protected void handleRectangleClick(MouseEvent event) throws IOException {
    super.handleRectangleClick(event);
  }
}
