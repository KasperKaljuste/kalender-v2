package oop;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
public class Ülesanne4 extends Application{
    @Override
    public void start(Stage peaLava) throws Exception {
        Group juur = new Group();
        Button nupp = new Button();
        juur.getChildren().add(nupp);
        Scene stseen1 = new Scene(juur, 535, 535, Color.SNOW);
        nupp.setOnMouseEntered(event -> nupp.relocate(stseen1.getWidth()*Math.random(), stseen1.getHeight()*Math.random()));
        peaLava.setTitle("Lava");
        peaLava.setScene(stseen1);
        peaLava.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}