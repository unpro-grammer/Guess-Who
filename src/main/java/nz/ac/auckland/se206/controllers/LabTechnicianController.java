package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import javafx.scene.shape.Rectangle;

public class LabTechnicianController extends RoomController {
  @FXML private ImageView labtechnician;
  @FXML private ImageView labtechnicianturned;
  @FXML private Label timerLabel;
  @FXML private ImageView pauseButton;
  @FXML private ImageView suspectThinking;
  @FXML private ImageView suspectSpeaking;
  @FXML private ImageView guessRequirementImg;
  @FXML private Rectangle beforeGuess;

  @FXML
  public void initialize() {
    super.initialize();

    hideTurned();
  }

  @FXML
  public void onPauseClick(MouseEvent event) {
    super.onPauseClick(event);
  }

  @FXML
  public void showSuspectThinking() {
    super.showSuspectThinking();
  }

  @FXML
  public void showSuspectSpeaking() {
    super.showSuspectSpeaking();
  }

  @FXML
  public void showTurned() {
    if (!App.isChatOpen()) {
      labtechnician.setVisible(false);
      labtechnicianturned.setVisible(true);
    }
  }

  @FXML
  public void hideTurned() {
    if (!App.isChatOpen()) {
      labtechnician.setVisible(true);
      labtechnicianturned.setVisible(false);
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
