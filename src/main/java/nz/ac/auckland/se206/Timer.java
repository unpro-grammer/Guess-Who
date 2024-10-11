package nz.ac.auckland.se206;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * The Timer class provides functionality to create a countdown timer that displays the time on a
 * JavaFX Label. It can also trigger a sound and a callback when the time reaches zero.
 */
public class Timer {

  /** The thread in which the timer runs. */
  private Thread timerThread;

  /** Stores the current time in seconds. */
  private int currentTime;

  /** The label to display the countdown. */
  private Label timerLabel;

  /** The action to be executed when the timer reaches zero. */
  private Runnable onTimeOver;

  /** Indicates if the timer is paused. */
  private boolean isPaused = false;

  /** The MediaPlayer used to play a sound when the timer ends. */
  private MediaPlayer soundPlayer;

  /**
   * Constructs a Timer instance with a specified label, duration, and callback action when the
   * timer ends.
   *
   * @param timerLabel the Label on which to display the remaining time
   * @param duration the initial countdown duration in seconds
   * @param onTimeOver the Runnable action to execute when the timer reaches zero
   */
  public Timer(Label timerLabel, int duration, Runnable onTimeOver) {
    this.onTimeOver = onTimeOver; // Callback for when timer finishes
    this.timerLabel = timerLabel;
    this.currentTime = duration; // Initialize currentTime with the full duration
  }

  /**
   * Sets a new Label to display the remaining time.
   *
   * @param timerLabel the Label to be updated with the timer countdown
   */
  public void setLabel(Label timerLabel) {
    this.timerLabel = timerLabel;
  }

  /**
   * Starts the countdown timer. The timer will decrement every second and update the label with the
   * current time. If the timer reaches zero, it will play a sound and execute the onTimeOver
   * callback.
   */
  public void startTimer() {
    Task<Void> timerTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            while (currentTime >= 0) { // Use class-level currentTime
              if (!isPaused) {
                // Update the timer label on the JavaFX Application thread
                Platform.runLater(() -> timerLabel.setText(formatTime(currentTime)));
                Thread.sleep(1000); // Sleep for 1 second
                currentTime--; // Decrement the class-level currentTime
              }
            }

            // When the timer reaches zero, execute the time-over actions
            Platform.runLater(() -> handleTimeOver());
            return null;
          }
        };

    // Start the timer thread as a daemon so it exits when the application closes
    timerThread = new Thread(timerTask);
    timerThread.setDaemon(true);
    timerThread.start();
  }

  /**
   * Formats the given time in seconds into a string of "MM : SS".
   *
   * @param seconds the time in seconds to format
   * @return the formatted time as a string in "MM : SS" format
   */
  public String formatTime(int seconds) {
    int minutes = seconds / 60;
    int remainingSeconds = seconds % 60;
    return String.format("%02d   %02d", minutes, remainingSeconds);
  }

  /** Pauses the countdown timer and essentially stops it. */
  public void pauseTimer() {
    isPaused = true;
    if (timerThread != null) {
      timerThread.interrupt(); // Interrupt the timer thread if it's running
    }
  }

  /**
   * Handles the actions to perform when the timer reaches zero, such as playing a sound and
   * executing the callback.
   */
  public void handleTimeOver() {
    // Play a sound when the timer reaches zero
    Media sound = new Media(App.class.getResource("/sounds/timerup.mp3").toExternalForm());
    soundPlayer = new MediaPlayer(sound);
    soundPlayer.setVolume(0.8); // Set the volume

    System.out.println(soundPlayer);

    Platform.runLater(() -> soundPlayer.play());
    onTimeOver.run(); // Execute the callback
    System.out.println("Time's up!");
  }

  /**
   * Gets the current time remaining on the timer.
   *
   * @return the current time in seconds
   */
  public int getCurrentTime() {
    return currentTime;
  }

  /**
   * Sets the current time remaining on the timer.
   *
   * @param currentTime the time in seconds to set as the current time
   */
  public void setCurrentTime(int currentTime) {
    this.currentTime = currentTime;
  }
}
