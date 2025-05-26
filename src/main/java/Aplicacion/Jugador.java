package Aplicacion;

import Material.ListaSE;
import Unidades.Unidad;

public class Jugador {
    private String nombre;
    private boolean esHumano;
    private final Faccion faccion;
    private ListaSE<Unidad> unidades = new ListaSE<>();

    public Jugador(String nombre, boolean esHumano, Faccion faccion){
        this.nombre = nombre;
        this.esHumano = esHumano;
        this.faccion = faccion;
    }

    public String getNombre() {return nombre;}
    public boolean esHumano() {return esHumano;}
    public Faccion getFaccion() {return faccion;}
    public ListaSE<Unidad> getUnidades() {return unidades;}

    public void addUnidad(Unidad u){
        unidades.add(u);
    }
}
