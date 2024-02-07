package com.heshanthenura.maze;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class MainController implements Initializable {
    @FXML
    private Box cube;

    @FXML
    private Group group;

    @FXML
    private AnchorPane root;

    Logger logger = Logger.getLogger("info");

    PerspectiveCamera camera = new PerspectiveCamera();

    Rectangle floor;

    PointLight light = new PointLight(Color.WHITE);

    Group wallGroup = new Group();

    int row = 100;
    int col = 100;
    int size = 10;
    private double anchorX, anchorY;
    //Keep track of current angle for x and y
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    //We will update these after drag. Using JavaFX property to bind with object
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(() -> {
            root.getChildren().addAll(wallGroup);
            addWalls();

            light.setTranslateX(100);
            light.setTranslateY(100);
            light.setTranslateZ(-300);
            root.getChildren().add(light);

            pan();
        });

    }

    void addWalls() {
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                Box box = new Box(size, size, size);
                box.setMaterial(new PhongMaterial(Color.AQUA));
                box.setTranslateX(c * size);
                box.setTranslateY(r * size);
                if (new Random().nextInt(2) == 0) {
                    wallGroup.getChildren().add(box);
                }
            }
        }
        keepWallsCenter();
    }

    void keepWallsCenter() {
        wallGroup.setTranslateX((root.getWidth() / 2) - (wallGroup.getBoundsInParent().getWidth() / 2) + (size / 2));
        wallGroup.setTranslateY((root.getHeight() / 2) - (wallGroup.getBoundsInParent().getHeight() / 2) + (size / 2));
    }

    void pan() {
        Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);

        // Set the pivot points for rotations to the center of wallGroup
        xRotate.setPivotX(wallGroup.getBoundsInParent().getWidth() / 2);
        xRotate.setPivotY(wallGroup.getBoundsInParent().getHeight() / 2);
        xRotate.setPivotZ(wallGroup.getBoundsInParent().getDepth() / 2);

        yRotate.setPivotX(wallGroup.getBoundsInParent().getWidth() / 2);
        yRotate.setPivotY(wallGroup.getBoundsInParent().getHeight() / 2);
        yRotate.setPivotZ(wallGroup.getBoundsInParent().getDepth() / 2);

        wallGroup.getTransforms().addAll(xRotate, yRotate);
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        root.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        root.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + anchorX - event.getSceneX());
        });
    }



}