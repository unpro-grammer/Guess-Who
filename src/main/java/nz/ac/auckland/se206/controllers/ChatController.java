package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionRequest;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionResult;
import nz.ac.auckland.apiproxy.chat.openai.ChatMessage;
import nz.ac.auckland.apiproxy.chat.openai.Choice;
import nz.ac.auckland.apiproxy.config.ApiProxyConfig;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.prompts.PromptEngineering;
import nz.ac.auckland.se206.speech.FreeTextToSpeech;

/**
 * Controller class for the chat view. Handles user interactions and communication with the GPT
 * model via the API proxy.
 */
public class ChatController {

  private static Map<String, Boolean> firstInteraction = new HashMap<>();
  private static boolean talked = false;
  private static RoomController roomController;

  public static boolean hasTalked() {
    return talked;
  }

  public static void setRoomController(RoomController roomContrl) {
    roomController = roomContrl;
  }

  @FXML private TextArea txtaChat;
  @FXML private TextField txtInput;
  @FXML private Button btnSend;

  private ChatCompletionRequest chatCompletionRequest;
  private String profession;
  private String promptSource;
  private String name = "Speaker";
  private Task<Void> fetchChatTask;
  private Task<Void> runGptTask;

  private Boolean first;
  private MediaPlayer mediaPlayerChat;

  /**
   * Initializes the chat view.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  @FXML
  public void initialize() throws ApiProxyException {
    System.out.println("Chat initialized");
    firstInteraction.put("Lab Technician", true);
    firstInteraction.put("Lead Scientist", true);
    firstInteraction.put("Scholar", true);
    // Any required initialization code can be placed here
  }

  /**
   * Generates the system prompt based on the profession.
   *
   * @return the system prompt string
   */
  private String getSystemPrompt() {
    Map<String, String> map = new HashMap<>();
    map.put("profession", profession);
    first = firstInteraction.get(profession);
    switch (profession) {
      case "Lab Technician":
        promptSource = first ? "lab_technician.txt" : "lab_technician_2.txt";
        firstInteraction.put("Lab Technician", false);
        break;
      case "Lead Scientist":
        promptSource = first ? "lead_scientist.txt" : "lead_scientist_2.txt";
        firstInteraction.put("Lead Scientist", false);
        break;
      case "Scholar":
        promptSource = first ? "scholar.txt" : "scholar_2.txt";
        firstInteraction.put("Scholar", false);
        break;
      default:
        promptSource = "chat.txt";
        break;
    }
    System.out.println(promptSource);
    return PromptEngineering.getPrompt(promptSource, map);
  }

  public void playHmm() {
    Media hmmSound = new Media(App.class.getResource("/sounds/hmmm.mp3").toExternalForm());
    mediaPlayerChat = new MediaPlayer(hmmSound);
    // set volume
    mediaPlayerChat.setVolume(0.8);

    System.out.println(mediaPlayerChat);

    Platform.runLater(
        () -> {
          mediaPlayerChat.play();
        });
  }

  /**
   * Sets the profession for the chat context and initializes the ChatCompletionRequest.
   *
   * @param profession the profession to set
   */
  public void setProfession(String profession) {
    System.out.println("Setting profession");
    updateChatTexts();
    this.profession = profession;
    // begin new task to retrieve generated text via api
    fetchChatTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            try {
              first = firstInteraction.get(profession);
              ApiProxyConfig config = ApiProxyConfig.readConfig();
              chatCompletionRequest =
                  new ChatCompletionRequest(config)
                      .setN(1)
                      .setTemperature(0.2)
                      .setTopP(0.5)
                      .setMaxTokens(100);
              runGpt(new ChatMessage("system", getSystemPrompt()), first);
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
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  private void appendChatMessage(ChatMessage msg) {
    if (msg.getRole().equals("assistant")) {
      name = profession;
    } else {
      name = "You";
    }
    txtaChat.appendText(name + ": " + msg.getContent() + "\n\n");
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  private ChatMessage runGpt(ChatMessage msg, boolean first) throws ApiProxyException {
    // FreeTextToSpeech.stop();
    chatCompletionRequest.addMessage(msg);
    try {
      // handle GUI methods via main application thread, but at any point it's free
      // Platform.runLater(() -> roomController.showHmm(profession)); // ADD SOUNDFX LATER
      playHmm();
      System.out.println(profession);
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      appendChatMessage(result.getChatMessage());
      if (first) {
        FreeTextToSpeech.speak(result.getChatMessage().getContent());
      }
      // Platform.runLater(() -> roomController.hideHmm(profession)); // SOUNDFX LATER
      return result.getChatMessage();
    } catch (ApiProxyException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Sends a message to the GPT model.
   *
   * @param event the action event triggered by the send button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    updateChatTexts();
    String message = txtInput.getText().trim();
    if (message.isEmpty()) {
      return;
    }
    txtInput.clear();
    ChatMessage msg = new ChatMessage("user", message);
    appendChatMessage(msg);
    talked = true;
    runGptTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            runGpt(msg, false);
            return null;
          }
        };

    Thread backgroundGptThread = new Thread(runGptTask);
    backgroundGptThread.setDaemon(true);
    backgroundGptThread.start();
  }

  private void updateChatTexts() {
    txtaChat.clear();
  }

  /**
   * Navigates back to the previous view.
   *
   * @param event the action event triggered by the go back button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onGoBack(ActionEvent event) throws ApiProxyException, IOException {
    // FreeTextToSpeech.stop();
    App.hideChat();
  }
}
