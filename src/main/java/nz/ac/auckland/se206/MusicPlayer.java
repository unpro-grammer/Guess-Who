package nz.ac.auckland.se206;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicPlayer {
  private static MediaPlayer mediaPlayer;

  public static void playAudio(String filePath) {
    try {
      if (mediaPlayer != null) {
        mediaPlayer.stop();
        mediaPlayer.dispose();
      }
      mediaPlayer = new MediaPlayer(new Media(App.class.getResource(filePath).toURI().toString()));
      mediaPlayer.play();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
