package Unidades;

public class Ingeniero extends Unidad{
    public Ingeniero(boolean esJugadorHumano){
        super("Ingeniero", 5, 2, 1, 2,1, 1, 5);
    }
    @Override
    public String getTipo() {
        return "Ingeniero";
    }

    @Override
    public boolean puedeMoverA(int nuevaFila, int nuevaColumna) {
        int dFila = Math.abs(nuevaFila - getFila());
        int dCol = Math.abs(nuevaColumna - getColumna());
        return dFila == dCol && dFila <= getRangomovimiento();
    }
}
