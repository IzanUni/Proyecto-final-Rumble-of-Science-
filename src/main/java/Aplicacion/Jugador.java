package Aplicacion;

import Material.ListaSE;

public class Jugador {
    private String nombre;
    private boolean esHumano;
    private ListaSE<Unidad> unidades = new ListaSE<>();

    public Jugador(String nombre, boolean esHumano){
        this.nombre = nombre;
        this.esHumano = esHumano;
    }

    public String getNombre() {return nombre;}
    public boolean esHumano() {return esHumano;}
    public ListaSE<Unidad> getUnidades() {return unidades;}

    public void addUnidad(Unidad u){
        unidades.add(u);
    }
}
