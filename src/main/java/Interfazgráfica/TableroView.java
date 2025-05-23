package Interfazgráfica;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import Tablero.Tablero;
import Tablero.Casilla;
public class TableroView extends GridPane{
    public TableroView(Tablero tablero) {
        setHgap(2);
        setVgap(2);
        setAlignment(Pos.CENTER);

        for (int i = 0; i < tablero.getFilas(); i++) {
            for (int j = 0; j < tablero.getColumnas(); j++) {
                Casilla casilla = tablero.getCasilla(i, j);
                Button boton = new Button();
                boton.setPrefSize(50, 50);

                // Color o texto en función del modificador o coste
                String texto =  "D" + casilla.getModificadorDefensa();
                boton.setText(texto);

                if (casilla.getModificadorDefensa() > 0) {
                    boton.setStyle("-fx-background-color: lightgreen;");
                } else if (casilla.getModificadorDefensa() < 0) {
                    boton.setStyle("-fx-background-color: lightcoral;");
                } else {
                    boton.setStyle("-fx-background-color: lightgray;");
                }

                add(boton, j, i);
            }
        }
    }
}
