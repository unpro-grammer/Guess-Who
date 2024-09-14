package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import nz.ac.auckland.se206.controllers.ChatController;
import nz.ac.auckland.se206.speech.FreeTextToSpeech;

/**
 * This is the entry point of the JavaFX application. This class initializes and runs the JavaFX
 * application.
 */
public class App extends Application {

  private static Scene scene;

  /**
   * The main method that launches the JavaFX application.
   *
   * @param args the command line arguments
   */
  public static void main(final String[] args) {
    launch();
  }

  /**
   * Sets the root of the scene to the specified FXML file.
   *
   * @param fxml the name of the FXML file (without extension)
   * @throws IOException if the FXML file is not found
   */
  public static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFxml(fxml));
  }

  /**
   * Loads the FXML file and returns the associated node. The method expects that the file is
   * located in "src/main/resources/fxml".
   *
   * @param fxml the name of the FXML file (without extension)
   * @return the root node of the FXML file
   * @throws IOException if the FXML file is not found
   */
  private static Parent loadFxml(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  /**
   * Opens the chat view and sets the profession in the chat controller.
   *
   * @param event the mouse event that triggered the method
   * @param profession the profession to set in the chat controller
   * @throws IOException if the FXML file is not found
   */
  public static void openChat(MouseEvent event, String profession) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/chat.fxml"));
    Parent root = loader.load();

    ChatController chatController = loader.getController();
    chatController.setProfession(profession);

    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  public static void switchRoom(MouseEvent event, String roomButtonId) {

    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

    // checks which room button was clicked and switches to the corresponding scene
    // TO-DO: Do the loading of fxml files in platform run later? Doesn't make a visual difference
    // but at least buttons won't freeze.
    try {
      switch (roomButtonId) {
        case "clueSceneBtn":
          System.out.println("Switching to clue scene");
          setRoot("room");
          break;
        case "leadScientistSceneButton":
          System.out.println("Switching to lead scientist scene");
          setRoot("leadscientist");
          break;
        case "labTechnicianSceneButton":
          System.out.println("Switching to lab technician scene");
          setRoot("labtechnician");
          break;
        case "scholarSceneButton":
          System.out.println("Switching to scholar scene");
          setRoot("scholar");
          break;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    stage.setScene(scene);
    stage.show();
  }

  public static boolean gameResult(String suspectName) {
    if (suspectName.equals("Scholar")) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "room" scene.
   *
   * @param stage the primary stage of the application
   * @throws IOException if the "src/main/resources/fxml/room.fxml" file is not found
   */
  @Override
  public void start(final Stage stage) throws IOException {
    Parent root = loadFxml("home");
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
    stage.setOnCloseRequest(event -> handleWindowClose(event));
    root.requestFocus();
  }

  public static void actuallyStart() throws IOException {
    Parent clueRoot = loadFxml("room");

    StackPane stackPane = new StackPane(scene.getRoot(), clueRoot);

    clueRoot.setOpacity(0);

    // Fade homescreen out
    FadeTransition fadeHome = new FadeTransition(Duration.millis(300), scene.getRoot());
    scene.setRoot(stackPane);
    stackPane.setStyle("-fx-background-color: #c5c7e1;");
    fadeHome.setFromValue(1);
    fadeHome.setToValue(0);

    fadeHome.setOnFinished(
        event -> {
          stackPane.getChildren().remove(0);

          // Fade clue room in
          FadeTransition fadeClue = new FadeTransition(Duration.millis(300), clueRoot);
          fadeClue.setFromValue(0);
          fadeClue.setToValue(1);
          fadeClue.play();
        });

    fadeHome.play();
  }

  private void handleWindowClose(WindowEvent event) {
    FreeTextToSpeech.deallocateSynthesizer();
  }
}
