package nz.ac.auckland.se206.states;

import java.io.IOException;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.speech.TextToSpeech;

/**
 * The GameStarted state of the game. Handles the initial interactions when the game starts,
 * allowing the player to chat with characters and prepare to make a guess.
 */
public class GameStarted implements GameState {

  private final GameStateContext context;

  /**
   * Constructs a new GameStarted state with the given game state context.
   *
   * @param context the context of the game state
   */
  public GameStarted(GameStateContext context) {
    this.context = context;
  }

  /**
   * Handles the event when a rectangle is clicked. Depending on the clicked rectangle, it either
   * provides an introduction or transitions to the chat view.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @param rectangleId the ID of the clicked rectangle
   * @throws IOException if there is an I/O error
   */
  @Override
  public void handleRectangleClick(MouseEvent event, String rectangleId) throws IOException {
    // Transition to chat view or provide an introduction based on the clicked rectangle
    switch (rectangleId) {
      case "rectCashier":
        TextToSpeech.speak("Welcome to my cafe!");
        return;
      case "rectWaitress":
        TextToSpeech.speak("Hi, let me know when you are ready to order!");
        return;
    }
    // Open chat with the selected character
    App.openChat(event, context.getProfession(rectangleId));
  }

  /**
   * Handles the event when the guess button is clicked. Prompts the player to make a guess and
   * transitions to the guessing state.
   *
   * @throws IOException if there is an I/O error
   */
  @Override
  public void handleGuessClick() throws IOException {
    App.setRoot("guessing");
    context.setState(context.getGuessingState());
  }

  /**
   * Handles the transition between different rooms in the game based on the button clicked.
   *
   * @param event the mouse event triggered by clicking a room transition button
   * @param buttonId the ID of the clicked button representing the room transition
   */
  public void handleRoomTransition(MouseEvent event, String buttonId) {
    try {
      switch (buttonId) {
        case "leadScientistSceneButton":
          // Transition to Lead Scientist Room
          App.switchRoom(event, "leadScientist.fxml");
          break;
        case "labTechnicianSceneButton":
          // Transition to Lab Technician Room
          App.switchRoom(event, "labTechnician.fxml");
          break;
        case "scholarSceneButton":
          // Transition to Scholar Room
          App.switchRoom(event, "scholar.fxml");
          break;
        default:
          // Handle other cases or unknown transitions
          TextToSpeech.speak("Room transition not available.");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
