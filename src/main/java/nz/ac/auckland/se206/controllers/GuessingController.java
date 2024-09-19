package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;

public class GuessingController {
  @FXML Rectangle leadScientist;
  @FXML Rectangle labTechnician;
  @FXML Rectangle scholar;
  @FXML TextArea explainationField;
  @FXML Button confimGuessingButton;

  private static MediaPlayer speaker;

  @FXML
  void initialize() {
    Media speech = new Media(App.class.getResource("/sounds/guess.mp3").toExternalForm());
    speaker = new MediaPlayer(speech);
    // set volume
    speaker.setVolume(0.8);

    System.out.println(speaker);

    Platform.runLater(
        () -> {
          speaker.play();
        });

    explainationField.setText("Explain why you think the suspect is the thief.");
  }

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
