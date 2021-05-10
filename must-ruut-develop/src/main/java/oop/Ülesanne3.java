package oop;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Ülesanne3 extends Application {

  @Override
  public void start(Stage peaLava) throws Exception {
    Group juur = new Group();
    Rectangle ristkulik1 = new Rectangle(50, 50, 435, 435);
    ristkulik1.setOnMouseEntered(event -> System.out.println("Hiir läks ristküliku peale."));
    ristkulik1.setOnMouseClicked(event -> ristkulik1.setFill(Color.RED));
    juur.getChildren().add(ristkulik1);
    Scene stseen1 = new Scene(juur, 535, 535, Color.SNOW);
    peaLava.setTitle("Lava");
    peaLava.setScene(stseen1);
    peaLava.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

