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
  @FXML Button labButton;
  @FXML Button leadButton;
  @FXML Button scholarButton;

  @FXML
  private void playAgain(ActionEvent event) throws IOException {
    App.setRoot("home");
  }

  @FXML
  private void changeSuspect(ActionEvent event) {
    Button clickedButton = (Button) event.getSource();
    String suspectName = clickedButton.getText();
    if (App.gameResult(suspectName)) {
      thiefResultDisplay.setText("The " + suspectName + " is the thief!");
      feedbackDisplay.setText("He stole the research notes!");
      winLoseDisplay.setText("You WON!");
    } else {
      thiefResultDisplay.setText("The " + suspectName + " is not the thief!");
      feedbackDisplay.setText("He didn't steal the research notes!");
      winLoseDisplay.setText("You LOST!");
    }
  }
}
