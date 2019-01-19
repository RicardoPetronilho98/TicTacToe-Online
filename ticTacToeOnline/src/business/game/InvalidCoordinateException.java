package business.game;

public class InvalidCoordinateException extends Exception {

    public InvalidCoordinateException() {
        super();
    }

    public InvalidCoordinateException(String msg) {
        super(msg);
    }
}
