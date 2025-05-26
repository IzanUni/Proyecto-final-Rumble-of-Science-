package Aplicacion;
import Excepciones.SameFaccion;
import Material.IteradorSE;
import Tablero.Tablero;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import Tablero.Tablero;
import Unidades.*;
import Material.ListaSE;

public class Partida {
    private final Tablero tablero;
    private final Jugador jugador1, jugador2;
    private Jugador jugadoractual;
    private int turno = 0;
    private final int spawnInterval;
    private final Random random = new Random();

    public Partida(Tablero tablero, Jugador j1, Jugador j2, int spawnInterval) {
        if (j1.getFaccion() == j2.getFaccion()) {
            throw new SameFaccion("Los jugadores deben defender facciones opuestas");
        }
        this.tablero = tablero;
        this.jugador1 = j1;
        this.jugador2 = j2;
        this.spawnInterval = spawnInterval;
    }

    public void iniciarPartida() {

    }

    private void colocarIniciales(Jugador jugador, Posicion[] esquinas) {
        for (Posicion pos : esquinas) {
            Unidad u = generarUnidadAleatoria(jugador);
            tablero.colocarUnidad(u, pos.getFila(), pos.getColumna());
            u.setPosicion(pos.getFila(), pos.getColumna());
            jugador.addUnidad(u);
        }
    }

    private Unidad generarUnidadAleatoria(Jugador jugador) {
        Random rnd = new Random();
        boolean tipo = rnd.nextBoolean();
        if (jugador.getFaccion() == Faccion.NUMEROS) {
            if (tipo) {
                return new Ingeniero(jugador.esHumano());
            } else {
                return new Matematico(jugador.esHumano());
            }
        } else {
            if (tipo) {
                return new Poeta(jugador.esHumano());
            } else {
                return new Filosofo(jugador.esHumano());
            }
        }


    }

    public void nextTurn() {
        turno++;
        jugadoractual = (turno % 2 == 1) ? jugador2 : jugador1;
        if (turno % spawnInterval == 0) {
            Unidad nueva = generarUnidadAleatoria(jugadoractual);
            ListaSE<Posicion> libres = getPosicionesAdyacentesLibres(jugadoractual);
        }

    }

    private ListaSE<Posicion> getPosicionesAdyacentesLibres(Jugador jugador) {
        ListaSE<Posicion> libres = new ListaSE<>();

        // Iteramos sobre la lista de unidades con su iterador
        IteradorSE<Unidad> itUnidades = jugador.getUnidades().getIterador();
        while (itUnidades.hasNext()) {
            Unidad u = itUnidades.next();
            int f = u.getFila();
            int c = u.getColumna();

            // Arriba
            if (f > 0 && !tablero.getCasilla(f - 1, c).estaOcupada()) {
                Posicion pos = new Posicion(f - 1, c);
                if (!contiene(libres, pos)) {
                    libres.add(pos);
                }
            }
            // Abajo
            if (f < tablero.getFilas() - 1 && !tablero.getCasilla(f + 1, c).estaOcupada()) {
                Posicion pos = new Posicion(f + 1, c);
                if (!contiene(libres, pos)) {
                    libres.add(pos);
                }
            }
            // Izquierda
            if (c > 0 && !tablero.getCasilla(f, c - 1).estaOcupada()) {
                Posicion pos = new Posicion(f, c - 1);
                if (!contiene(libres, pos)) {
                    libres.add(pos);
                }
            }
            // Derecha
            if (c < tablero.getColumnas() - 1 && !tablero.getCasilla(f, c + 1).estaOcupada()) {
                Posicion pos = new Posicion(f, c + 1);
                if (!contiene(libres, pos)) {
                    libres.add(pos);
                }
            }
        }

        return libres;
    }

    private boolean contiene(ListaSE<Posicion> lista, Posicion p) {
        IteradorSE<Posicion> it = lista.getIterador();
        while (it.hasNext()) {
            Posicion otra = it.next();
            if (otra.getFila() == p.getFila() && otra.getColumna() == p.getColumna()) {
                return true;
            }
        }
        return false;
    }

}
