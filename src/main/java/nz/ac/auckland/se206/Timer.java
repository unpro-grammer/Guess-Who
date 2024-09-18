package nz.ac.auckland.se206;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

public class Timer {
  private Thread timerThread;
  private int currentTime; // To store the current time
  private Label timerLabel;

  public Timer(Label timerLabel, int duration) {
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
              Platform.runLater(() -> timerLabel.setText(formatTime(currentTime)));
              Thread.sleep(1000); // Sleep for 1 second
              currentTime--; // Decrement the class-level currentTime
            }

            // Action to be performed when the timer reaches 0
            Platform.runLater(() -> handleTimeOver());

            return null;
          }
        };

    timerThread = new Thread(timerTask);
    timerThread.setDaemon(true); // Ensure the thread exits when the application closes
    timerThread.start();
  }

  private String formatTime(int seconds) {
    int minutes = seconds / 60;
    int remainingSeconds = seconds % 60;
    return String.format("%02d:%02d", minutes, remainingSeconds);
  }

  public void handleTimeOver() {
    // Define what happens when the timer reaches 0
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
