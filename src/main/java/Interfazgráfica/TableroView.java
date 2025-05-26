package Interfazgráfica;
import Aplicacion.Posicion;
import Material.IteradorSE;
import Unidades.Unidad;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import Tablero.Tablero;
import Tablero.Casilla;
import Material.ListaSE;

import java.util.function.BiConsumer;

public class TableroView extends GridPane {
    private CellClickListener cellClickListener;
    private final Tablero tablero;
    private final Button[][] buttons;

    public TableroView(Tablero tablero) {
        this.tablero = tablero;
        int filas = tablero.getFilas();
        int columnas = tablero.getColumnas();
        buttons = new Button[filas][columnas];
        setHgap(2);
        setVgap(2);
        setAlignment(Pos.CENTER);

        for (int i = 0; i < tablero.getFilas(); i++) {
            for (int j = 0; j < tablero.getColumnas(); j++) {
                Button b = new Button();
                b.setPrefSize(50, 50);
                final int fi = i;
                final int cj = j;
                b.setOnAction(e -> {
                    if (cellClickListener != null)
                        cellClickListener.onCellClick(fi,cj);
                });
                buttons[i][j] = b;
                add(b, j, i);
            }
        }
        refrescar();
    }

    public void setCellClickListener(CellClickListener listener) {
        this.cellClickListener = listener;
    }



    public void refrescar() {
        for (int i = 0; i < tablero.getFilas(); i++) {
            for (int j = 0; j < tablero.getColumnas(); j++) {
                Casilla cas = tablero.getCasilla(i, j);
                Button b = buttons[i][j];

                if (cas.estaOcupada()) {
                    Unidad u = cas.getUnidad();
                    b.setText(u.getTipo().substring(0, 2));
                } else {
                    b.setText("");
                }
                if (cas.getModificadorDefensa() > 0) {
                    b.setStyle("-fx-background-color: lightgreen;");
                } else if (cas.getModificadorDefensa() < 0) {
                    b.setStyle("-fx-background-color: lightcoral;");
                } else {
                    b.setStyle("-fx-background-color: lightgray;");
                }
            }
        }
    }

    public void resaltar(ListaSE<Posicion> posList) {
        IteradorSE<Posicion> it = posList.getIterador();
        while (it.hasNext()) {
            Posicion p = it.next();
            Button b = buttons[p.getFila()][p.getColumna()];
            b.setStyle(b.getStyle() + "-fx-border-color: blue; -fx-border-width: 3;");
        }
    }
    public void limpiarResaltados(){
        refrescar();
    }
}