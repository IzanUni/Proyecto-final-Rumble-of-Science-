package Excepciones;

public class SameFaccion extends RuntimeException {
    public SameFaccion(String message) {
        //se reiniciara el juego y volveran al menu
        super(message);
    }
}
