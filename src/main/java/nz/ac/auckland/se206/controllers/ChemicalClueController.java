package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;

/**
 * Controller for the chemical clue scene. This scene is where the player solves the chemical clue.
 */
public class ChemicalClueController extends ClueController {

  @FXML private ImageView chemicalLabel;

  /** Initialise chemical clue screen with one interactable. */
  @FXML
  @Override
  void initialize() {
    super.initialize();
    chemicalLabel.setVisible(false);
  }

  /**
   * Display the zoomed in label of chemical.
   *
   * @throws IOException if chemical label is not found
   */
  @FXML
  private void onOpenLabel() throws IOException {
    chemicalLabel.setVisible(true);
  }

  /** Hide the zoomed in label of chemical. */
  @FXML
  private void closeLabel() {
    chemicalLabel.setVisible(false);
  }

  /**
   * Exit the chemical clue and return to room.
   *
   * @throws IOException if room is not found
   */
  @FXML
  private void onExitClue() throws IOException {
    App.setRoot("room");
    System.out.println("exit chemical clue");
  }
}
