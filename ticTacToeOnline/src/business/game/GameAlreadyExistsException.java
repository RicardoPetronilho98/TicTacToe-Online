package business.game;

public class GameAlreadyExistsException extends Exception {

    public GameAlreadyExistsException() {
        super();
    }

    public GameAlreadyExistsException(String msg) {
        super(msg);
    }
}