package Unidades;

public class Poeta extends Unidad{
    public Poeta(boolean esJugadorHumano) {
        super("Poeta", 5, 2, 1, 2,1, 10,5 );
    }

    @Override
    public String getTipo() {
        return "Poeta";
    }

    @Override
    public boolean puedeMoverA(int nuevaFila, int nuevaColumna) {
         int dFila = Math.abs(nuevaFila - getFila());
         int dCol = Math.abs(nuevaColumna - getColumna());
         return dFila == dCol && dFila <= getRangomovimiento();
    }
}



