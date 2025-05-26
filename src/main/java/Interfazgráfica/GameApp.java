package Interfazgráfica;

import Aplicacion.Faccion;
import Aplicacion.Jugador;
import Aplicacion.Partida;
import Aplicacion.Posicion;
import Material.ListaSE;
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
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import java.awt.*;
import java.util.Random;
import Aplicacion.Partida;
import Tablero.Casilla;

public class GameApp extends Application {
    private Tablero tablero;
    private Partida partida;
    private TableroView tableroView;
    private Unidad spawnPendiente;
    private Unidad selectedUnit;
    private int spawnInterval;
    private final int filas = 10;
    private final int columnas = 10;
    private final Random rnd = new Random();

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
        Button nextTurnBtn = new Button("Siguiente turno");
        nextTurnBtn.setOnAction(e->handleNextTurn());
        HBox controls = new HBox(10, nextTurnBtn);
        BorderPane root = new BorderPane();
        root.setCenter(tableroView);
        root.setBottom(controls);

        Scene scene = new Scene(root,600,600);
        primaryStage.setTitle("Rumble of Science");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    private void handleNextTurn() {
        selectedUnit = null;
        spawnPendiente = null;
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
            ListaSE<Posicion> libres = partida.getPosicionesAdyacentesLibres(actual);
            tableroView.resaltar(libres);
        }
        else{
            spawnPendiente = null;
        }
        tableroView.refrescar();
    }

    private void handleCellClick(int fila, int columna) {
        if(spawnPendiente != null){
            Posicion pos = new Posicion(fila, columna);
            boolean ok = partida.colocarUnidadNueva(partida.getJugadorActual(), spawnPendiente, pos);
            if(ok){
                spawnPendiente = null;
                tableroView.limpiarResaltados();
                tableroView.refrescar();
            }
            return;
        }
        Casilla cas = tablero.getCasilla(fila, columna);
        if(selectedUnit == null){
            if (cas.estaOcupada() && cas.getUnidad().esJugadorHumano() == partida.getJugadorActual().esHumano()) {
                selectedUnit = cas.getUnidad();
                ListaSE<Posicion> moves = getMovimientoPosiciones(selectedUnit);
                ListaSE<Posicion> attacks = getAtaquePosiciones(selectedUnit);
                tableroView.resaltar(moves);
                tableroView.resaltar(attacks);
            }
        }
        else{
            boolean moved = selectedUnit.mover(tablero, fila, columna);
            if(!moved){
                selectedUnit.atacar(tablero, fila, columna);
            }
            selectedUnit = null;
            tableroView.limpiarResaltados();
            tableroView.refrescar();
        }
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

    public static void main(String[] args) {
        launch(args);
    }
}
