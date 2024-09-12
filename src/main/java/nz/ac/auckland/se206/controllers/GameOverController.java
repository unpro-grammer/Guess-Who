package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.App;

public class GameOverController {
  @FXML Text thiefResultDisplay;
  @FXML TextArea feedbackDisplay;
  @FXML Text winLoseDisplay;
  @FXML Button playAgainButton;

  @FXML
  private void playAgain(ActionEvent event) throws IOException {
    App.setRoot("home");
  }
}
