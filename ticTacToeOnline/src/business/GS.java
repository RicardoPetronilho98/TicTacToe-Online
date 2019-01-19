package business;

import business.game.*;

public class GS {

    private GameManager gm;

    public GS() {
        this.gm = new GameManager();
    }

    public String createPlayer(String playerName) {
        return this.gm.createPlayer(playerName);
    }

    public String connect(String playerId) throws GameNotExistsException, GameAlreadyExistsException {
        return this.gm.connect(playerId);
    }

    public void removeGame(String gameId) throws GameNotExistsException {
        this.gm.removeGame(gameId);
    }

    public void play(int l, int c, String playerName, String gameId) throws GameNotExistsException, InvalidCoordinateException, CoordinateAlreadyTakenException, NotPlayerTurnException {
        this.gm.play(l, c, playerName, gameId);
    }

    public char[][] getGameTable(String gameId) throws GameNotExistsException {
        return this.gm.getGameTable(gameId);
    }

    @Override
    public String toString() {
        return "GS{" +
                "gm=" + gm +
                '}';
    }
}