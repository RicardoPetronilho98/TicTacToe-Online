package business.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Game {

    public static final int N = 3;
    public static final int MAX_PLAYERS = 2;

    public Lock l;
    public char[][] table; // E -> empty | X -> p1 (true) | O -> p2 (false)
    public String id;

    public List<String> players;
    private Condition notStart;

    public int currentTurn;


    public Game(String id) {
        this.l = new ReentrantLock();
        this.table = new char[N][N];

        // empty table
        for(int l=0; l<N; l++)
            for(int c=0; c<N; c++)
                this.table[l][c] = 'E';

        this.id = id;
        this.players = new ArrayList<>();
        this.notStart = this.l.newCondition();

        this.currentTurn = 0;
    }

    public void connect(String playerId) {
        this.players.add(playerId);
        this.notStart.signal();

        // wait for other player
        while(this.players.size() != MAX_PLAYERS) {
            try {
                System.err.println("waiting for player to connect...");
                notStart.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "Game{" +
                "l=" + l +
                ", table=" + Arrays.toString(table) +
                ", id='" + id + '\'' +
                ", players=" + players +
                ", notStart=" + notStart +
                "}\n";
    }
}