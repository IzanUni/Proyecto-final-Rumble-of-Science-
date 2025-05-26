package Aplicacion;
import Excepciones.SameFaccion;
import Material.IteradorSE;
import Tablero.Tablero;

import java.util.Random;

import Unidades.*;
import Material.ListaSE;

public class Partida {
    private final Tablero tablero;
    private final Jugador jugador1, jugador2;
    private Jugador jugadorActual;
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
        colocarIniciales(jugador1,
                new Posicion(0, 0),
                new Posicion(tablero.getFilas() - 1, 0)
        );
        colocarIniciales(jugador2,
                new Posicion(0, tablero.getColumnas() - 1),
                new Posicion(tablero.getFilas() - 1, tablero.getColumnas() - 1)
        );
        jugadorActual = jugador1;
    }

    private void colocarIniciales(Jugador jugador, Posicion... esquinas) {
        for (Posicion pos : esquinas) {
            Unidad u = generarUnidadAleatoria(jugador);
            tablero.colocarUnidad(u, pos.getFila(), pos.getColumna());
            u.setPosicion(pos.getFila(), pos.getColumna());
            jugador.addUnidad(u);
        }
    }

    public Jugador getJugador1(){
        return this.jugador1;
    }
    public Jugador getJugador2(){
        return this.jugador2;
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
        jugadorActual = (turno % 2 == 1) ? jugador2 : jugador1;
        if (turno % spawnInterval == 0) {
            Unidad nueva = generarUnidadAleatoria(jugadorActual);
            ListaSE<Posicion> libres = getPosicionesAdyacentesLibres(jugadorActual);
        }

    }
    public int getTurno() {
        return turno;
    }

    public Jugador getJugadorActual() {
        return jugadorActual;
    }

    public boolean colocarUnidadNueva(Jugador jugador, Unidad unidad, Posicion pos) {
        ListaSE<Posicion> libres = getPosicionesAdyacentesLibres(jugador);
        boolean valido = false;
        IteradorSE<Posicion> it = libres.getIterador();
        while (it.hasNext()) {
            Posicion p = it.next();
            if (p.getFila() == pos.getFila() && p.getColumna() == pos.getColumna()) {
                valido = true;
                break;
            }
        }
        if (!valido) return false;

        tablero.colocarUnidad(unidad, pos.getFila(), pos.getColumna());
        unidad.setPosicion(pos.getFila(), pos.getColumna());
        jugador.addUnidad(unidad);
        return true;
    }
    public ListaSE<Posicion> getPosicionesAdyacentesLibres(Jugador jugador) {
        ListaSE<Posicion> libres = new ListaSE<>();


        IteradorSE<Unidad> itUnidades = jugador.getUnidades().getIterador();
        while (itUnidades.hasNext()) {
            Unidad u = itUnidades.next();
            int f = u.getFila();
            int c = u.getColumna();

            if (f > 0 && !tablero.getCasilla(f - 1, c).estaOcupada()) {
                Posicion pos = new Posicion(f - 1, c);
                if (!contiene(libres, pos)) {
                    libres.add(pos);
                }
            }
            if (f < tablero.getFilas() - 1 && !tablero.getCasilla(f + 1, c).estaOcupada()) {
                Posicion pos = new Posicion(f + 1, c);
                if (!contiene(libres, pos)) {
                    libres.add(pos);
                }
            }
            if (c > 0 && !tablero.getCasilla(f, c - 1).estaOcupada()) {
                Posicion pos = new Posicion(f, c - 1);
                if (!contiene(libres, pos)) {
                    libres.add(pos);
                }
            }
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

    public boolean finPartida() {
        return jugador1.getUnidades().getNumElementos() == 0
                || jugador2.getUnidades().getNumElementos() == 0;
    }

    public Jugador getGanador() {
        int n1 = jugador1.getUnidades().getNumElementos();
        int n2 = jugador2.getUnidades().getNumElementos();
        if (n1 == 0 && n2 == 0) return null;
        if (n1 == 0)        return jugador2;
        return null;
    }


}
