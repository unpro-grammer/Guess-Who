package nz.ac.auckland.se206;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
// import mediaplayer
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Timer {
  private Thread timerThread;
  private int currentTime; // To store the current time
  private Label timerLabel;
  private Runnable onTimeOver;
  private boolean isPaused = false;
  private MediaPlayer soundPlayer;

  public Timer(Label timerLabel, int duration, Runnable onTimeOver) {
    this.onTimeOver = onTimeOver; // callback for when timer finishes
    this.timerLabel = timerLabel;
    this.currentTime = duration; // Initialize currentTime with the full duration
  }

  public void setLabel(Label timerLabel) {
    this.timerLabel = timerLabel;
  }

  // Modify this method to avoid re-initializing currentTime inside the task
  public void startTimer() {
    Task<Void> timerTask =
        new Task<Void>() {

          @Override
          protected Void call() throws Exception {
            while (currentTime >= 0) { // Use class-level currentTime
              if (!isPaused) {
                Platform.runLater(() -> timerLabel.setText(formatTime(currentTime)));
                Thread.sleep(1000); // Sleep for 1 second
                currentTime--; // Decrement the class-level currentTime
              }
            }

            // Action to be performed when the timer reaches 0
            Platform.runLater(
                () -> {
                  handleTimeOver();
                });

            return null;
          }
        };

    timerThread = new Thread(timerTask);
    timerThread.setDaemon(true); // Ensure the thread exits when the application closes
    timerThread.start();
  }

  public String formatTime(int seconds) {
    int minutes = seconds / 60;
    int remainingSeconds = seconds % 60;
    return String.format("%02d:%02d", minutes, remainingSeconds);
  }

  public void pauseTimer() {
    isPaused = true; // pause the timer
  }

  public void handleTimeOver() {
    // when timer reaches 0
    Media sound = new Media(App.class.getResource("/sounds/timerup.mp3").toExternalForm());
    soundPlayer = new MediaPlayer(sound);
    // set volume
    soundPlayer.setVolume(0.8);

    System.out.println(soundPlayer);

    Platform.runLater(
        () -> {
          soundPlayer.play();
        });
    onTimeOver.run();
    System.out.println("Time's up!");
  }

  // Method to return the current time on the timer
  public int getCurrentTime() {
    return currentTime;
  }

  // Method to set the current time on the timer
  public void setCurrentTime(int currentTime) {
    this.currentTime = currentTime;
  }
}
