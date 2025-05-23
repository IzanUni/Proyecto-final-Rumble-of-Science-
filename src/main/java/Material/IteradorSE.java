package Material;

public class IteradorSE<T> implements Iterador<T>{
    ElementoSE<T> actual;

    public IteradorSE(ListaSE<T> milista) {
        this.actual = milista.inicio;
    }
    @Override
    public boolean hasNext() {
        return actual != null;
    }
    @Override
    public T next() {
        if (actual == null) {
            return null;
        }
        T dato = actual.dato;
        actual = actual.siguiente;
        return dato;
    }
    public void delete(){
        if (actual == null) {
            return;
        }if(actual.siguiente == null){
            actual = null;
        }
        actual = actual.siguiente;
    }
}
