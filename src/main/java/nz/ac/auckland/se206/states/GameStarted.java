package nz.ac.auckland.se206.states;

import java.io.IOException;
import java.util.HashSet;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.controllers.ChatController;
import nz.ac.auckland.se206.speech.FreeTextToSpeech;

/**
 * The GameStarted state of the game. Handles the initial interactions when the game starts,
 * allowing the player to chat with characters and prepare to make a guess.
 */
public class GameStarted implements GameState {

  private final GameStateContext context;
  private HashSet<String> cluesExplored = new HashSet<>();
  private boolean security1Said = false;
  private boolean security2Said = false;
  private boolean guessCluesWarned = false;
  private boolean guessChatWarned = false;

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
      case "rectSecurityGuard":
        System.out.println("Security Guard");
        if (!cluesExplored.contains("Chemical")) {
          if (!security1Said) {
            FreeTextToSpeech.speak(
                "I will try to help, although it may be hard to recall the events of that"
                    + " day without something to jog my memory.");
          }
          security1Said = true;
        } else {
          if (!security2Said) {
            FreeTextToSpeech.speak(
                "Ah, the flask? I remember the chemical had spilled over the table"
                    + " that day. Usually the lab is spotless.");
          }
          security2Said = true;
        }
        return;
      case "rectBriefcase":
        // App.showClue("zoomedbriefcase"); // ADD CLUE
        cluesExplored.add("Briefcase");
        return; // actually make a popup to give hint
      case "rectBag":
        // App.showClue("zoomedbag"); // ADD CLUE
        cluesExplored.add("Bag");
        return;
      case "rectChemical":
        // App.showClue("zoomedchemical"); // ADD CLUE
        cluesExplored.add("Chemical");
        return;
      // case objects
      case "rectLabTechnician":
      case "rectLeadScientist":
      case "rectScholar":
        break;
    }
    App.showChatbox(event, context.getProfession(rectangleId));
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

  public boolean handleGuess(Button button) {
    if (canGuess()) {
      try {
        App.hideChat();
      } catch (IOException e) {
        e.printStackTrace();
      }
      // disable guess button
      button.setDisable(true);
      return true;
    }
    return false;
  }

  private boolean checkEnoughClues() {
    if (cluesExplored.size() >= 1) {
      return true;
    }
    return false;
  }

  public boolean canGuess() {
    return checkEnoughClues() && ChatController.hasTalked();
  }

  public void handleRoomTransition(MouseEvent event, String buttonId) {
    App.switchRoom(event, buttonId);
  }
}
