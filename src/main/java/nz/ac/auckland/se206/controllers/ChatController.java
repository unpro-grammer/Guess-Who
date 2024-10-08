package nz.ac.auckland.se206.controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionRequest;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionResult;
import nz.ac.auckland.apiproxy.chat.openai.ChatMessage;
import nz.ac.auckland.apiproxy.chat.openai.Choice;
import nz.ac.auckland.apiproxy.config.ApiProxyConfig;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;

/**
 * Controller class for the chat view. Handles user interactions and communication with the GPT
 * model via the API proxy.
 */
public class ChatController {

  // Static Fields
  private static Map<String, Boolean> firstInteraction = new HashMap<>();
  private static Map<String, ArrayList<ArrayList<String>>> chatHistories = new HashMap<>();
  private static boolean talked = false;
  private static RoomController roomController;
  private static MediaPlayer mediaPlayerChat;
  private static boolean stillTalking = false;
  private static Boolean first;
  private static HashMap<String, Integer> displayedChat = new HashMap<>();
  private static boolean canSend = true;
  private static Set<String> talkedTo = new HashSet<>();

  // Static Methods
  public static boolean isStillTalking() {
    return stillTalking;
  }

  public static void setStillTalking(boolean stillTalking) {
    ChatController.stillTalking = stillTalking;
  }

  /**
   * Resets the game to its initial state.
   *
   * <p>This method reinitializes key game variables such as chat histories, interaction flags, and
   * state indicators to their default values, preparing the game for a new session.
   */
  public static void resetGame() {

    firstInteraction.put("Lab Technician", true); // Reset the game state
    firstInteraction.put("Lead Scientist", true);
    firstInteraction.put("Scholar", true);
    chatHistories = new HashMap<>();
    canSend = true;
    stillTalking = false;
    talked = false;
    talkedTo = new HashSet<>();
    displayedChat = new HashMap<>();
    resetDisplayedChat();
  }

  public static void resetDisplayedChat() { // Reset the displayed chat messages
    displayedChat.put("Lab Technician", 0);
    displayedChat.put("Lead Scientist", 0);
    displayedChat.put("Scholar", 0);
  }

  /**
   * Retrieves the set of professions the player has talked to.
   *
   * <p>This method returns the set of professions (suspects) that the player has interacted with.
   *
   * @return a set of professions that the player has talked to
   */
  public static Set<String> getTalkedTo() {
    return talkedTo;
  }

  /**
   * Checks if the player has talked to all suspects.
   *
   * <p>This method verifies if the player has interacted with all three suspects ("Lab Technician",
   * "Lead Scientist", and "Scholar"). It returns true if the player has interacted with each.
   *
   * @return true if all suspects have been talked to, false otherwise
   */
  public static boolean hasTalkedEnough() {
    System.out.println(talkedTo.size()); // Check if the player has talked to all suspects
    return (talkedTo.size() == 3);
  }

  /**
   * Checks if the player has talked to any suspect.
   *
   * <p>This method returns the boolean value of the 'talked' flag, which tracks whether the player
   * has initiated any conversation with a suspect.
   *
   * @return true if the player has talked to at least one suspect, false otherwise
   */
  public static boolean hasTalked() {
    return talked;
  }

  /**
   * Stops any currently playing chat sound.
   *
   * <p>This method checks if there is an active MediaPlayer instance for chat audio and stops the
   * sound playback if it is currently playing.
   */
  public static void stopSounds() {
    if (mediaPlayerChat != null) { // Stop the chat sound if it is playing
      mediaPlayerChat.stop();
    }
  }

  /**
   * Sets the room controller for managing the suspect interaction.
   *
   * <p>This method assigns a RoomController instance to the class and initializes the
   * LabTechnicianController as the current room controller.
   *
   * @param roomContrl the RoomController instance to manage room activities
   */
  public static void setRoomController(RoomController roomContrl) {
    roomController = roomContrl; // Set the room controller
  }

