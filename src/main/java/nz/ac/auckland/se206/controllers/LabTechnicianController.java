package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;

public class LabTechnicianController extends RoomController {
  @FXML private ImageView labtechnician;
  @FXML private ImageView labtechnicianturned;

  @FXML
  public void initialize() {
    hideTurned();
  }

  @FXML
  public void showTurned() {
    if (!App.isChatOpen()) {
      labtechnician.setVisible(false);
      System.out.println("labtechnician hide");
      labtechnicianturned.setVisible(true);
      System.out.println("labtechnicianturned show");
    }
  }

  @FXML
  public void hideTurned() {
    if (!App.isChatOpen()) {
      labtechnician.setVisible(true);
      System.out.println("labtechnician show");
      labtechnicianturned.setVisible(false);
      System.out.println("labtechnicianturned hide");
    }
  }

  @FXML
  @Override
  protected void handleRectangleClick(MouseEvent event) throws IOException {
    super.handleRectangleClick(event);
  }
}
