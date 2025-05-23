package Material;

public class ElementoSE<T> {
    T dato;
    ElementoSE<T> siguiente;
    public ElementoSE(T midato) {
        this.dato = midato;
        this.siguiente = null;
    }
}
