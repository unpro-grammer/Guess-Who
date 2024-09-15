package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class LeadScientistController extends RoomController {
  @FXML private ImageView leadscientist;
  @FXML private ImageView leadscientistturned;

  @FXML
  public void initialize() {
    hideTurned();
  }

  @FXML
  void showTurned() {
    leadscientist.setVisible(false);
    leadscientistturned.setVisible(true);
  }

  @FXML
  void hideTurned() {
    leadscientist.setVisible(true);
    leadscientistturned.setVisible(false);
  }
}
