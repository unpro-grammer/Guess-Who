package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;

public class ScholarController extends RoomController {
  @FXML private ImageView scholar;
  @FXML private ImageView scholarturned;
  @FXML private Label timerLabel;

  @FXML
  public void initialize() {
    super.initialize();
    hideTurned();
  }

  @FXML
  public void showTurned() {
    if (!App.isChatOpen()) {
      scholar.setVisible(false);
      scholarturned.setVisible(true);
    }
  }

  @FXML
  public void hideTurned() {
    if (!App.isChatOpen()) {
      scholar.setVisible(true);
      scholarturned.setVisible(false);
    }
  }

  @FXML
  @Override
  protected void handleRectangleClick(MouseEvent event) throws IOException {
    super.handleRectangleClick(event);
  }
}
