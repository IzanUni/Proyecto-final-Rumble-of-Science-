package Tablero;
import Unidades.Unidad;
public class Casilla {
    private int modificadorDefensa;
    private int modificadorAtaque;
    private Unidad unidad;

    public Casilla(int modificadorDefensa, int modificadorAtaque) {
        this.modificadorDefensa = modificadorDefensa;
        this.modificadorAtaque = modificadorAtaque;
        this.unidad = null;
    }


    public int getModificadorDefensa() {
        return modificadorDefensa;
    }

    public int getModificadorAtaque() {
        return modificadorAtaque;
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
