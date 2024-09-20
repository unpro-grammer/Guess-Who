package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.MusicPlayer;
import nz.ac.auckland.se206.Timer;
import nz.ac.auckland.se206.speech.TextToSpeech;

/**
 * Controller class for the room view. Handles user interactions within the room where the user can
 * chat with customers and guess their profession.
 */
public class RoomController {

  protected static boolean isFirstTimeInit = true;
  private static GameStateContext context = App.getContext();
  private static RoomController ctrl;
  private static MediaPlayer speaker;

  /**
   * Initializes the room view. If it's the first time initialization, it will provide instructions
   * via text-to-speech.
   */
  public static void setFirstTime() {
    isFirstTimeInit = true;
  }

  /**
   * Enables the guess button, allowing the player to submit their guess. This is usually triggered
   * after the player has gathered enough information.
   */
  @FXML
  public static void enableGuessButton() {
    ctrl.btnGuess.setDisable(false);
    System.out.println("Guess button enabled");
  }

  /**
   * Retrieves the instance of the RoomController. This can be used by other classes to access the
   * controller and its methods.
   *
   * @return the RoomController instance
   */
  public static RoomController getRoomController() {
    return ctrl;
  }

  @FXML private AnchorPane anchor;
  @FXML private Button clueSceneBtn;
  @FXML private Button leadScientistSceneButton;
  @FXML private Button labTechnicianSceneButton;
  @FXML private Button scholarSceneButton;
  @FXML private Button btnGuess;
  @FXML private ImageView mapOverlay;
  @FXML private ImageView mapIcon;
  @FXML private ImageView pauseButton;
  @FXML private ImageView suspectThinking;
  @FXML private ImageView suspectSpeaking;
  @FXML private ImageView openLocker;
  @FXML private Label timerLabel;
  @FXML private Rectangle rectLabTechnician;
  @FXML private Rectangle rectScholar;
  @FXML private Rectangle rectLeadScientist;
  @FXML private Rectangle rectLocker;

  protected Timer timer;

  private Image pauseImage = new Image("/images/pauseButton.png");
  private Image playImage = new Image("/images/play-button.png");

  /**
   * Initializes the room controller. This method sets up the timer, hides the map overlay, disables
   * or enables the guess button based on player interactions, plays the introductory speech and
   * background music, and configures the pause button for the music.
   */
  @FXML
  public void initialize() {

    // Set up the timer label
    timerLabel.setText(App.getTimer().formatTime(App.getTimer().getCurrentTime()));
    App.getTimer().setLabel(timerLabel);

    hideMap();

    // Enable or disable the guess button based on interaction progress
    if (App.isInteractedEnough()) {
      btnGuess.setDisable(false);
    } else {
      btnGuess.setDisable(true);
    }

    // First-time initialization logic
    if (isFirstTimeInit) {
      // Load and play the introductory speech
      Media speech = new Media(App.class.getResource("/sounds/whostole.mp3").toExternalForm());
      speaker = new MediaPlayer(speech);
      speaker.setVolume(0.8); // Set volume to 80%
      Platform.runLater(speaker::play);

      // Use Text-to-Speech to provide instructions
      TextToSpeech.speak(
          "Chat with the three customers, and guess who is the " + context.getProfessionToGuess());

      // Start the timer and play background music
      isFirstTimeInit = false;
      App.getTimer().startTimer();
      MusicPlayer.playAudio("/sounds/lofifocusbeat.mp3");

    } else {
      // Update the pause button image based on the music state
      if (MusicPlayer.isPlaying()) {
        pauseButton.setImage(pauseImage);
      } else {
        pauseButton.setImage(playImage);
      }
    }
    hideOpen(); // Hides any open elements initially
    ctrl = this; // Store the controller instance
  }

  /**
   * Hides the map and shows the map icon. Also disables the scene buttons for clues and suspects.
   */
  protected void hideMap() {
    mapOverlay.setVisible(false);
    mapIcon.setVisible(true);
    clueSceneBtn.setVisible(false);
    leadScientistSceneButton.setVisible(false);
    labTechnicianSceneButton.setVisible(false);
    scholarSceneButton.setVisible(false);
  }

  /**
   * Shows the map overlay and hides the map icon. Enables the scene buttons for clues and suspects.
   */
  @FXML
  protected void showMap() {
    // show map overlay, hide map icon
    mapOverlay.setVisible(true);
    mapIcon.setVisible(false);
    // display the navigation buttons
    clueSceneBtn.setVisible(true);
    leadScientistSceneButton.setVisible(true);
    labTechnicianSceneButton.setVisible(true);
    scholarSceneButton.setVisible(true);
  }

  /** Closes the map overlay by calling the `hideMap()` method. */
  @FXML
  protected void closeMap() {
    ChatController.resetDisplayedChat();
    hideMap();
  }

  /**
   * Displays the "suspect thinking" visual on the screen, indicating that a suspect is processing
   * or reflecting.
   */
  @FXML
  public void showSuspectThinking() {
    suspectThinking.setVisible(true);
  }

