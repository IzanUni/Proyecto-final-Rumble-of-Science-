package Excepciones;

public class SameFaccion extends RuntimeException {
    public SameFaccion(String message) {
        super(message);
    }
}
