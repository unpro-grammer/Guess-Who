package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;

public class GuessingController {
  @FXML Rectangle leadScientist;
  @FXML Rectangle labTechnician;
  @FXML Rectangle scholar;
  @FXML TextArea explainationField;
  @FXML Button confimGuessingButton;

  @FXML
  private void confirmGuessing(ActionEvent event) throws IOException {
    App.setRoot("gameover");
  }

  @FXML
  private void selectSuspect(MouseEvent event) throws IOException {
    leadScientist.getStyleClass().remove("highlight");
    labTechnician.getStyleClass().remove("highlight");
    scholar.getStyleClass().remove("highlight");
    Rectangle selected = (Rectangle) event.getSource();
    selected.getStyleClass().add("highlight");
  }
}
