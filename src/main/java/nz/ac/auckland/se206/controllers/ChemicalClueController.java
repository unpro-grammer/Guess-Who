package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;

public class ChemicalClueController extends ClueController {

  @FXML private ImageView chemicalLabel;

  @FXML
  @Override
  void initialize() {
    super.initialize();
    chemicalLabel.setVisible(false);
  }

  @FXML
  private void onOpenLabel() throws IOException {
    chemicalLabel.setVisible(true);
  }

  @FXML
  private void closeLabel() {
    chemicalLabel.setVisible(false);
  }

  @FXML
  private void onExitClue() throws IOException {
    App.setRoot("room");
    System.out.println("exit chemical clue");
  }
}
