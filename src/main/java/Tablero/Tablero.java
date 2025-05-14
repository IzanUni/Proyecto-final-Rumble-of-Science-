package Tablero;
import Unidades.Unidad;
public class Tablero {
    private Casilla[][] casillas;
    private int filas;
    private int columnas;

    public Tablero(int filas, int columnas){
        this.casillas = new Casilla[filas][columnas];
        this.filas = filas;
        this.columnas = columnas;
        inicializarTablero();
    }

    private void inicializarTablero(){
        for(int i=0; i<filas;i++){
            for(int j=0; j<columnas; j++){
                int defensa = -1 + (int) (Math.random() * 3);
                int ataque = -1 + (int) (Math.random() * 3);
                casillas[i][j] = new Casilla(defensa, ataque);
            }
        }
    }

    public Casilla getCasilla(int fila, int columna){
        return casillas[fila][columna];
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public boolean colocarUnidad(Unidad unidad, int fila, int columna){
        Casilla c = getCasilla(fila, columna);
        if(!c.estaOcupada()){
            c.colocarUnidad(unidad);
            unidad.setPosicion(fila, columna);
            return true;
        }
        return false;
    }


}
