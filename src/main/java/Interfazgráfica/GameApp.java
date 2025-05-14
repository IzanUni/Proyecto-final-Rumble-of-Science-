package Interfazgráfica;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import Tablero.Tablero;
import Unidades.Unidad;
import Unidades.Ingeniero;
import Unidades.Matematico;
import Unidades.Poeta;
import Unidades.Filosofo;

public class GameApp extends Application {
    private final int filas = 10;
    private final int columnas = 10;

    @Override
    public void start(Stage primaryStage) {
        Tablero tablero = new Tablero(filas,columnas);
        TableroView tableroView = new TableroView(tablero);

        BorderPane root = new BorderPane();
        root.setCenter(tableroView);

        Scene scene = new Scene(root,600,600);
        primaryStage.setTitle("Rumble of Science");
        primaryStage.setScene(scene);
        primaryStage.show();
        Unidad ing = new Ingeniero(true);
        Unidad fil = new Filosofo(false);
        tablero.colocarUnidad(ing, 0, 0);
        tablero.colocarUnidad(fil, 9, 9);

    }
}
