package oop;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
public class Ülesanne6 extends Application{
    @Override
    public void start(Stage peaLava) throws Exception {
        Group juur = new Group();
        GridPane piir = new GridPane();
        TextField küsimus = new TextField();
        küsimus.setEditable(false);
        küsimus.setText("Mis on minu nimi?");
        piir.add(küsimus, 0, 0);
        TextField vastus1 = new TextField();
        vastus1.setEditable(false);
        vastus1.setText("Marl-Kagnus Loikaja");
        vastus1.setOnMouseClicked(event -> vastus1.setText("Vale vastus!"));
        piir.add(vastus1, 0, 1);
        TextField vastus2 = new TextField();
        vastus2.setEditable(false);
        vastus2.setText("Karl-Magnus Laikoja");
        vastus2.setOnMouseClicked(event -> vastus2.setText("Õige vastus!"));
        piir.add(vastus2, 0, 2);
        TextField vastus3 = new TextField();
        vastus3.setEditable(false);
        vastus3.setText("Magnus-Karl Laikoja");
        vastus3.setOnMouseClicked(event -> vastus3.setText("Vale vastus!"));
        piir.add(vastus3, 0, 3);
        juur.getChildren().add(piir);
        Scene stseen1 = new Scene(juur, 535, 535, Color.SNOW);
        peaLava.setTitle("Lava");
        peaLava.setScene(stseen1);
        peaLava.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