  /**
   * Reads the content of a text file and returns it as a string.
   *
   * <p>This method reads a file from the provided file path, line by line, and returns the content
   * as a single string.
   *
   * @param filePath the path of the file to be read
   * @return the content of the file as a string
   */
  public static String readTextFile(String filePath) {
    StringBuilder content = new StringBuilder(); // Initialize the content string
    BufferedReader reader = null;

    try {
      reader = new BufferedReader(new FileReader(filePath)); // Read the file line by line

      String line;
      while ((line = reader.readLine()) != null) { // Append each line to the content
        content.append(line).append(System.lineSeparator());
      }

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try { // Close the reader when finished
        if (reader != null) {
          reader.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return content.toString();
  }

  // Instance Fields
  private String profession;
  private String filePath;
  private String name = "Speaker";
  @FXML private TextArea txtaChat;
  @FXML private TextField txtInput;
  @FXML private Button btnSend;
  @FXML private Button btnLast;
  @FXML private Button btnNext;
  private ChatCompletionRequest chatCompletionRequest;
  private Task<Void> fetchChatTask;
  private Task<Void> runGptTask;

  /**
   * Initializes the chat system when the chat window is opened.
   *
   * <p>This method sets up the initial interaction flags and configures the input field event
   * handler to send messages when the Enter key is pressed.
   *
   * @throws ApiProxyException if an error occurs during the chat initialization process
   */
  @FXML
  public void initialize() throws ApiProxyException {
    System.out.println("Chat initialized");
    firstInteraction.put("Lab Technician", true); // Initialize the interaction flags
    firstInteraction.put("Lead Scientist", true);
    firstInteraction.put("Scholar", true);
    txtaChat.getStyleClass().add("no-highlight");
    resetDisplayedChat();

    txtInput.setOnKeyPressed(
        event -> {
          if (event.getCode() == KeyCode.ENTER
              && canSend) { // Send the message when the Enter key is pressed
            try {
              onSendMessage(new ActionEvent());
            } catch (ApiProxyException | IOException e) {
              e.printStackTrace();
            }
          }
        });
  }

  /**
   * Sets the room controller for managing the suspect interaction.
   *
   * <p>This method assigns a RoomController instance
   *
   * <p>// Chat Functionality Methods
   *
   * <p>/** Sets the profession for the chat context and initializes the ChatCompletionRequest.
   *
   * @param profession the profession to set
   */
  public void setProfession(String profession) {

    stillTalking = true; // Set the profession and initialize the chat context
    System.out.println("Setting profession");
    updateChatTexts();
    this.profession = profession;
    initializeFilePath();
    fetchChatTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception { // Initialize the chat context
            try {
              btnSend.setVisible(true);
              txtInput.setVisible(true);
              first = firstInteraction.get(profession);
              ApiProxyConfig config = ApiProxyConfig.readConfig();
              chatCompletionRequest =
                  new ChatCompletionRequest(config) // Initialize the ChatCompletionRequest
                      .setN(1)
                      .setTemperature(0.2)
                      .setTopP(0.5)
                      .setMaxTokens(100);

              runGpt(new ChatMessage("system", getSystemPrompt()));

            } catch (ApiProxyException e) {
              e.printStackTrace();
            }
            return null;
          }
        };

    Thread backgroundChatThread = new Thread(fetchChatTask);
    backgroundChatThread.setDaemon(true);
    backgroundChatThread.start();
  }

  /**
   * Initializes the file path based on the selected profession.
   *
   * <p>This method assigns a specific file path for each profession to load the appropriate chat
   * prompt file.
   */
  private void initializeFilePath() {
    switch (profession) { // Select the appropriate file path based on the profession
      case "Lab Technician":
        filePath = "src/main/resources/prompts/lab_technician_2.txt";
        break;
      case "Lead Scientist":
        filePath = "src/main/resources/prompts/lead_scientist_2.txt";
        break;
      case "Scholar":
        filePath = "src/main/resources/prompts/scholar_2.txt";
        break;
      default:
        filePath = "src/main/resources/prompts/chat_2.txt";
        break;
    }
  }

  /**
   * Generates the system prompt based on the profession.
   *
   * <p>This method reads two specific text files based on the profession and concatenates their
   * content to form the system prompt for that profession.
   *
   * @return the generated system prompt as a string
   */
  private String getSystemPrompt() { // Generate the system prompt based on the profession
    Map<String, String> map = new HashMap<>();
    map.put("profession", profession);
    String firstFile, secondFile;

    System.out.println(firstInteraction);
    switch (profession) { // Select the appropriate files based on the profession
      case "Lab Technician":
        firstFile = "lab_technician.txt";
        secondFile = "lab_technician_2.txt";
        break;
      case "Lead Scientist":
        firstFile = "lead_scientist.txt";
        secondFile = "lead_scientist_2.txt";
        break;
      case "Scholar":
        firstFile = "scholar.txt";
        secondFile = "scholar_2.txt";
        break;
      default:
        firstFile = "chat.txt";
        secondFile = null;
        break;
    }

    return readTextFile(
            "src/main/resources/prompts/" + firstFile) // Concatenate the content of the two files
        + "\n\n"
        + readTextFile("src/main/resources/prompts/" + secondFile);
  }

  /**
   * Plays a sound effect based on the profession and interaction status.
   *
   * <p>This method selects and plays a greeting or response sound effect for the given profession
   * based on whether it is the first interaction with that profession. The sound is played using
   * the JavaFX `MediaPlayer`.
   *
   * @param profession the profession for which the sound effect is played
   * @param first indicates if this is the first interaction with the profession
   */
  public void playSound(
      String profession,
      boolean first) { // Play the sound effect based on the profession and interaction status
    String soundSource = "";
    System.out.println(profession);
    if (firstInteraction.get(profession)) {
      switch (profession) { // Select the appropriate sound effect based on the profession
        case "Lab Technician":
          soundSource = "labtechnician_greeting.mp3";
          break;
        case "Lead Scientist":
          soundSource = "leadscientist_greeting.mp3";
          break;
        case "Scholar":
          soundSource = "scholar_greeting.mp3";
          break;

        default:
          break;
      }
    } else {
      switch (profession) { // Select the appropriate sound effect based on the profession
        case "Lab Technician":
          soundSource = "labtechnician_hmm.mp3";
          break;
        case "Lead Scientist":
          soundSource = "leadscientist_hmm.mp3";
          break;
        case "Scholar":
          soundSource = "scholar_hmm.mp3";
          break;

        default:
          break;
      }
    }
    StringBuilder sb = new StringBuilder();
    sb.append("/sounds/");
    sb.append(soundSource);
    Media hmmSound =
        new Media(App.class.getResource(sb.toString()).toExternalForm()); // Load the sound file
    mediaPlayerChat = new MediaPlayer(hmmSound);
    mediaPlayerChat.setVolume(0.8);

    System.out.println(mediaPlayerChat);

    Platform.runLater(
        () -> {
          mediaPlayerChat.play(); // Play the sound
        });
  }

  // Chat Handling Methods

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  private void appendChatMessage(ChatMessage msg) {
    // Determine the name to display: 'assistant' or 'You'
    String name = msg.getRole().equals("assistant") ? profession : "You";
    String messageText = name + ": " + msg.getContent() + "\n\n";

    // Save the chat message to the file
    saveChatToFile(msg.getContent() + "\n");

    // Append the message to the chat text area
    txtaChat.appendText(messageText);

    // Get the chat history for the current profession, or create a new one if none exists
    ArrayList<ArrayList<String>> history =
        chatHistories.getOrDefault(profession, new ArrayList<>());

    // Add the new message to the chat history

    if (msg.getRole().equals("assistant")) {
      history.get(0).add(messageText);
      chatHistories.get(profession).get(0).add(messageText);

    } else {
      history.get(1).add(messageText);
      chatHistories.get(profession).get(1).add(messageText);
    }

    // Update the displayedChat variable to reflect the most recent message
    ChatController.displayedChat.put(profession, history.size());

    // Debugging purposes: print the updated chat history for the current profession
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * <p>This method sends the provided chat message to the GPT model for processing, handles the
   * interaction by showing and hiding the suspect's speaking and thinking animations, and returns
   * the response from the GPT model.
   *
   * @param msg the chat message to process
   * @return the response chat message generated by the GPT model
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  private ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {

    RoomController.getRoomController().hideSuspectSpeaking();
    if (stillTalking) { // Show the suspect thinking animation if they are still talking
      RoomController.getRoomController().showSuspectThinking();
    }
    chatCompletionRequest.addMessage(msg);
    try {
      canSend = false; // Disable the send button while the GPT model is processing
      btnSend.setDisable(true);

      playSound(profession, first);
      firstInteraction.put(profession, false);
      ChatCompletionResult chatCompletionResult =
          chatCompletionRequest.execute(); // Execute the GPT model
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      appendChatMessage(result.getChatMessage());
      RoomController.getRoomController().hideSuspectThinking();
      if (stillTalking) { // Show the suspect speaking animation if they are still talking
        RoomController.getRoomController().showSuspectSpeaking();
      }
      chatHistories
          .get(profession)
          .get(0)
          .add(profession + ": " + result.getChatMessage().getContent());
      canSend = true;
      btnSend.setDisable(false);
      return result.getChatMessage();
    } catch (ApiProxyException e) { // Handle any API proxy exceptions
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Saves the chat history to a file.
   *
   * <p>Appends the provided chat content to the specified file.
   *
   * @param chatContent the chat message content to save to the file
   */
  private void saveChatToFile(String chatContent) {
    try {
      Files.writeString( // Write the chat content to the file
          Paths.get(filePath),
          chatContent + "\n",
          StandardOpenOption.CREATE, // Create the file if it doesn't exist
          StandardOpenOption.APPEND);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Sends a message to the GPT model when the send button is clicked.
   *
   * <p>This method is triggered by the "Send" button. It retrieves the user's input message,
   * updates the chat history, and sends the message to the GPT model for processing asynchronously.
   * If the user has talked enough or interacted enough, additional UI elements may be updated, like
   * enabling the guess button.
   *
   * @param event the action event triggered by the send button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    if (txtInput.getText().trim().isEmpty()) { // if the input is empty,
      return;
    }

    updateChatTexts();
    String message = txtInput.getText().trim();

    if (message.isEmpty()) { // Send the message to the GPT model
      return;
    }
    chatHistories.get(profession).get(1).add("You: " + message);

    txtInput.clear(); // Clear the input field after sending the message
    ChatMessage msg = new ChatMessage("user", message);

    appendChatMessage(msg);
    talked = true;
    talkedTo.add(profession);
    if (hasTalkedEnough()) { // Check if the player has talked to all suspects
      App.setTalkedEnough(true);
    }
    if (App.isInteractedEnough()) { // Check if the player has interacted enough
      RoomController.enableGuessButton();
    }
    runGptTask =
        new Task<Void>() { // Run the GPT model asynchronously
          @Override
          protected Void call() throws Exception {
            runGpt(msg);
            return null;
          }
        };

    Thread backgroundGptThread = new Thread(runGptTask); // Start the GPT model thread
    backgroundGptThread.setDaemon(true);
    backgroundGptThread.start();
  }

  /**
   * Handles the "Next" button click event to navigate to the next chat message.
   *
   * <p>If the user has previously clicked the "Last" button to see past messages, this method
   * allows them to go forward to the next message in the chat history.
   *
   * @param event the action event triggered by the "Next" button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onNextClicked(ActionEvent event) throws ApiProxyException, IOException {
    // Check if there are chat histories available and if we haven't reached the last message
    if (!chatHistories.get(profession).isEmpty()
        && displayedChat.getOrDefault(profession, 0) < chatHistories.get(profession).size() - 1) {

      // Increment the displayed chat count for the current profession
      ChatController.displayedChat.put(
          profession, ChatController.displayedChat.getOrDefault(profession, 0) + 1);

      // Update the text area with the new message
      txtaChat.setText(
          chatHistories
              .get(profession)
              .get(0)
              .get(displayedChat.get(profession))); // Get the next message from the history

      // Hide the input field and send button if we're still in history mode
      txtInput.setVisible(false);
      btnSend.setVisible(false);
    }

    // If we reach the end of the chat history, show the input and send button
    if (displayedChat.get(profession) == chatHistories.get(profession).size() - 1) {
      txtInput.setVisible(true); // Make the input field visible
      btnSend.setVisible(true); // Make the send button visible
    }
  }

  /**
   * Clears the text area where the chat messages are displayed.
   *
   * <p>This method is used to reset the chat display area when navigating between messages or
   * updating the chat history.
   */
  private void updateChatTexts() {
    txtaChat.clear();
  }

  /**
   * Handles the "Last" button click event to navigate to the previous chat message.
   *
   * <p>If the user has previously clicked the "Next" button to see future messages, this method
   * allows them to go back one message in the chat history.
   *
   * <p>- Decreases displayedChat to show the previous message. - Hides the input and send button
   * when navigating backward through chat history.
   *
   * @param event The action event triggered by the "Last" button.
   * @throws ApiProxyException If there's an issue with the API proxy.
   * @throws IOException If an I/O error occurs.
   */
  @FXML
  private void onLastClicked(ActionEvent event) throws ApiProxyException, IOException {
    // Check if there are chat histories available and if we haven't reached the first message
    if (!chatHistories.get(profession).isEmpty() && displayedChat.getOrDefault(profession, 0) > 0) {

      // Decrement the displayed chat count for the current profession
      ChatController.displayedChat.put(
          profession, ChatController.displayedChat.getOrDefault(profession, 0) - 1);

      // Update the text area with the previous message
      txtaChat.setText(
          chatHistories
              .get(profession)
              .get(0)
              .get(displayedChat.get(profession))); // Get the previous message from the history

      // Hide the input field and send button when navigating backward
      txtInput.setVisible(false);
      btnSend.setVisible(false);
    }
  }

  /**
   * Navigates back to the previous view in the chat application.
   *
   * <p>This method handles the event triggered by clicking a "Go Back" button. It resets various UI
   * components related to the suspect's interaction (hides the suspect's speaking and thinking
   * actions) and stops any ongoing media playback for the chat.
   *
   * <p>- Stops chat-related media playback. - Hides chat and resets suspect-related UI components.
   *
   * @param event The action event triggered by the "Go Back" button.
   * @throws ApiProxyException If there is an error communicating with the API proxy.
   * @throws IOException If there is an I/O error.
   */
  @FXML
  private void onGoBack(ActionEvent event) throws ApiProxyException, IOException {
    stillTalking = false;
    RoomController.getRoomController().hideSuspectSpeaking(); // Hide the suspect speaking animation
    RoomController.getRoomController().hideSuspectThinking();
    if (mediaPlayerChat != null) {
      mediaPlayerChat.stop();
    }
    RoomController.getRoomController().hideSuspectSpeaking(); // Hide the suspect speaking animation
    RoomController.getRoomController().hideSuspectThinking();

    App.hideChat();
  }
}
