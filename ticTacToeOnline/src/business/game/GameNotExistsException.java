package business.game;

public class GameNotExistsException extends Exception {

    public GameNotExistsException() {
        super();
    }

    public GameNotExistsException(String msg) {
        super(msg);
    }
}
