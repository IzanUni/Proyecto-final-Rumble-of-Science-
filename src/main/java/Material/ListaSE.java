package Material;
public class ListaSE<T> implements Lista<T>{
        ElementoSE<T> inicio;

    public ListaSE() {
            this.inicio = null;
        }

        @Override
        public boolean add(T elemento) {
            ElementoSE<T> nuevo = new ElementoSE<>(elemento);
            if (inicio == null) {
                inicio = nuevo;
                inicio.siguiente = null;
                return true;
            } else {
                ElementoSE<T> temporal = inicio;
                while (temporal.siguiente != null) {
                    temporal = temporal.siguiente;
                }
                temporal.siguiente = nuevo;
                nuevo.siguiente = null;
            }
            return true;
        }

        @Override
        public boolean delete(T elemento) {
            if (inicio == null) {
                return false;
            }
            if (inicio.dato == elemento) {
                inicio = inicio.siguiente;
            }
            ElementoSE<T> actual = inicio;
            while (actual.siguiente != null && !actual.siguiente.dato.equals(elemento)) {
                actual = actual.siguiente;
            }
            if (actual.siguiente != null) {
                actual.siguiente = actual.siguiente.siguiente;
                return true;
            }
            return false;
        }

        @Override
        public IteradorSE<T> getIterador() {
            return new IteradorSE<>(this);
        }

        @Override
        public int getNumElementos() {
            int size = 0;
            ElementoSE<T> temporal = inicio;
            while (temporal != null) {
                temporal = temporal.siguiente;
                size++;
            }
            return size;
        }
}
