package IA;

import Aplicacion.Jugador;
import Aplicacion.Partida;
import Tablero.Tablero;
import Tablero.Casilla;
import Unidades.Unidad;
import Material.IteradorSE;

public class IAController {
    private final Partida partida;
    private final Tablero tablero;

    public IAController(Partida partida, Tablero tablero) {
        this.partida = partida;
        this.tablero = tablero;
    }

    public void ejecutarTurnoIA() {
        Jugador ia = partida.getJugadorActual();
        Jugador rival = partida.getJugadorActual() == partida.getJugador1()
                ? partida.getJugador2() : partida.getJugador1();
        Unidad mejorU = null;
        Unidad mejorTarget = null;
        double mejorDist = Double.MAX_VALUE;
        IteradorSE<Unidad> itU = ia.getUnidades().getIterador();
        while (itU.hasNext()) {
            Unidad u = itU.next();
            IteradorSE<Unidad> itV = rival.getUnidades().getIterador();
            while (itV.hasNext()) {
                Unidad v = itV.next();
                double d = Math.max(Math.abs(u.getFila() - v.getFila()), Math.abs(u.getColumna() - v.getColumna()));
                if (d < mejorDist) {
                    mejorDist = d;
                    mejorU = u;
                    mejorTarget = v;
                }
            }
        }
        if (mejorU == null || mejorTarget == null) return;
        int tf = mejorTarget.getFila(), tc = mejorTarget.getColumna();
        Casilla dest = tablero.getCasilla(tf, tc);

        // Si puede atacar
        if (mejorU.puedeAtacarA(tf, tc)) {
            mejorU.atacar(tablero, tf, tc);
        } else {
            // Mover un paso Chebyshev hacia target
            int df = Integer.signum(tf - mejorU.getFila());
            int dc = Integer.signum(tc - mejorU.getColumna());
            int nf = mejorU.getFila() + df;
            int nc = mejorU.getColumna() + dc;
            if (mejorU.puedeMoverA(nf, nc) && !tablero.getCasilla(nf, nc).estaOcupada()) {
                mejorU.mover(tablero, nf, nc);
            }
        }

    }
}