package Unidades;
import Excepciones.InvalidMoveException;
import Excepciones.invalidAttackException;
import Tablero.Tablero;
import Tablero.Casilla;
public abstract class Unidad {
    protected String nombre;
    protected int hp;
    protected int ataque;
    protected int defensa;
    protected int rangomovimiento;
    protected int rangoataque;

    protected int fila;
    protected int columna;

    protected boolean esJugadorHumano;

    public Unidad(String nombre, int hp, int ataque, int defensa, int rangoataque,int rangomovimiento, int fila, int columna, boolean esJugadorHumano) {
        this.nombre = nombre;
        this.hp = hp;
        this.ataque = ataque;
        this.defensa = defensa;
        this.rangoataque = rangoataque;
        this.fila = fila;
        this.columna = columna;
        this.rangomovimiento = rangomovimiento;
        this.esJugadorHumano = esJugadorHumano;
    }
    public int getAtaque() {
        return ataque;
    }
    public int getDefensa() {
        return defensa;
    }

    public abstract String getTipo();

    public int getHp(){
        return hp;
    }

    public void recibirDamage(int damage){
        if (damage > 0){
            hp -= damage;
        }
    }

    public void recibirHp(int heal){
        if (heal > 0){
            hp += heal;
        }
    }

    public boolean estaViva() {
        return hp > 0;
    }

    public int getRangomovimiento(){return rangomovimiento;}
    public int getRangoataque(){return rangoataque;}

    public int getFila(){
        return fila;
    }


    public int getColumna(){
        return columna;
    }

    public void setPosicion(int fila, int columna){
        this.fila = fila;
        this.columna = columna;
    }

    public boolean esJugadorHumano(){
        return esJugadorHumano;
    }

    protected int calcularDistancia(int nuevaFila, int nuevaColumna) {
        return Math.abs(nuevaFila - this.fila) + Math.abs(nuevaColumna - this.columna);
    }

    public boolean mover(Tablero tablero, int nuevaFila, int nuevaColumna) {
        if (nuevaFila < 0 || nuevaFila >= tablero.getFilas()
                || nuevaColumna < 0 || nuevaColumna >= tablero.getColumnas()) {
            throw new InvalidMoveException("Posición fuera del tablero: " + nuevaFila + "," + nuevaColumna);
        }
        if (!puedeMoverA(nuevaFila, nuevaColumna))  throw new InvalidMoveException("Movimiento no permitido a (" + nuevaFila + "," + nuevaColumna + ")");;

        Casilla destino = tablero.getCasilla(nuevaFila, nuevaColumna);
        if (destino.estaOcupada()) { throw new InvalidMoveException("Casilla ocupada en (" + nuevaFila + "," + nuevaColumna + ")");}
        int distancia = calcularDistancia(nuevaFila, nuevaColumna);
        if(distancia > rangomovimiento){ throw new InvalidMoveException("Movimiento no permitido a (" + nuevaFila + "," + nuevaColumna + ")");}
        tablero.getCasilla(this.fila, this.columna).eliminarUnidad();
        destino.colocarUnidad(this);
        setPosicion(nuevaFila, nuevaColumna);
        return true;
    }
    public abstract boolean puedeMoverA(int nuevaFila, int nuevaColumna);

    public boolean atacar(Tablero tablero, int objetivoFila, int objetivoColumna) {
        if (objetivoFila < 0 || objetivoFila >= tablero.getFilas()
                || objetivoColumna < 0 || objetivoColumna >= tablero.getColumnas()) {
            throw new invalidAttackException("Objetivo fuera del tablero: " + objetivoFila + "," + objetivoColumna);
        }
        if (!puedeAtacarA(objetivoFila, objetivoColumna))throw new invalidAttackException("Fuera de rango de ataque: (" + objetivoFila + "," + objetivoColumna + ")");;

        Casilla objetivo = tablero.getCasilla(objetivoFila, objetivoColumna);
        if (!objetivo.estaOcupada()) throw new invalidAttackException("No hay objetivo en la casilla (" + objetivoFila + "," + objetivoColumna + ")");;

        Unidad enemigo = objetivo.getUnidad();
        if (enemigo.esJugadorHumano == this.esJugadorHumano) throw new invalidAttackException("No puedes atacar a un aliado.");;

        int damage = Math.max(0, this.ataque - enemigo.defensa);
        enemigo.recibirDamage(damage);

        if (!enemigo.estaViva()) {
            objetivo.eliminarUnidad();
        }

        return true;
    }

    public boolean puedeAtacarA(int objetivoFila, int objetivoColumna) {
            int dx = Math.abs(objetivoFila     - this.fila);
            int dy = Math.abs(objetivoColumna  - this.columna);
            return Math.max(dx, dy) <= this.rangoataque;
    }
}
