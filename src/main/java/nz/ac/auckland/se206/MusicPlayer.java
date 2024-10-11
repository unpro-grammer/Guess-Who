package nz.ac.auckland.se206;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * The MusicPlayer class provides methods to play, pause, and manage background music or sound
 * effects in the application. It utilizes the JavaFX Media and MediaPlayer classes to handle audio
 * playback.
 */
public class MusicPlayer {

  /** The MediaPlayer instance used for playing audio. */
  private static MediaPlayer mediaPlayer;

  /**
   * Plays the specified audio file. If an audio file is already playing, it stops and disposes of
   * the previous MediaPlayer instance before creating a new one.
   *
   * @param filePath the path to the audio file to be played
   */
  public static void playAudio(String filePath) {
    try {
      // Stop and dispose of the previous audio if it's playing
      if (mediaPlayer != null) {
        mediaPlayer.stop();
        mediaPlayer.dispose();
      }
      // Create a new MediaPlayer with the specified audio file
      mediaPlayer = new MediaPlayer(new Media(App.class.getResource(filePath).toURI().toString()));
      mediaPlayer.setVolume(0.3); // Set the volume to 30%
      mediaPlayer.play(); // Start playing the audio

    } catch (Exception e) {
      e.printStackTrace(); // Handle any exceptions (e.g., file not found)
    }
  }

  /**
   * Checks if the current audio is playing.
   *
   * @return true if the audio is playing, false otherwise
   */
  public static boolean isPlaying() {
    return mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
  }

  /** Pauses the currently playing audio. */
  public static void pauseAudio() {
    mediaPlayer.pause();
  }

  /** Unpauses the currently paused audio. */
  public static void unPauseAudio() {
    mediaPlayer.play();
  }

  /** Stops the currently playing audio. */
  public static void stopAudio() {
    if (mediaPlayer != null) {
      mediaPlayer.stop();
    }
  }
}
