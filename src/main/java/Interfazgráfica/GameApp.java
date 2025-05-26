package Interfazgráfica;

import Aplicacion.Faccion;
import Aplicacion.Jugador;
import Aplicacion.Partida;
import Aplicacion.Posicion;
import Excepciones.InvalidMoveException;
import Excepciones.invalidAttackException;
import IA.IAController;
import Logging.LogEdge;
import Material.ListaSE;
import Material.IteradorSE;
import Tablero.Tablero;
import Tablero.Casilla;
import Unidades.Unidad;
import Unidades.Ingeniero;
import Unidades.Matematico;
import Unidades.Poeta;
import Unidades.Filosofo;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.awt.*;
import java.util.Random;
import Logging.LogGraph;

public class GameApp extends Application {
    private Tablero tablero;
    private Partida partida;
    private TableroView tableroView;
    private Unidad spawnPendiente;
    private ListaSE<Posicion> spawnPositions = new ListaSE<>();
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
    private VBox sidePanel;
    private IAController iaController;
    private final LogGraph log = new LogGraph();

    @Override
    public void start(Stage primaryStage) {
        spawnInterval = 5;
        tablero = new Tablero(filas,columnas);
        Jugador j1 = new Jugador("Jugador 1",true, Faccion.NUMEROS);
        Jugador j2 = new Jugador ("Jugador 2",false, Faccion.LETRAS);
        partida = new Partida(tablero, j1, j2, spawnInterval);
        partida.iniciarPartida();
        iaController = new IAController(partida, tablero);

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

        moveBtn.setOnAction(e -> activateMoveMode());
        attackBtn.setOnAction(e -> activateAttackMode());
        nextTurnBtn.setOnAction(e -> handleNextTurn());


        HBox actionBox = new HBox(10, moveBtn, attackBtn, nextTurnBtn);
        actionBox.setPadding(new Insets(5,5,5,5));
        sidePanel = new VBox(10);
        sidePanel.setPadding(new Insets(5));
        sidePanel.getChildren().add(new Label("-- Unidades --"));
        ScrollPane scroll = new ScrollPane(sidePanel);
        scroll.setFitToWidth(true);
        scroll.setPrefWidth(180);
        BorderPane root = new BorderPane();
        root.setCenter(tableroView);
        root.setBottom(actionBox);
        root.setRight(scroll);

        Scene scene = new Scene(root,900,700);
        primaryStage.setTitle("Rumble of Science");
        primaryStage.setScene(scene);
        primaryStage.show();
        updateSidePanel();
        handleNextTurn();
    }
    private void updateSidePanel(){
        sidePanel.getChildren().clear();
        sidePanel.getChildren().add(new Label("-- Jugador 1 (Números) --"));
        nombreUnidades(partida.getJugador1(), sidePanel);
        sidePanel.getChildren().add(new Label("-- Jugador 2 (Letras) --"));
        nombreUnidades(partida.getJugador2(), sidePanel);
    }
    private void nombreUnidades(Jugador j, VBox panel){
        IteradorSE<Unidad> it = j.getUnidades().getIterador();
        while (it.hasNext()) {
            Unidad u = it.next();
            Label lbl = new Label(u.getTipo() + " @ ("+u.getFila()+","+u.getColumna()+")");
            panel.getChildren().add(lbl);
        }
    }
    private void handleNextTurn() {
        if (spawnPendiente != null) {
            new Alert(AlertType.WARNING,
                    "Debes colocar primero la unidad generada antes de pasar turno.")
                    .showAndWait();
            return;
        }

        actionDone = false;
        selectedUnit = null;
        actionMode = ActionMode.NONE;
        moveBtn.setDisable(true);
        attackBtn.setDisable(true);
        spawnPositions = new ListaSE<>();
        tableroView.limpiarResaltados();

        partida.nextTurn();
        tablasActualizar();

        Jugador actual = partida.getJugadorActual();
        int total = countUnits();

        if (partida.getTurno() % spawnInterval == 0 && total < 8) {
            spawnPendiente = generarUnidadActual(actual);
            spawnPositions = partida.getPosicionesAdyacentesLibres(actual);


            if (actual.esHumano()) {
                tableroView.resaltar(spawnPositions);
                new Alert(AlertType.INFORMATION,
                        "Spawn: nueva unidad " + spawnPendiente.getTipo() +
                                ". Selecciona casilla resaltada para colocarla.")
                        .showAndWait();
                return;
            } else {
                int cnt = spawnPositions.getNumElementos();
                int idx = rnd.nextInt(cnt);
                IteradorSE<Posicion> it = spawnPositions.getIterador();
                Posicion p = null;
                for (int i = 0; i <= idx; i++) {
                    p = it.next();
                }
                partida.colocarUnidadNueva(actual, spawnPendiente, p);
                log.addEdge(
                        "U" + spawnPendiente.hashCode(), spawnPendiente.getTipo(),
                        "C_" + p.getFila() + "_" + p.getColumna(),
                        "Casilla(" + p.getFila() + "," + p.getColumna() + ")",
                        "spawn(turn " + partida.getTurno() + ")"
                );
                spawnPendiente = null;
                tablasActualizar();
                return;
            }
        }

        if (!actual.esHumano()) {
            iaController.ejecutarTurnoIA();
            tablasActualizar();
            partida.nextTurn();
            tablasActualizar();
        }
    }
    private Unidad generarUnidadActual(Jugador j) {
        boolean t = rnd.nextBoolean();
        if (j.getFaccion()==Faccion.NUMEROS) return t
                ? new Ingeniero(j.esHumano())
                : new Matematico(j.esHumano());
        else return t
                ? new Poeta(j.esHumano())
                : new Filosofo(j.esHumano());
    }

