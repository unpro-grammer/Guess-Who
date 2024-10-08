package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;

public class ChemicalClueController extends ClueController {

  @FXML private ImageView chemicalLabel;

  /** Initialise chemical clue screen. */
  @FXML
  @Override
  void initialize() {
    super.initialize();
    chemicalLabel.setVisible(false);
  }

  /**
   * Display label of chemical.
   *
   * @throws IOException
   */
  @FXML
  private void onOpenLabel() throws IOException {
    chemicalLabel.setVisible(true);
  }

  /** Hide label of chemical. */
  @FXML
  private void closeLabel() {
    chemicalLabel.setVisible(false);
  }

  /**
   * Exit chemical clue.
   *
   * @throws IOException
   */
  @FXML
  private void onExitClue() throws IOException {
    App.setRoot("room");
    System.out.println("exit chemical clue");
  }
}
