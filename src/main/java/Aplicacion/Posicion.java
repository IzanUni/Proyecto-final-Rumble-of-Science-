package Aplicacion;


public class Posicion {
    private int fila;
    private int columna;

    public Posicion(int fila, int columna){
        this.fila = fila;
        this.columna = columna;
    }

    public int getFila() {return this.fila;}
    public int getColumna() {return this.columna;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Posicion)) return false;
        Posicion p = (Posicion) o;
        return p.fila == fila && p.columna == columna;
    }

    @Override
    public int hashCode() {
        return 31 + fila + columna;
    }
}
