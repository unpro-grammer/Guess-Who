package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;

public class HomeController {

  @FXML private ImageView backstory;

  @FXML
  void initialize() {
    backstory.setVisible(false);
  }

  @FXML
  void showFolders() {
    backstory.setVisible(true);
  }

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
