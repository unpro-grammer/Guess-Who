package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class GuessingController {
  @FXML Rectangle leadScientist;
  @FXML Rectangle labTechnician;
  @FXML Rectangle scholar;
  @FXML TextArea explainationField;
  @FXML Button confimGuessingButton;

  @FXML
  private void confirmGuessing(ActionEvent event) throws IOException {}

  @FXML
  private void selectSuspect(MouseEvent event) throws IOException {}
}
