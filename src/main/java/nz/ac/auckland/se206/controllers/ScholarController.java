package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;

public class ScholarController extends RoomController {
  @FXML private ImageView scholar;
  @FXML private ImageView scholarturned;
  @FXML private Label timerLabel;
  @FXML private ImageView guessRequirementImg;
  @FXML private Rectangle beforeGuess;

  /** Initialise the room in which the scholar is. */
  @FXML
  public void initialize() {
    super.initialize();
    hideTurned();
  }

  /** Show turned version of the scholar. */
  @FXML
  public void showTurned() {
    if (!App.isChatOpen()) {
      scholar.setVisible(false);
      scholarturned.setVisible(true);
    }
  }

  /** Hide turned version of the scholar. */
  @FXML
  public void hideTurned() {
    if (!App.isChatOpen()) {
      scholar.setVisible(true);
      scholarturned.setVisible(false);
    }
  }

  /** Show any remaining requirements for guessing. */
  @FXML
  protected void onGuessRequirements() {
    super.onGuessRequirements();
  }

  /** Hide any remaining requirements for guessing. */
  @FXML
  protected void onGuessRequirementsExit() {
    super.onGuessRequirementsExit();
  }

  /** Handle rectangle click of the scholar rectangle. */
  @FXML
  @Override
  protected void handleRectangleClick(MouseEvent event) throws IOException {
    super.handleRectangleClick(event);
  }
}
