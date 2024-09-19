package nz.ac.auckland.se206;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicPlayer {
  private static MediaPlayer mediaPlayer;

  public MusicPlayer() {}

  public static void playAudio(String filePath) {
    try {
      if (mediaPlayer != null) {
        mediaPlayer.stop();
        mediaPlayer.dispose();
      }
      mediaPlayer = new MediaPlayer(new Media(App.class.getResource(filePath).toURI().toString()));
      mediaPlayer.setVolume(0.3);
      mediaPlayer.play();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static boolean isPlaying() {
    return mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
  }

  public static void playAudio() {
    mediaPlayer.play();
  }

  public static void pauseAudio() {
    mediaPlayer.pause();
  }
}
