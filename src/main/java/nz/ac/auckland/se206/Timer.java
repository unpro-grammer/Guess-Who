package nz.ac.auckland.se206;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

public class Timer {
  private static final int TIMER_DURATION = 60; // Example duration in seconds
  private Thread timerThread;
  final Label timerLabel;

  public Timer(Label timerLabel) {
    this.timerLabel = timerLabel;
  }

  private void startTimer() {
    Task<Void> timerTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            for (int i = TIMER_DURATION; i >= 0; i--) {
              final int currentTime = i;
              Platform.runLater(() -> timerLabel.setText(formatTime(currentTime)));
              Thread.sleep(1000); // Sleep for 1 second
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

  private void handleTimeOver() {
    // Define what happens when the timer reaches 0
    System.out.println("Time's up!");
  }
}
