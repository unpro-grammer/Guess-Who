package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;

public class LabTechnicianController extends RoomController {
  @FXML private ImageView labtechnician;
  @FXML private ImageView labtechnicianturned;
  @FXML private Label timerLabel;
  @FXML private ImageView pauseButton;
  @FXML private ImageView suspectThinking;
  @FXML private ImageView suspectSpeaking;
  @FXML private ImageView guessRequirementImg;
  @FXML private Rectangle beforeGuess;

  /** Initialise the scene in which the lab technician is. */
  @FXML
  public void initialize() {
    super.initialize();

    hideTurned();
  }

  /** Pause the music in the background. */
  @FXML
  public void onPauseClick(MouseEvent event) {
    super.onPauseClick(event);
  }

  /** Show the lab technician thinking. */
  @FXML
  public void showSuspectThinking() {
    super.showSuspectThinking();
  }

  /** Show the lab technician speaking. */
  @FXML
  public void showSuspectSpeaking() {
    super.showSuspectSpeaking();
  }

  /** Show the lab tech when turned around. */
  @FXML
  public void showTurned() {
    if (!App.isChatOpen()) {
      labtechnician.setVisible(false);
      labtechnicianturned.setVisible(true);
    }
  }

  /** Hide the turned around version of the lab tech. */
  @FXML
  public void hideTurned() {
    if (!App.isChatOpen()) {
      labtechnician.setVisible(true);
      labtechnicianturned.setVisible(false);
    }
  }

  /** Show the requirements for guessing. */
  @FXML
  protected void onGuessRequirements() {
    super.onGuessRequirements();
  }

  /** Hide the requirements for guessing. */
  @FXML
  protected void onGuessRequirementsExit() {
    super.onGuessRequirementsExit();
  }

  /** Handle the click of the rectangle for the lab tech. */
  @FXML
  @Override
  protected void handleRectangleClick(MouseEvent event) throws IOException {
    super.handleRectangleClick(event);
  }
}
