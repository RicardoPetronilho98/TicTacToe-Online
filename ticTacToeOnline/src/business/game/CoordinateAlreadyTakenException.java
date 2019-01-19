package business.game;

public class CoordinateAlreadyTakenException extends Exception {
    public CoordinateAlreadyTakenException(String msg) {
        super(msg);
    }
}
