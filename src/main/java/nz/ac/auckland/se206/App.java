package nz.ac.auckland.se206;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import nz.ac.auckland.se206.controllers.ChatController;
import nz.ac.auckland.se206.controllers.GuessingController;
import nz.ac.auckland.se206.speech.FreeTextToSpeech;
import nz.ac.auckland.se206.states.GameState;

/**
 * This is the entry point of the JavaFX application. This class initializes and runs the JavaFX
 * application.
 */
public class App extends Application {

  // 5 minute timer <TOCHANGE> 301 secs cause of transition
  private static Timer timer = new Timer(null, 301, () -> switchToGuessing());

  private static Timer guessTimer;
  private static Scene scene;
  private static Parent chatView = null;
  private static ChatController chatController = null;
  private static String feedback = "";
  private static String userAnswer = "";
  private static String userGuess = "";
  private static GameStateContext context = new GameStateContext();
  private static boolean talkedEnough = false;
  private static Set<String> cluesExplored = new HashSet<>();
  private static GuessingController guessCtrl = null;

  private static String[] charactersToShow =
      new String[] {
        "rectLabTechnician",
        "rectScholar",
        "rectLeadScientist",
        "leadscientist",
        "labtechnician",
        "scholar",
      };

  private static String[] charactersToHide =
      new String[] {
        "rectLabTechnician",
        "rectScholar",
        "rectLeadScientist",
        "leadscientist",
        "labtechnician",
        "scholar",
        "leadscientistturned",
        "labtechnicianturned",
        "scholarturned"
      };

  private static boolean isChatOpen = false;

  /**
   * Checks if the user has interacted enough with the characters and explored enough clues to make
   * a guess.
   *
   * @return true if the user has interacted enough, false otherwise
   */
  public static boolean isInteractedEnough() {
    return talkedEnough && cluesExplored.size() >= 1; // <TOCHANGE> UNCOMMENT THIS

  }

  /**
   * Checks if the user has explored enough clues to make a guess.
   *
   * @return true if the user has explored enough clues, false otherwise
   */
  public static boolean exploredEnoughClues() {
    return cluesExplored.size() >= 1;

  }

  /** Clears all chat history files to reset the game. */
  public static void clearChats() {
    // empty all chat history files
    replaceFileContent(
        "src/main/resources/prompts/emptyfile.txt",
        "src/main/resources/prompts/lab_technician_2.txt");
    replaceFileContent(
        "src/main/resources/prompts/emptyfile.txt",
        "src/main/resources/prompts/lead_scientist_2.txt");
    replaceFileContent(
        "src/main/resources/prompts/emptyfile.txt", "src/main/resources/prompts/scholar_2.txt");
  }

  /** Resets the game state to the initial state, directing player to home. */
  public static void resetGame() {
    // clear all variables and reset game state
    clearChats();

    // <TOCHANGE> 5 minute timer. 301 secs cause of transition
    timer = new Timer(null, 301, () -> switchToGuessing());

    guessTimer = null;
    feedback = "";
    userAnswer = "";
    userGuess = "";
    guessCtrl = null;
    context.setState(context.getGameStartedState());
    // game completion progress to 0
    talkedEnough = false;
    cluesExplored = new HashSet<>();
  }

  /**
   * Sets the boolean value of whether the user has talked enough to make a guess.
   *
   * @param talkedEnough the boolean value to set
   */
  public static void setTalkedEnough(boolean talkedEnough) {
    App.talkedEnough = talkedEnough;
  }

  /**
   * Gets the boolean value of whether the user has talked enough to make a guess.
   *
   * @return the boolean value of whether the user has talked enough to make a guess
   */
  public static boolean getTalkedEnough() {
    return talkedEnough;
  }

  /**
   * Adds a clue to the set of clues explored.
   *
   * @param clue the clue to add
   */
  public static void addClueExplored(String clue) {
    cluesExplored.add(clue);
  }

  /**
   * Gets the Game State Context at present.
   *
   * @return the Game State Context at present
   */
  public static GameStateContext getContext() {
    return context;
  }

  /**
   * Sets the Game State Context.
   *
   * @param context the Game State Context to set
   */
  public static void setContext(GameStateContext context) {
    App.context = context;
  }

  /** Pauses the game timer for playing. */
  public static void pauseGameTimer() {
    if (timer != null) {
      timer.pauseTimer();
    }
  }

  /** Pauses the guess timer for guessing. */
  public static void pauseGuessTimer() {
    if (guessTimer != null) {
      guessTimer.pauseTimer();
    }
  }

