package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import nz.ac.auckland.se206.App;

public class ChemicalClueController extends ClueController {

  @FXML
  private void onExitClue() throws IOException {
    App.setRoot("room");
    System.out.println("exit chemical clue");
  }
}
