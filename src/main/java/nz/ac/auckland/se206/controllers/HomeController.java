package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;

/** Controller for the home scene. This scene is where the player starts the game. */
public class HomeController {

  @FXML private ImageView backstory;
  @FXML private Label timerLabel;
  @FXML private ImageView timer;
  @FXML private ImageView homeImage;
  @FXML private Button startBtn;

  private boolean homescreenGone = false;

  /** Initialises the home controller for the home scene. */
  @FXML
  void initialize() {}

  /** Show the profiles of the suspects in which the timer begins. */
  @FXML
  void showFolders() {

    // Background thread to check when the home screen is gone.
    Task<Void> backgroundCounter =
        new Task<Void>() {

          @Override
          protected Void call() throws Exception {

            // Arbitrary duration (good enough)
            Thread.sleep(1160);
            homescreenGone = true;

            return null;
          }
        };
    Thread backgroundThread = new Thread(backgroundCounter);
    backgroundThread.start();

    // Transition the home screen out of view
    startBtn.setDisable(true);
    ParallelTransition parallelTransition = new ParallelTransition();
    parallelTransition
        .getChildren()
        .addAll(
            createTranslateTransition(homeImage, homeImage, 1500),
            createTranslateTransition(homeImage, startBtn, 1500));
    parallelTransition.setOnFinished(
        event -> {
          homeImage.setVisible(false);
          startBtn.setVisible(false);
        });
    parallelTransition.play();

    // Show the profiles of the suspects
    backstory.setVisible(true);
    timerLabel.setVisible(true);
    timer.setVisible(true);

    // Start the timer
    timerLabel.setText(App.getTimer().formatTime(App.getTimer().getCurrentTime()));
    App.getTimer().setLabel(timerLabel);

    // Set the font of the timer
    Font font = Font.loadFont(getClass().getResourceAsStream("/fonts/sonoMedium.ttf"), 27);
    System.out.println(font);
    timerLabel.setFont(font);

    App.getTimer().startTimer();
  }

  /**
   * Creates a translate transition for the given node.
   *
   * @param reference node for position reference
   * @param toMove node that is experiencing transition
   * @param duration duration of the transition
   * @return the translate transition
   */
  private TranslateTransition createTranslateTransition(
      Node reference, Node toMove, double duration) {
    TranslateTransition transition = new TranslateTransition(Duration.millis(duration), toMove);
    transition.setByY(-reference.getLayoutY() - reference.getBoundsInParent().getHeight());

    return transition;
  }

  /** Starts the game by transitioning to the room scene. */
  @FXML
  private void onStart() {
    if (homescreenGone) {
      try {

        // Set the root to the room scene
        App.actuallyStart();

      } catch (IOException e) {
        e.printStackTrace();
      }
      System.out.println("Starting game");
    }
  }
}
