package Unidades;

public class Filosofo extends Unidad {
    public Filosofo(boolean esJugadorHumano) {
        super("Filosofo", 10, 1, 2, 1, 1,9, 5,esJugadorHumano);
    }

    @Override
    public String getTipo() {
        return "Filosofo";
    }

    @Override
    public boolean puedeMoverA(int nuevaFila, int nuevaColumna) {
        int dx = Math.abs(nuevaFila - getFila());
        int dy = Math.abs(nuevaColumna - getColumna());
        return ((dx == 1 && dy == 0) || (dx == 0 && dy == 1));
    }
}
