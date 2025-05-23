package Aplicacion;

public class Posicion {
    private int fila;
    private int columna;

    public Posicion(int fila, int columna){
        this.fila = fila;
        this.columna = columna;
    }

    public boolean mismaPosicion(Posicion otra){
        if(otra == null) return false;
        return this.fila == otra.fila && this.columna == otra.columna;
    }

    public int getFila() {return this.fila;}
    public int getColumna() {return this.columna;}

}
