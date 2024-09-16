package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;

public class LockerClueController {

  @FXML private ImageView trash;
  @FXML private ImageView takeaway1;
  @FXML private ImageView chinesetakeaway;
  @FXML private ImageView bottle;
  @FXML private ImageView shoes;

  @FXML private Rectangle rectTrash;
  @FXML private Rectangle rectTakeaway1;
  @FXML private Rectangle rectChineseTakeaway;
  @FXML private Rectangle rectBottle;
  @FXML private Rectangle rectShoes;
  @FXML private Rectangle rectBoundary;

  private double xOffset = 0;
  private Rectangle currentRect;
  private ImageView currentImage;

  @FXML
  private void initialize() {
    // pair images with their rectangles which handle drag
    setDrag(trash, rectTrash);
    setDrag(takeaway1, rectTakeaway1);
    setDrag(chinesetakeaway, rectChineseTakeaway);
    setDrag(bottle, rectBottle);
    setDrag(shoes, rectShoes);
  }

  private void setDrag(ImageView image, Rectangle rectSelection) {
    // pressing of mouse
    rectSelection.setOnMousePressed(
        event -> {
          currentRect = rectSelection;
          currentImage = image;
          xOffset = event.getSceneX() - image.getLayoutX();
        });

    // dragging of mouse (horizontally only)
    rectSelection.setOnMouseDragged(
        event -> {
          if (currentImage == image) {
            double newX = event.getSceneX() - xOffset;

            double boundaryX = 200;
            // manually set boundary width actually
            double boundaryWidth = 400;

            // constrain horizontal dragging motion to boundary
            double imageWidth = image.getFitWidth();

            if (newX < boundaryX) newX = boundaryX;
            if (newX > boundaryX + boundaryWidth - imageWidth)
              newX = boundaryX + boundaryWidth - imageWidth;

            image.setLayoutX(newX);
            rectSelection.setLayoutX(newX);
          }
        });
  }

  @FXML
  private void onExitClue() throws IOException {
    App.setRoot("room");
    System.out.println("exit locker clue");
  }
}
