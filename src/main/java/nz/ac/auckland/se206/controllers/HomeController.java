package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.App;

public class HomeController {
  @FXML private Button startBtn;

  @FXML
  private void onStart() {
    try {
      App.actuallyStart();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("Starting game");
  }
}
