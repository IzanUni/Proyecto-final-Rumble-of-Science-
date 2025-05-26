package Tablero;
import Unidades.Unidad;
public class Casilla {
    private int modificadorDefensa;
    private Unidad unidad;

    public Casilla(int modificadorDefensa, int modificadorAtaque) {
        this.modificadorDefensa = modificadorDefensa;
        this.unidad = null;
    }


    public int getModificadorDefensa() {
        return modificadorDefensa;
    }

    public boolean estaOcupada(){
        return unidad != null;
    }

    public Unidad getUnidad() {
        return unidad;
    }

    public void colocarUnidad(Unidad u){
        this.unidad = u;
    }

    public void eliminarUnidad(){
        this.unidad = null;
    }
}
