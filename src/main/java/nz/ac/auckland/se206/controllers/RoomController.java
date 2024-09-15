package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.speech.TextToSpeech;

/**
 * Controller class for the room view. Handles user interactions within the room where the user can
 * chat with customers and guess their profession.
 */
public class RoomController {

  @FXML private Rectangle rectLabTechnician;
  @FXML private Rectangle rectScholar;
  @FXML private Rectangle rectLeadScientist;
  @FXML private Button btnGuess;
  @FXML private Button clueSceneBtn;
  @FXML private Button leadScientistSceneButton;
  @FXML private Button labTechnicianSceneButton;
  @FXML private Button scholarSceneButton;
  @FXML private ImageView openLocker;
  @FXML private Rectangle rectLocker;

  private static boolean isFirstTimeInit = true;
  private static GameStateContext context = new GameStateContext();

  /**
   * Initializes the room view. If it's the first time initialization, it will provide instructions
   * via text-to-speech.
   */
  @FXML
  public void initialize() {
    if (isFirstTimeInit) {
      TextToSpeech.speak(
          "Chat with the three customers, and guess who is the " + context.getProfessionToGuess());
      isFirstTimeInit = false;
    }
    hideOpen();
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

  @FXML
  protected void handleRoomTransition(MouseEvent event) throws IOException {
    Button clickedRoomButton = (Button) event.getSource();
    context.handleRoomTransition(event, clickedRoomButton.getId());
  }

  @FXML
  protected void showOpen() {
    openLocker.setVisible(true);
  }

  @FXML
  protected void hideOpen() {
    openLocker.setVisible(false);
  }
}