  /**
   * Gets the user's guess if applicable.
   *
   * @return the user's guess of who the suspect is.
   */
  public static String getUserGuess() {
    return userGuess;
  }

  /**
   * Gets the current game state of the game state context.
   *
   * @return the current game state of the game state context
   */
  public static GameState getCurrentState() {
    return context.getState();
  }

  /**
   * Sets the guess controller variable.
   *
   * @param guessCtrl the guess controller to set
   */
  public static void setGuessCtrl(GuessingController guessCtrl) {
    App.guessCtrl = guessCtrl;
  }

  /** Switches the game state to the guessing state. */
  private static void switchToGuessing() {

    pauseGameTimer();
    System.out.println("Paused game timer");
    // if insufficient interactions, switch to game over

    if (!isInteractedEnough()) {
      System.out.println(
          "talked to: " + ChatController.getTalkedTo() + " clues explored: " + cluesExplored);
      switchToGameOver();
      System.out.println("Switching to game over from switchToGuessing");
      return;
    }

    context.setGuessingState();

    // update scene IF ENOUGH INTERACTIONS
    try {
      // App.setRoot("guessing");
      FXMLLoader guessLoader = new FXMLLoader(App.class.getResource("/fxml/guessing.fxml"));
      Parent guessRoot = guessLoader.load();
      guessCtrl = guessLoader.getController();
      scene.setRoot(guessRoot);
      System.out.println("Switching to guessing");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** Switches the game state to the game over state. */
  private static void switchToGameOver() {

    context.setGameOverState();
    System.out.println("Switching to game over from switchToGameOver");
    try {
      // NEED MORE LOGIC TO HANDLE WHETHER A GUESS HAS BEEN CLICKED (selectesuspect in
      // gameovercontroller) // actually wait this is already done because suspect will be null.
      // Thankfully I have unified the gameover screen for any sort of time running out.

      if (guessCtrl != null) {
        guessCtrl.setUserExplanation();
      }
      App.setRoot("gameover");

      System.out.println("Switching to game over from switchToGameOver");

    } catch (IOException e) {
      e.printStackTrace();
    }
    // update scene

  }

  /**
   * Sets the user's guess of who the suspect is.
   *
   * @param userGuess guessed suspect by player
   */
  public static void setUserGuess(String userGuess) {
    App.userGuess = userGuess;
  }

  /**
   * Gets the user's answer to why they guessed the suspect.
   *
   * @return the user's explanation for their guess
   */
  public static String getUserAnswer() {
    return userAnswer;
  }

  /**
   * Sets the user's answer to why they guessed the suspect.
   *
   * @param userAnswer the user's explanation for their guess
   */
  public static void setUserAnswer(String userAnswer) {
    App.userAnswer = userAnswer;
  }

  /**
   * The main method that launches the JavaFX application.
   *
   * @param args the command line arguments
   */
  public static void main(final String[] args) {
    clearChats();
    launch();
  }

  /**
   * Replaces the content of the destination file with the content of the source file.
   *
   * @param sourceFilePath the path of the source file
   * @param destinationFilePath the path of the destination file
   */
  public static void replaceFileContent(String sourceFilePath, String destinationFilePath) {
    Path sourcePath = Paths.get(sourceFilePath);
    Path destinationPath = Paths.get(destinationFilePath);

    try {
      // Read content from the source file
      byte[] content = Files.readAllBytes(sourcePath);

      // Write content to the destination file, overwriting it
      Files.write(destinationPath, content);
    } catch (IOException e) {
      System.err.println("An error occurred while replacing file content: " + e.getMessage());
    }
  }

  /**
   * Appends the content to the specified file.
   *
   * @param content the content to append
   * @param filePath the path of the file to append to
   */
  public static void appendToFile(String content, String filePath) {
    Path path = Paths.get(filePath);

    try {
      // Append content to the file
      Files.write(path, content.getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);

    } catch (IOException e) {
      System.err.println("An error occurred while appending to the file: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Gets the game timer and grabs it.
   *
   * @return the game timer
   */
  public static Timer getTimer() {
    return timer;
  }

  /**
   * Starts the game timer and hence it starts.
   *
   * @return the game timer
   */
  public static Timer startGuessTimer() {
    // 60 sec timer <TOCHANGE>
    guessTimer = new Timer(null, 60, () -> switchToGameOver());
    return guessTimer;
  }

  /**
   * Sets the root of the scene to the specified FXML file.
   *
   * @param fxml the name of the FXML file (without extension)
   * @throws IOException if the FXML file is not found
   */
  public static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFxml(fxml));
  }

  public static void setRoot(Parent root) {
    scene.setRoot(root);
  }

  /**
   * Loads the FXML file and returns the associated node. The method expects that the file is
   * located in "src/main/resources/fxml".
   *
   * @param fxml the name of the FXML file (without extension)
   * @return the root node of the FXML file
   * @throws IOException if the FXML file is not found
   */
  private static Parent loadFxml(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  /**
   * Opens the chat view and sets the profession in the chat controller.
   *
   * @param event the mouse event that triggered the method
   * @param profession the profession to set in the chat controller
   * @throws IOException if the FXML file is not found
   */
  public static void showChatbox(MouseEvent event, String profession) throws IOException {

    // cache chat node and controller
    System.out.println("Showing chat box");
    if (chatView == null) {
      FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/chat.fxml"));
      chatView = loader.load();
      chatController = loader.getController();
    }

    chatController.setProfession(profession);

    Parent root = scene.getRoot();
    if (root instanceof AnchorPane) {
      AnchorPane mainPane = (AnchorPane) root;

      // chatPane is the space in each fxml file where the chat view will be displayed
      AnchorPane chatPane = (AnchorPane) mainPane.lookup("#chatPane");
      if (chatPane != null) {
        System.out.println("Chat pane found");
        chatPane.getChildren().clear();
        chatPane.getChildren().add(chatView);
        chatPane.setVisible(true);
      }

      isChatOpen = true;

      // find characters in scene by id and hide them

      for (String characterId : charactersToHide) {
        Node character = mainPane.lookup("#" + characterId);
        hideCharacter(character);
      }
    }
  }

  /**
   * Hides the character in the scene.
   *
   * @param character the character to hide
   */
  private static void hideCharacter(Node character) {
    if (character != null) {
      character.setVisible(false);
      System.out.println("Hiding " + character.getId());
    }
  }

  /**
   * Shows the character in the scene.
   *
   * @param character the character to show
   */
  private static void showCharacter(Node character) {
    if (character != null) {
      character.setVisible(true);
      System.out.println("Showing " + character.getId());
    }
  }

  /**
   * Hides the chat view and shows the characters in the scene.
   *
   * @throws IOException if the FXML file is not found
   */
  public static void hideChat() throws IOException {
    Parent root = scene.getRoot();
    if (root instanceof AnchorPane) {
      AnchorPane mainPane = (AnchorPane) root;
      AnchorPane chatPane = (AnchorPane) mainPane.lookup("#chatPane");
      if (chatPane != null) {
        chatPane.getChildren().clear();
        chatPane.setVisible(false);
      }

      isChatOpen = false;

      // find characters by id and show them
      for (String characterId : charactersToShow) {
        Node character = mainPane.lookup("#" + characterId);
        showCharacter(character);
      }
    }
  }

  /**
   * Switches the room based on the room button clicked.
   *
   * @param event the mouse event that triggered the method
   * @param roomButtonId the id of the room button clicked
   */
  public static void switchRoom(MouseEvent event, String roomButtonId) {

    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    ChatController.setStillTalking(false);

    // checks which room button was clicked and switches to the corresponding scene
    // TO-DO: Do the loading of fxml files in platform run later? Doesn't make a visual difference
    // but at least buttons won't freeze.
    try {
      ChatController.stopSounds();
      App.closeChat();
      switch (roomButtonId) {
        case "clueSceneBtn":
          System.out.println("Switching to clue scene");
          setRoot("room");
          break;
        case "leadScientistSceneButton":
          System.out.println("Switching to lead scientist scene");
          setRoot("leadscientist");
          break;
        case "labTechnicianSceneButton":
          System.out.println("Switching to lab technician scene");
          setRoot("labtechnician");
          break;
        case "scholarSceneButton":
          System.out.println("Switching to scholar scene");
          setRoot("scholar");
          break;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    stage.setScene(scene);
    stage.setResizable(false);
    stage.show();
  }

  /**
   * Checks if the chat view is open.
   *
   * @return true if the chat view is open, false otherwise
   */
  public static boolean isChatOpen() {
    return isChatOpen;
  }

  /** Closes the chat view - well, makes sure it's flagged as closed. */
  public static void closeChat() {
    isChatOpen = false;
  }

  /**
   * Checks whether the game was won or lost.
   *
   * @param suspectName the name of the suspect guessed by the player
   * @return true if the player correctly guessed the scholar, false otherwise
   */
  public static boolean gameResult(String suspectName) {
    if (suspectName.equals("Scholar")) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Actually starts the game by fading out the home screen and fading in the clue room.
   *
   * @throws IOException if the "src/main/resources/fxml/room.fxml" file is not found
   */
  public static void actuallyStart() throws IOException {
    // App.setRoot("room");
    Parent clueRoot = loadFxml("room");

    // Extract the timer and timerLabel from the home scene
    Node timerHome = scene.lookup("#timer");
    Node timerLabelHome = (Label) scene.lookup("#timerLabel");

    // Extract the timer and timerLabel from the clueRoot scene
    Node timerClue = clueRoot.lookup("#timer");
    Node timerLabelClue = clueRoot.lookup("#timerLabel");

    timerClue.setOpacity(1);
    timerLabelClue.setOpacity(1);

    Pane timerPane = new Pane();
    timerPane.getChildren().addAll(timerHome, timerLabelHome);

    // Maintain original positions of the timer and timerLabel
    timerHome.setLayoutX(timerHome.getLayoutX());
    timerHome.setLayoutY(timerHome.getLayoutY());
    timerLabelHome.setLayoutX(timerLabelHome.getLayoutX());
    timerLabelHome.setLayoutY(timerLabelHome.getLayoutY());

    // Create a StackPane to hold the current and new scenes
    StackPane stackPane = new StackPane(scene.getRoot(), clueRoot, timerPane);

    clueRoot.setOpacity(0);

    Font font = Font.loadFont(App.class.getResourceAsStream("/fonts/sonoMedium.ttf"), 29.9);

    if (timerLabelHome != null) {
      Label homeLabel = (Label) timerLabelHome;
      homeLabel.setFont(font);
      homeLabel.setText(App.getTimer().formatTime(App.getTimer().getCurrentTime()));
      App.getTimer().setLabel(homeLabel);
    }

    // Fade homescreen out
    FadeTransition fadeHome = new FadeTransition(Duration.millis(250), scene.getRoot());
    scene.setRoot(stackPane);
    stackPane.setStyle("-fx-background-color: #c5c7e1;");
    fadeHome.setFromValue(1);
    fadeHome.setToValue(0);

    fadeHome.setOnFinished(
        event -> {
          stackPane.getChildren().remove(scene.getRoot());

          if (timerLabelClue != null) {
            Label roomLabel = (Label) timerLabelClue;
            roomLabel.setFont(font);
            roomLabel.setText(App.getTimer().formatTime(App.getTimer().getCurrentTime()));
            App.getTimer().setLabel(roomLabel);
          }

          // Fade clue room in
          FadeTransition fadeClue = new FadeTransition(Duration.millis(250), clueRoot);
          fadeClue.setFromValue(0);
          fadeClue.setToValue(1);
          fadeClue.play();

          fadeClue.setOnFinished(
              fadeEvent -> {
                stackPane.getChildren().remove(timerHome);
                stackPane.getChildren().remove(timerLabelHome);
                // Hide timerPane
                timerPane.setVisible(false);
              });
        });

    fadeHome.play();
  }

  /**
   * Handles the window close event by clearing the chat history and deallocating the synthesizer.
   *
   * @param event the window event that triggered the method
   */
  private void handleWindowClose(WindowEvent event) {
    clearChats();
    FreeTextToSpeech.deallocateSynthesizer();
  }

  /**
   * Sets the feedback message to display to the user.
   *
   * @param feedbackmsg the feedback message to display
   */
  public static void setFeedback(String feedbackmsg) {
    feedback = feedbackmsg;
  }

  /**
   * Gets the feedback message to display to the user.
   *
   * @return the feedback message to display
   */
  public static String getFeedback() {
    return feedback;
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "room" scene.
   *
   * @param stage the primary stage of the application
   * @throws IOException if the "src/main/resources/fxml/room.fxml" file is not found
   */
  @Override
  public void start(final Stage stage) throws IOException {
    Parent root = loadFxml("home");
    scene = new Scene(root);
    stage.setScene(scene);
    stage.setResizable(false);
    stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));
    stage.show();
    stage.setOnCloseRequest(event -> handleWindowClose(event));
    root.requestFocus();
  }

  /**
   * Sets the label of the timer.
   *
   * @param timerLabel the label to set
   */
  public void setLabel(Label timerLabel) {
    timer.setLabel(timerLabel);
  }

  /**
   * Sets the label of the guess timer.
   *
   * @param timerLabel the label to set
   */
  public void setGuessTimerLabel(Label timerLabel) {
    guessTimer.setLabel(timerLabel);
  }
}
