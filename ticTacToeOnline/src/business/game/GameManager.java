package business.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GameManager {

    private Lock lgames;
    private Map<String, Game> games; // jogos neste momento a decorrer
    private long currentId;

    private Lock lconnect;
    private List<String> canConnect; // lista de pessoas em espera

    private Lock lplayers;
    private long playerId;

    public GameManager() {
        this.lgames = new ReentrantLock();
        this.games = new HashMap<>();
        this.currentId = 0;

        this.lconnect = new ReentrantLock();
        this.canConnect = new ArrayList<>();

        this.lplayers = new ReentrantLock();
        this.playerId = 0;
    }

    public String createPlayer(String playerName) {

        this.lplayers.lock();
        String playerId = Long.toString(this.playerId);
        this.playerId++;
        this.lplayers.unlock();

        return playerId;
    }

    public String connect(String playerId) throws GameAlreadyExistsException, GameNotExistsException {

        String res;

        this.lconnect.lock();

        if (this.canConnect.isEmpty()) { // no one is waiting to play

            this.lgames.lock();
            String gameId = Long.toString(this.currentId);
            if (this.games.containsKey(gameId)) {
                this.lconnect.unlock();
                this.lgames.unlock();
                throw new GameAlreadyExistsException(gameId);
            }
            Game game = new Game(gameId);
            game.l.lock();
            this.currentId++;
            this.games.put(game.id, game);
            this.lgames.unlock();

            this.canConnect.add(gameId);
            this.lconnect.unlock();

            game.connect(playerId);
            game.l.unlock();

            res = gameId;
        }

        else { // at least one person is waiting to play

            String gameId = this.canConnect.get(0); // FIFO order
            this.canConnect.remove(0);
            this.lconnect.unlock();

            this.lgames.lock();
            if (! this.games.containsKey(gameId)) {
                this.lgames.unlock();
                throw new GameNotExistsException(gameId);
            }
            Game game = this.games.get(gameId);
            game.l.lock();
            this.lgames.unlock();

            game.connect(playerId);
            game.l.unlock();

            res = gameId;
        }

        return res;
    }

    public void removeGame(String gameId) throws GameNotExistsException {
        this.lgames.lock();
        if (! this.games.containsKey(gameId)) {
            this.lgames.unlock();
            throw new GameNotExistsException(gameId);
        }
        this.games.remove(gameId);
        this.lgames.unlock();
    }

    public void play(int l, int c, String playerId, String gameId) throws GameNotExistsException, InvalidCoordinateException, CoordinateAlreadyTakenException, NotPlayerTurnException {
        int N = Game.N;
        if (l < 0 || l >= N || c < 0 || c >= N ) throw new InvalidCoordinateException("l=" + l + ", c=" + c);

        this.lgames.lock();
        if (! this.games.containsKey(gameId)) {
            this.lgames.unlock();
            throw new GameNotExistsException(gameId);
        }
        Game game = this.games.get(gameId);
        game.l.lock();
        this.lgames.unlock();

        if (! game.players.get(game.currentTurn).equals(playerId) ) {
            game.l.unlock();
            throw new NotPlayerTurnException(playerId);
        }

        if (game.table[l][c] != 'E') {
            game.l.unlock();
            throw new CoordinateAlreadyTakenException("l=" + l + ", c=" + c);
        }

        game.currentTurn++;
        if (game.currentTurn > 1) game.currentTurn = 0;

        game.table[l][c] = playerId.equals(game.players.get(0)) ? 'X' : 'O';
        game.l.unlock();
    }

    public char[][] getGameTable(String gameId) throws GameNotExistsException {
        this.lgames.lock();
        if (! this.games.containsKey(gameId)) {
            this.lgames.unlock();
            throw new GameNotExistsException(gameId);
        }
        Game game = this.games.get(gameId);
        game.l.lock();
        this.lgames.unlock();
        char[][] res = game.table;
        game.l.unlock();
        return res;
    }

    @Override
    public String toString() {
        return "GameManager{" +
                "lgames=" + lgames +
                ", \ngames=" + games +
                ", currentId=" + currentId +
                ", lconnect=" + lconnect +
                ", canConnect=" + canConnect +
                '}';
    }
}