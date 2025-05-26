package Interfazgráfica;

import Aplicacion.Faccion;
import Aplicacion.Jugador;
import Aplicacion.Partida;
import Aplicacion.Posicion;
import Material.ListaSE;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import Tablero.Tablero;
import Unidades.Unidad;
import Unidades.Ingeniero;
import Unidades.Matematico;
import Unidades.Poeta;
import Unidades.Filosofo;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import java.awt.*;
import java.util.Random;
import javafx.geometry.Insets;
import Tablero.Casilla;


public class GameApp extends Application {
    private Tablero tablero;
    private Partida partida;
    private TableroView tableroView;
    private Unidad spawnPendiente;
    private Unidad selectedUnit;
    private boolean actionDone;
    private ActionMode actionMode = ActionMode.NONE;
    private int spawnInterval;
    private final int filas = 10;
    private final int columnas = 10;
    private final Random rnd = new Random();
    private Button moveBtn;
    private Button attackBtn;
    private Button nextTurnBtn;
    private enum ActionMode { NONE, MOVE, ATTACK }

    @Override
    public void start(Stage primaryStage) {
        spawnInterval = 5;
        tablero = new Tablero(filas,columnas);
        Jugador j1 = new Jugador("Jugador 1",true, Faccion.NUMEROS);
        Jugador j2 = new Jugador ("Jugador 2",false, Faccion.LETRAS);
        partida = new Partida(tablero, j1, j2, spawnInterval);
        partida.iniciarPartida();
        tableroView = new TableroView(tablero);
        tableroView.setCellClickListener(new CellClickListener() {
            @Override
            public void onCellClick(int fila, int columna) {
                handleCellClick(fila, columna);
            }
        });
        moveBtn = new Button("Mover");
        attackBtn = new Button("Atacar");
        nextTurnBtn = new Button("Siguiente Turno");

        moveBtn.setDisable(true);
        attackBtn.setDisable(true);

        moveBtn.setOnAction(e -> {
            actionMode = ActionMode.MOVE;
            if (selectedUnit != null) {
                ListaSE<Posicion> moves = getMovimientoPosiciones(selectedUnit);
                tableroView.limpiarResaltados();
                tableroView.resaltar(moves);
            }
        });
        attackBtn.setOnAction(e -> {
            actionMode = ActionMode.ATTACK;
            if (selectedUnit != null) {
                ListaSE<Posicion> atks = getAtaquePosiciones(selectedUnit);
                tableroView.limpiarResaltados();
                tableroView.resaltar(atks);
            }
        });
        nextTurnBtn.setOnAction(e->handleNextTurn());

        HBox actionBox = new HBox(10, moveBtn, attackBtn);
        actionBox.setPadding(new Insets(5,5,5,5));
        HBox controls = new HBox(10, actionBox, nextTurnBtn);
        controls.setPadding(new Insets(5,5,5,5));
        BorderPane root = new BorderPane();
        root.setCenter(tableroView);
        root.setBottom(controls);

        Scene scene = new Scene(root,600,700);
        primaryStage.setTitle("Rumble of Science");
        primaryStage.setScene(scene);
        primaryStage.show();
        handleNextTurn();

    }
    private void handleNextTurn() {
        actionDone = false;
        selectedUnit = null;
        spawnPendiente = null;
        actionMode = ActionMode.NONE;
        moveBtn.setDisable(true);
        attackBtn.setDisable(true);
        tableroView.limpiarResaltados();
        partida.nextTurn();

        if(partida.getTurno() % spawnInterval == 0){
            Jugador actual = partida.getJugadorActual();
            boolean tipoUnidad = rnd.nextBoolean();
            if (actual.getFaccion() == Faccion.NUMEROS) {
                spawnPendiente = tipoUnidad
                        ? new Ingeniero(actual.esHumano())
                        : new Matematico(actual.esHumano());
            }
            else{
                spawnPendiente = tipoUnidad
                        ? new Poeta(actual.esHumano())
                        : new Filosofo(actual.esHumano());
            }
            ListaSE<Posicion> libres = getMovimientoPosiciones(spawnPendiente);
            tableroView.refrescar();
        }
        tableroView.refrescar();
    }

    private void handleCellClick(int fila, int columna) {
        if (actionDone && spawnPendiente == null) {
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setHeaderText(null);
            info.setContentText("Ya has realizado tu acción. Pulsa 'Siguiente Turno'.");
            info.showAndWait();
            return;
        }
        if(spawnPendiente != null){
            Posicion pos = new Posicion(fila, columna);
            if (partida.colocarUnidadNueva(partida.getJugadorActual(), spawnPendiente, pos)) {
                spawnPendiente = null;
                actionDone = true;
                tableroView.limpiarResaltados();
                tableroView.refrescar();
            }
            return;
        }
        Casilla cas = tablero.getCasilla(fila, columna);
        Jugador actual = partida.getJugadorActual();
        if(selectedUnit == null){
            if (cas.estaOcupada() && cas.getUnidad().esJugadorHumano() == actual.esHumano()) {
                selectedUnit = cas.getUnidad();
                showStats(selectedUnit);
                ListaSE<Posicion> moves = getMovimientoPosiciones(selectedUnit);
                ListaSE<Posicion> attacks = getAtaquePosiciones(selectedUnit);
                tableroView.resaltar(moves);
                tableroView.resaltar(attacks);
            }
            return;
        }
        if (cas.estaOcupada() && cas.getUnidad().esJugadorHumano() != selectedUnit.esJugadorHumano()) {
            if (selectedUnit.puedeAtacarA(fila, columna)) {
                selectedUnit.atacar(tablero, fila, columna);
                actionDone = true;
            }
        } else {
            if (!cas.estaOcupada() && selectedUnit.puedeMoverA(fila, columna)) {
                selectedUnit.mover(tablero, fila, columna);
                actionDone = true;
            }
        }
        selectedUnit = null;
        tableroView.limpiarResaltados();
        tableroView.refrescar();
    }


    private ListaSE<Posicion> getMovimientoPosiciones(Unidad unidad) {
        ListaSE<Posicion> res = new ListaSE<>();
        for (int i = 0; i < tablero.getFilas(); i++) {
            for (int j = 0; j < tablero.getColumnas(); j++) {
                if (unidad.puedeMoverA(i, j) && !tablero.getCasilla(i, j).estaOcupada()) {
                    res.add(new Posicion(i, j));
                }
            }
        }
        return res;
    }

    private ListaSE<Posicion> getAtaquePosiciones(Unidad unidad) {
        ListaSE<Posicion> res = new ListaSE<>();
        for (int i = 0; i < tablero.getFilas(); i++) {
            for (int j = 0; j < tablero.getColumnas(); j++) {
                if (unidad.puedeAtacarA(i, j)) {
                    Casilla c = tablero.getCasilla(i, j);
                    if (c.estaOcupada() && c.getUnidad().esJugadorHumano() != unidad.esJugadorHumano()) {
                        res.add(new Posicion(i, j));
                    }
                }
            }
        }
        return res;
    }

    private void showStats(Unidad u) {
        Alert stats = new Alert(Alert.AlertType.INFORMATION);
        stats.setTitle("Estadísticas de " + u.getTipo());
        stats.setHeaderText(null);
        stats.setContentText(String.format(
                "HP: %d\nAtaque: %d\nDefensa: %d",
                u.getHp(), u.getAtaque(), u.getDefensa()));
        stats.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
