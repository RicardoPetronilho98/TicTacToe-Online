package business.game;

public class NotPlayerTurnException extends Exception {

    public NotPlayerTurnException(String msg) {
        super(msg);
    }
}
