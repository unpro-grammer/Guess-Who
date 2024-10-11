package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;

/** Controller for the locker clue scene. This scene is where the player solves the locker clue. */
public class LockerClueController extends ClueController {

  @FXML private ImageView trash;
  @FXML private ImageView takeaway1;
  @FXML private ImageView chinesetakeaway;
  @FXML private ImageView bottle;
  @FXML private ImageView shoes;
  @FXML private ImageView candy;

  @FXML private ImageView zoomedShoes;

  @FXML private Rectangle rectTrash;
  @FXML private Rectangle rectTakeaway1;
  @FXML private Rectangle rectChineseTakeaway;
  @FXML private Rectangle rectBottle;
  @FXML private Rectangle rectShoes;
  @FXML private Rectangle rectCandy;
  @FXML private Rectangle rectBoundary;

  private double offsetX = 0;
  private ImageView currentImage;

  /** Initialises the locker clue controller as soon as the clue is clicked. */
  @FXML
  @Override
  protected void initialize() {
    super.initialize();
    // pair images with their rectangles which handle drag
    setDrag(trash, rectTrash);
    setDrag(takeaway1, rectTakeaway1);
    setDrag(chinesetakeaway, rectChineseTakeaway);
    setDrag(bottle, rectBottle);
    setDrag(shoes, rectShoes);
    setDrag(candy, rectCandy);
    zoomedShoes.setVisible(false);
  }

  /** Zoom in on the shoe clue. */
  @FXML
  private void zoomOnShoes() {
    zoomedShoes.setVisible(true);
  }

  /** Close the zoomed in shoe clue. */
  @FXML
  private void closeShoes() {
    zoomedShoes.setVisible(false);
  }

  /** Pair each image with corresponding rectangle which handles drag. */
  private void setDrag(ImageView image, Rectangle rectSelection) {
    // pressing of mouse
    rectSelection.setOnMousePressed(
        event -> {
          currentImage = image;
          offsetX = event.getSceneX() - image.getLayoutX();
        });

    // dragging of mouse (horizontally only)
    rectSelection.setOnMouseDragged(
        event -> {
          if (currentImage == image) {
            double newX = event.getSceneX() - offsetX;

            double boundaryX = 200;
            // manually set boundary width actually
            double boundaryWidth = 400;

            // constrain horizontal dragging motion to boundary
            double imageWidth = image.getFitWidth();

            if (newX < boundaryX) {
              newX = boundaryX;
            }
            if (newX > boundaryX + boundaryWidth - imageWidth) {
              newX = boundaryX + boundaryWidth - imageWidth;
            }
            image.setLayoutX(newX);
            rectSelection.setLayoutX(newX);
          }
        });
  }

  /**
   * Exit the locker clue and return to the room.
   *
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onExitClue() throws IOException {
    App.setRoot("room");
    System.out.println("exit locker clue");
  }
}
