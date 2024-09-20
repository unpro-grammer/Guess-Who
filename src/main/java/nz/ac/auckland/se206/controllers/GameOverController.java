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
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionRequest;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionResult;
import nz.ac.auckland.apiproxy.chat.openai.ChatMessage;
import nz.ac.auckland.apiproxy.chat.openai.Choice;
import nz.ac.auckland.apiproxy.config.ApiProxyConfig;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.prompts.PromptEngineering;

public class GameOverController {
  @FXML private TextArea feedbackDisplay;
  @FXML private Button playAgainButton; // used for correct guess (Scholar)
  @FXML private Button playAgainButton1; // used for incorrect guesses because position is different
  @FXML private Button playAgainButton2; // used for no guess because position is different
  @FXML private ImageView incorrectLabTech;
  @FXML private ImageView incorrectScientist;
  @FXML private ImageView correctScholar;
  @FXML private ImageView outOfTime;

  private String userAnswer;
  private ChatCompletionRequest chatCompletionRequest;
  private String profession;
  private String feedback;

  @FXML
  public void initialize() throws ApiProxyException {
    // Any required initialization code can be placed here

    // default make everything invisible
    userAnswer = App.getUserAnswer();
    incorrectLabTech.setVisible(false);
    incorrectScientist.setVisible(false);
    correctScholar.setVisible(false);
    feedbackDisplay.setVisible(false);
    playAgainButton.setVisible(false);
    playAgainButton1.setVisible(false);
    playAgainButton2.setVisible(false);
    outOfTime.setVisible(false);
    App.pauseGuessTimer();

    // depending on the player guessing result, display the correct image and related button
    switch (App.getUserGuess()) {
      case "Lead Scientist":
        incorrectScientist.setVisible(true);
        playAgainButton1.setVisible(true);
        break;
      case "Lab Technician":
        incorrectLabTech.setVisible(true);
        playAgainButton1.setVisible(true);
        break;
      case "Scholar":
      // only the Scholar one is correct and have feedback generated
        correctScholar.setVisible(true);
        feedbackDisplay.setVisible(true);
        getFeedback(userAnswer);
        playAgainButton.setVisible(true);
        break;
      default:
      // if the player instead failed to guess when ran out of time
        outOfTime.setVisible(true);
        playAgainButton2.setVisible(true);
        break;
    }
  }

  @FXML
  private void playAgain() throws IOException {
    App.setRoot("home");
  }

  /**
   * Displays the feedback based on the player's explaination during guessing
   * 
   * @param useranswer the explaination typed in by the user during guessing
   */
  public void getFeedback(String useranswer) {
    Task<Void> getFeedbackTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            try {
              ApiProxyConfig config = ApiProxyConfig.readConfig();
              chatCompletionRequest =
                  new ChatCompletionRequest(config)
                      .setN(1)
                      .setTemperature(0.2)
                      .setTopP(0.5)
                      .setMaxTokens(100);
              // runs ChatGPT to generate a feedback based on the model answer
              runGpt(new ChatMessage("system", getSystemPrompt()));
              Platform.runLater(
                  () -> {
                    // set text in feedbackDisplay to feedback
                    feedbackDisplay.setText(feedback);
                  });
            } catch (ApiProxyException e) {
              e.printStackTrace();
            }
            return null;
          }
        };

    Thread backgroundResponseThread = new Thread(getFeedbackTask);
    backgroundResponseThread.setDaemon(true);
    backgroundResponseThread.start();
  }

  public String getSystemPrompt() {
    Map<String, String> map = new HashMap<>();
    map.put("userAnswer", userAnswer);
    return PromptEngineering.getPrompt("modelanswer.txt", map);
  }

  /**
   * Runs ChatGPT to generate a feedback based on the model answer given
   * 
   * @param msg The model answer preset
   * @return resulting message from generating
   * @throws ApiProxyException if there is API error
   */
  private ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    // The same as the one in ChatController except it doesn't append but returns
    chatCompletionRequest.addMessage(msg);
    try {
      System.out.println(profession);
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      feedback = result.getChatMessage().getContent();
      return result.getChatMessage();
    } catch (ApiProxyException e) {
      e.printStackTrace();
      return null;
    }
  }
}