  /**
   * Displays the "suspect speaking" visual on the screen, indicating that a suspect is currently
   * talking.
   */
  @FXML
  public void showSuspectSpeaking() {
    suspectSpeaking.setVisible(true);
  }

  /** Hides the "suspect thinking" visual, usually after the suspect has finished processing. */
  public void hideSuspectThinking() {
    suspectThinking.setVisible(false);
  }

  /** Hides the "suspect speaking" visual, usually after the suspect has finished talking. */
  public void hideSuspectSpeaking() {
    suspectSpeaking.setVisible(false);
  }

  /**
   * Handles the click event on the pause button for music playback. Toggles between play and pause
   * for the background music and updates the button icon accordingly.
   *
   * @param event the mouse event triggered by clicking the pause button
   */
  @FXML
  public void onPauseClick(MouseEvent event) {
    // toggle between play and pause, alongside icons
    if (MusicPlayer.isPlaying()) {
      MusicPlayer.pauseAudio();
      pauseButton.setImage(playImage);
    } else {
      MusicPlayer.unPauseAudio();
      pauseButton.setImage(pauseImage);
    }
  }

  /**
   * Handles the key pressed event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyPressed(KeyEvent event) {
    System.out.println("Key " + event.getCode() + " pressed");
  }

  /**
   * Handles the key released event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyReleased(KeyEvent event) {
    System.out.println("Key " + event.getCode() + " released");
  }

  /**
   * Handles mouse clicks on rectangles representing people in the room.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @throws IOException if there is an I/O error
   */
  @FXML
  protected void handleRectangleClick(MouseEvent event) throws IOException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleRectangleClick(event, clickedRectangle.getId());
  }

  /**
   * Handles the guess button click event.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  protected void handleGuessClick(ActionEvent event) throws IOException {
    context.handleGuessClick();
  }

  /**
   * Handles the transition between rooms when the player clicks on a room button. This method
   * captures the source of the event (the clicked button) and uses its ID to trigger the
   * appropriate room transition logic in the game context.
   *
   * @param event the mouse event triggered by clicking a room button
   * @throws IOException if there is an error during the transition
   */
  @FXML
  protected void handleRoomTransition(MouseEvent event) throws IOException {
    Button clickedRoom = (Button) event.getSource();
    context.handleRoomTransition(event, clickedRoom.getId());
  }

  /**
   * Displays the open locker by making it visible on the screen. This is used when the player
   * interacts with a locker in the game.
   */
  @FXML
  protected void showOpen() {
    if (openLocker != null) {
      openLocker.setVisible(true);
    }
  }

  /**
   * Hides the open locker by making it invisible on the screen. This is used to close the locker
   * view after the player is done interacting with it.
   */
  @FXML
  protected void hideOpen() {
    if (openLocker != null) {
      openLocker.setVisible(false);
    }
  }

  /**
   * Disables the room buttons and suspect scene buttons, preventing the player from interacting
   * with them. This is useful when transitioning between different game states or rooms.
   */
  public void disableRoom() {
    btnGuess.setVisible(false);
    clueSceneBtn.setVisible(false);
    leadScientistSceneButton.setVisible(false);
    labTechnicianSceneButton.setVisible(false);
    scholarSceneButton.setVisible(false);
  }

  /**
   * Enables the room buttons and suspect scene buttons, allowing the player to interact with them
   * again. This is used when re-enabling the player’s ability to move between rooms or interact
   * with suspects.
   */
  public void enableRoom() {
    btnGuess.setVisible(true);
    clueSceneBtn.setVisible(true);
    leadScientistSceneButton.setVisible(true);
    labTechnicianSceneButton.setVisible(true);
    scholarSceneButton.setVisible(true);
  }

  /**
   * Disables the suspect rectangles, preventing interaction with the suspects in the current room.
   * This method checks if the suspects are present in the anchor container and disables their
   * interactivity.
   */
  public void disableSuspects() {
    if (anchor.getChildren().contains(rectLabTechnician)) {
      rectLabTechnician.setDisable(true);
    }
    if (anchor.getChildren().contains(rectScholar)) {
      rectScholar.setDisable(true);
    }
    if (anchor.getChildren().contains(rectLeadScientist)) {
      rectLeadScientist.setDisable(true);
    }
  }

  /**
   * Enables the suspect rectangles, allowing interaction with the suspects in the current room.
   * This method checks if the suspects are present in the anchor container and enables their
   * interactivity.
   */
  public void enableSuspects() {
    if (anchor.getChildren().contains(rectLabTechnician)) {
      rectLabTechnician.setDisable(false);
    }
    if (anchor.getChildren().contains(rectScholar)) {
      rectScholar.setDisable(false);
    }
    if (anchor.getChildren().contains(rectLeadScientist)) {
      rectLeadScientist.setDisable(false);
    }
  }
}
