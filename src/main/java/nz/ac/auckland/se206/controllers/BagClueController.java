package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;

/** Bag Clue Controller class */
public class BagClueController extends ClueController {

  @FXML private ImageView bagzipper;
  @FXML private Rectangle rectZipper;
  @FXML private Rectangle clickInto;
  @FXML private ImageView zoomedBagStuff;
  @FXML private ImageView sparkles;

  private double offsetY = 0;

  /** Initialises the bag clue controller as soon as the clue is clicked. */
  @FXML
  @Override
  protected void initialize() {
    super.initialize();

    zoomedBagStuff.setVisible(false);

    // clicking into not available until dragged
    clickInto.setVisible(false);
    sparkles.setVisible(false);

    // pair zipper with small rectangle which handles drag

    rectZipper.setOnMousePressed(
        event -> {
          offsetY = event.getSceneY() - bagzipper.getLayoutY();
        });

    // update position on drag
    rectZipper.setOnMouseDragged(
        event -> {
          double newY = event.getSceneY() - offsetY;

          // manually determined positions
          double upperBoundary = 0;
          double lowerBoundary = 170;

          // constrain motion to the boundaries
          if (newY < upperBoundary) {
            newY = upperBoundary;
          } else if (newY > lowerBoundary) {
            newY = lowerBoundary;
          }

          // set new positions, with rectangle lower than the start of the zipper.
          bagzipper.setLayoutY(newY);
          rectZipper.setLayoutY(newY + 230);
          // System.out.println("newY: " + newY);

          if (newY <= 60) {
            clickInto.setVisible(true);
            sparkles.setVisible(true);
          } else {
            clickInto.setVisible(false);
            sparkles.setVisible(false);
          }
        });
  }

  /**
   * Opens the bag and displays the contents.
   *
   * @throws IOException
   */
  @FXML
  private void onOpenBag() throws IOException {
    zoomedBagStuff.setVisible(true);
  }

  /** Hides bag contents. */
  @FXML
  private void closeBagStuff() {
    zoomedBagStuff.setVisible(false);
  }

  /**
   * Exits the locker clue.
   *
   * @throws IOException
   */
  @FXML
  private void onExitClue() throws IOException {
    App.setRoot("room");
    System.out.println("exit locker clue");
  }
}
