package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Timer;

public class GuessingController {
  private static MediaPlayer speaker;
  @FXML Rectangle leadScientist;
  @FXML Rectangle labTechnician;
  @FXML Rectangle scholar;
  @FXML TextArea explanationField;
  @FXML Button confimGuessingButton;
  @FXML Label timerLabel;

  private String userAnswer;
  private String userGuess;
  private Timer timer;

  @FXML
  void initialize() {
    App.pauseGameTimer();
    timer = App.startGuessTimer();
    timer.setLabel(timerLabel);
    timer.startTimer();
    confimGuessingButton.setDisable(true);
    Media speech = new Media(App.class.getResource("/sounds/guess.mp3").toExternalForm());
    speaker = new MediaPlayer(speech);
    // set volume
    speaker.setVolume(0.8);

    System.out.println(speaker);

    Platform.runLater(
        () -> {
          speaker.play();
        });

    explanationField.setText("Explain why you think the suspect is the thief.");
  }

  @FXML
  private void confirmGuessing(ActionEvent event) throws IOException {

    userAnswer = explanationField.getText();

    App.setUserAnswer(userAnswer);

    App.setRoot("gameover");
  }

  @FXML
  private void selectSuspect(MouseEvent event) throws IOException {
    leadScientist.getStyleClass().remove("highlight");
    labTechnician.getStyleClass().remove("highlight");
    scholar.getStyleClass().remove("highlight");
    Rectangle selected = (Rectangle) event.getSource();
    selected.getStyleClass().add("highlight");
    confimGuessingButton.setDisable(false);
    switch (selected.getId()) {
      case "leadScientist":
        userGuess = "Lead Scientist";
        break;
      case "labTechnician":
        userGuess = "Lab Technician";
        break;
      case "scholar":
        userGuess = "Scholar";
        break;
    }
    App.setUserGuess(userGuess);
    System.out.println(userGuess);
  }
}