    private void handleCellClick(int f, int c) {
        if (actionDone && spawnPendiente == null) return;

        Casilla cas = tablero.getCasilla(f, c);
        Jugador actual = partida.getJugadorActual();

        if (spawnPendiente != null) {
            Posicion p = new Posicion(f, c);
            if (contains(spawnPositions, p)
                    && partida.colocarUnidadNueva(actual, spawnPendiente, p)) {
                spawnPendiente = null;
                tablasActualizar();
            }
            return;
        }

        if (cas.estaOcupada()) {
            showStats(cas.getUnidad());
        }

        if (selectedUnit == null
                && cas.estaOcupada()
                && cas.getUnidad().esJugadorHumano() == actual.esHumano()) {
            selectedUnit = cas.getUnidad();
            moveBtn.setDisable(false);
            attackBtn.setDisable(false);
            return;
        }

        if (selectedUnit != null) {
            if (actionMode == ActionMode.ATTACK
                    && cas.estaOcupada()
                    && cas.getUnidad().esJugadorHumano() != selectedUnit.esJugadorHumano()
                    && selectedUnit.puedeAtacarA(f, c)) {
                showStats(cas.getUnidad());
                try {
                    selectedUnit.atacar(tablero, f, c);
                    log.addEdge(
                            "U" + selectedUnit.hashCode(), selectedUnit.getTipo(),
                            "U" + cas.getUnidad().hashCode(), cas.getUnidad().getTipo(),
                            "attack(turn " + partida.getTurno() + ")"
                    );
                    actionDone = true;
                    tablasActualizar();
                } catch (invalidAttackException ex) {
                    new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
                }
                actionDone = true;
                tablasActualizar();
            }
            else if (actionMode == ActionMode.MOVE
                    && !cas.estaOcupada()
                    && selectedUnit.puedeMoverA(f, c)) {
                try {
                    selectedUnit.mover(tablero, f, c);
                    log.addEdge(
                            "U" + selectedUnit.hashCode(), selectedUnit.getTipo(),
                            "C_" + f + "_" + c,
                            "Casilla(" + f + "," + c + ")",
                            "move(turn " + partida.getTurno() + ")"
                    );
                    actionDone = true;
                    tablasActualizar();
                } catch (InvalidMoveException ex) {
                    new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
                }
                actionDone = true;
                tablasActualizar();
            }
            selectedUnit = null;
            actionMode   = ActionMode.NONE;
            moveBtn.setDisable(true);
            attackBtn.setDisable(true);
        }
        tableroView.refrescar();
        updateSidePanel();

        if (partida.finPartida()) {
            Jugador ganador = partida.getGanador();
            String msg = ganador == null
                    ? "¡Empate!"
                    : "¡Victoria para " + ganador.getNombre() + "!";
            new Alert(Alert.AlertType.INFORMATION, msg)
                    .showAndWait();
            moveBtn.setDisable(true);
            attackBtn.setDisable(true);
            nextTurnBtn.setDisable(true);
            showLogWindow();
        }
    }

    private boolean contains(ListaSE<Posicion> list, Posicion p) {
        IteradorSE<Posicion> it = list.getIterador();
        while(it.hasNext()) if (it.next().equals(p)) return true;
        return false;
    }

    private int countUnits() {
        return partida.getJugador1()
                .getUnidades()
                .getNumElementos()
                +   partida.getJugador2()
                .getUnidades()
                .getNumElementos();
    }

    private void tablasActualizar() {
        tableroView.refrescar();
        updateSidePanel();
    }

    private void activateMoveMode() {
        actionMode=ActionMode.MOVE;
        if (selectedUnit!=null) tableroView.resaltar(getMovimientoPosiciones(selectedUnit));
    }

    private void activateAttackMode() {
        actionMode=ActionMode.ATTACK;
        if (selectedUnit!=null) tableroView.resaltar(getAtaquePosiciones(selectedUnit));
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

    private void showLogWindow() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Log de la Partida");

        TextArea area = new TextArea();
        area.setEditable(false);

        StringBuilder sb = new StringBuilder();
        for (LogEdge e : log.getEdges()) {
            sb.append(e.when).append(" \u2014 ")
                    .append(e.from.label).append(" → ").append(e.to.label)
                    .append(" [").append(e.action).append("]\n");
        }
        area.setText(sb.toString());

        VBox root = new VBox();
        root.setPadding(new Insets(10));
        dialog.setScene(new Scene(root, 500, 600));
        dialog.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
