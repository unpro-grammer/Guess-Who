package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.states.GameOver;
import nz.ac.auckland.se206.states.GameStarted;
import nz.ac.auckland.se206.states.GameState;
import nz.ac.auckland.se206.states.Guessing;

/**
 * Context class for managing the state of the game. Handles transitions between different game
 * states and maintains game data such as the professions and rectangle IDs.
 */
public class GameStateContext {

  private final String rectIdToGuess;
  private final String professionToGuess;
  private final Map<String, String> rectanglesToProfession;
  private final GameStarted gameStartedState;
  private final Guessing guessingState;
  private final GameOver gameOverState;
  private GameState gameState;

  /** Constructs a new GameStateContext and initializes the game states and professions. */
  public GameStateContext() {
    gameStartedState = new GameStarted(this);
    guessingState = new Guessing(this);
    gameOverState = new GameOver(this);

    gameState = gameStartedState;
    System.out.println(gameState);
    rectanglesToProfession = new HashMap<>();
    rectanglesToProfession.put("rectLabTechnician", "Lab Technician");
    rectanglesToProfession.put("rectScholar", "Scholar");
    rectanglesToProfession.put("rectLeadScientist", "Lead Scientist");

    System.out.println(rectanglesToProfession);

    rectIdToGuess = "rectScholar";
    professionToGuess = rectanglesToProfession.get(rectIdToGuess);
  }

  /**
   * Sets the current state of the game.
   *
   * @param state the new state to set
   */
  public void setState(GameState state) {
    this.gameState = state;
  }

  /**
   * Gets the current state of the game.
   *
   * @return the current state of the game
   */
  public GameState getState() {
    return gameState;
  }

  /** Sets the game started state. */
  public void setGameStartedState() {
    gameState = gameStartedState;
  }

  /** Sets the guessing state in which you make a guess!!!. */
  public void setGuessingState() {
    gameState = guessingState;
  }

  /** Sets the game over state. */
  public void setGameOverState() {
    gameState = gameOverState;
  }

  /**
   * Gets the initial game started state.
   *
   * @return the game started state
   */
  public GameState getGameStartedState() {
    return gameStartedState;
  }

  /**
   * Gets the guessing state, a game state.
   *
   * @return the guessing state
   */
  public GameState getGuessingState() {
    return guessingState;
  }

  /**
   * Gets the game over state, a game state.
   *
   * @return the game over state
   */
  public GameState getGameOverState() {
    return gameOverState;
  }

  /**
   * Gets the profession to be guessed.
   *
   * @return the profession to guess
   */
  public String getProfessionToGuess() {
    return professionToGuess;
  }

  /**
   * Gets the ID of the rectangle to be guessed.
   *
   * @return the rectangle ID to guess
   */
  public String getRectIdToGuess() {
    return rectIdToGuess;
  }

  /**
   * Gets the profession associated with a specific rectangle ID.
   *
   * @param rectangleId the rectangle ID
   * @return the profession associated with the rectangle ID
   */
  public String getProfession(String rectangleId) {
    return rectanglesToProfession.get(rectangleId);
  }

  /**
   * Handles the event when a rectangle is clicked.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @param rectangleId the ID of the clicked rectangle
   * @throws IOException if there is an I/O error
   */
  public void handleRectangleClick(MouseEvent event, String rectangleId) throws IOException {
    gameState.handleRectangleClick(event, rectangleId);
  }

  /**
   * Handles the event when the guess button is clicked.
   *
   * @throws IOException if there is an I/O error
   */
  public void handleGuessClick() throws IOException {
    gameState.handleGuessClick();
  }

  /**
   * Handles the event when a rectangle is clicked. Depending on the clicked rectangle, will
   * transition to the corresponding room.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @param clickedId the ID of the clicked rectangle
   */
  public void handleRoomTransition(MouseEvent event, String clickedId) {
    if (gameState instanceof GameStarted) {
      gameStartedState.handleRoomTransition(event, clickedId);
    }
  }
}
