package Unidades;

public class Matematico extends Unidad{

    public Matematico(boolean esJugadorHumano){
        super("Matematico", 10,1, 2, 1,1, 2, 5);
    }
    @Override
    public String getTipo() {
        return "Matemático";
    }

    @Override
    public boolean puedeMoverA(int nuevaFila, int nuevaColumna) {
        int dx = Math.abs(nuevaFila - getFila());
        int dy = Math.abs(nuevaColumna - getColumna());
        return ((dx == 1 && dy == 0) || (dx == 0 && dy == 1));
    }
}
