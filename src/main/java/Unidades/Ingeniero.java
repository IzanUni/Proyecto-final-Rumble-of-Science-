package Unidades;

import Tablero.Casilla;
import Tablero.Tablero;

public class Ingeniero extends Unidad{
    public Ingeniero(boolean esJugadorHumano){
        super("Ingeniero", 7, 5, 1, 2,1, 1, 5, esJugadorHumano);
    }
    @Override
    public String getTipo() {
        return "Ingeniero";
    }

    @Override
    public boolean puedeMoverA(int nuevaFila, int nuevaColumna) {
        int dx = Math.abs(nuevaFila - getFila());
        int dy = Math.abs(nuevaColumna - getColumna());
        return dx == 1 && dy == 1;
    }

    @Override
    public boolean mover(Tablero tablero, int nuevaFila, int nuevaColumna) {
        if (!puedeMoverA(nuevaFila, nuevaColumna)) return false;
        Casilla destino = tablero.getCasilla(nuevaFila, nuevaColumna);
        if (destino.estaOcupada()) return false;
        tablero.getCasilla(getFila(), getColumna()).eliminarUnidad();
        destino.colocarUnidad(this);
        setPosicion(nuevaFila, nuevaColumna);
        return true;
    }
}
