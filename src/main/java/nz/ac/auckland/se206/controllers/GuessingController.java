package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Timer;

public class GuessingController {
  @FXML private TextArea explanationField;
  private static MediaPlayer speaker;
  @FXML private Rectangle leadScientist;
  @FXML private Rectangle labTechnician;
  @FXML private Rectangle scholar;
  @FXML private Button confimGuessingButton;
  @FXML private Label timerLabel;
  @FXML private ImageView selectScientist;
  @FXML private ImageView selectLabtech;
  @FXML private ImageView selectScholar;

  private static String userAnswer;
  private String userGuess;
  private Timer timer;

  public void setUserExplanation() {
    userAnswer = explanationField.getText();
    App.setUserAnswer(userAnswer);
  }

  @FXML
  void initialize() {
    hideSelects();
    App.pauseGameTimer();
    timer = App.startGuessTimer();
    timer.setLabel(timerLabel);
    timer.startTimer();
    confimGuessingButton.setDisable(true);
    Font font = Font.loadFont(getClass().getResourceAsStream("/fonts/handwritingFont.ttf"), 27);
    System.out.println("Font Loaded: " + font.getName());
    Font.loadFont(getClass().getResourceAsStream("/fonts/sonoMedium.ttf"), 27);
    Media speech = new Media(App.class.getResource("/sounds/guess.mp3").toExternalForm());
    speaker = new MediaPlayer(speech);
    // set volume
    speaker.setVolume(0.8);

    System.out.println(speaker);

    Platform.runLater(
        () -> {
          speaker.play();
        });
  }

  @FXML
  private void onConfirmGuessing() throws IOException {

    userAnswer = explanationField.getText();
    App.setUserAnswer(userAnswer);

    App.setRoot("gameover");
    Media sound = new Media(App.class.getResource("/sounds/timerup.mp3").toExternalForm());
    speaker = new MediaPlayer(sound);
    // set volume
    speaker.setVolume(0.8);

    System.out.println(speaker);

    Platform.runLater(
        () -> {
          speaker.play();
        });
  }

  /**
   * Runs when player is selecting a suspect to be guessed during guessing state
   *
   * @param event mouse event triggered by clicking on the suspect rectangle
   * @throws IOException if there is I/O error
   */
  @FXML
  private void selectSuspect(MouseEvent event) throws IOException {
    leadScientist.getStyleClass().remove("highlight");
    labTechnician.getStyleClass().remove("highlight");
    scholar.getStyleClass().remove("highlight");
    Rectangle selected = (Rectangle) event.getSource();
    selected.getStyleClass().add("highlight");

    // Player is only able to guess after selecting a suspect
    confimGuessingButton.setDisable(false);
    switch (selected.getId()) {
      case "leadScientist":
        hideSelects();
        selectScientist.setVisible(true);
        userGuess = "Lead Scientist";
        break;
      case "labTechnician":
        hideSelects();
        selectLabtech.setVisible(true);
        userGuess = "Lab Technician";
        break;
      case "scholar":
        hideSelects();
        selectScholar.setVisible(true);
        userGuess = "Scholar";
        break;
    }

    // Update status in App
    App.setUserGuess(userGuess);
    System.out.println(userGuess);
  }

  private void hideSelects() {
    selectScientist.setVisible(false);
    selectLabtech.setVisible(false);
    selectScholar.setVisible(false);
  }
}
