package business;

import business.game.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {

    /** Comunicação entre business e cliente */
    private Socket cs;
    private PrintWriter pw;
    private BufferedReader br;

    /** shared object */
    private GS gs;

    /** player data */
    private String playerId;
    private String gameId;

    public ClientHandler(Socket cs, GS gs) throws IOException {
        this.cs = cs;
        this.pw = new PrintWriter(this.cs.getOutputStream(), true);
        this.br = new BufferedReader(new InputStreamReader(this.cs.getInputStream()));
        this.gs = gs;
        this.playerId = "";
        this.gameId = "";
    }

    private void connect(String[] arr) {
        if (arr.length == 1) {
            try {
                this.gameId = this.gs.connect(this.playerId);
                this.pw.println("0"); // sucesso
                this.pw.flush();
            } catch (GameNotExistsException e) {
                this.pw.println("1"); // insucesso
                this.pw.flush();
                e.printStackTrace();
            } catch (GameAlreadyExistsException e) {
                this.pw.println("2"); // insucesso
                this.pw.flush();
                e.printStackTrace();
            }
        }

        else {
            this.pw.println("99"); // insucesso
            this.pw.flush();
        }
    }

    private void play(String[] arr) {
        if (arr.length == 3) {
            try {
                int l = Integer.parseInt(arr[1]);
                int c = Integer.parseInt(arr[2]);
                this.gs.play(l, c, this.playerId, this.gameId);
                this.pw.println("0"); // sucesso
                this.pw.flush();
            } catch (GameNotExistsException e) {
                this.pw.println("1"); // insucesso
                this.pw.flush();
                e.printStackTrace();
            } catch (InvalidCoordinateException e) {
                this.pw.println("2"); // insucesso
                this.pw.flush();
                e.printStackTrace();
            } catch (CoordinateAlreadyTakenException e) {
                this.pw.println("3"); // insucesso
                this.pw.flush();
                e.printStackTrace();
            } catch (NotPlayerTurnException e) {
                this.pw.println("4"); // insucesso
                this.pw.flush();
                e.printStackTrace();
            }
        }

        else {
            this.pw.println("99"); // insucesso
            this.pw.flush();
        }
    }

    private void getGameTable(String[] arr) {
        if (arr.length == 1) {
            try {
                char[][] table = this.gs.getGameTable(this.gameId);
                StringBuilder sb = new StringBuilder();
                for(int l=0; l<table.length; l++) {
                    for (int c = 0; c < table[l].length; c++) {
                        sb.append(table[l][c]).append("-");
                    }
                    sb.deleteCharAt(sb.length()-1);
                    sb.append("_");
                }
                sb.deleteCharAt(sb.length()-1);
                this.pw.println(sb.toString());
                this.pw.flush();
            } catch (GameNotExistsException e) {
                this.pw.println("1"); // insucesso
                this.pw.flush();
                e.printStackTrace();
            }
        }

        else {
            this.pw.println("99"); // insucesso
            this.pw.flush();
        }
    }

    private void createPlayer(String[] arr) {
        if (arr.length == 2) {
            this.playerId = this.gs.createPlayer(arr[1]);
            this.pw.println("0"); // sucesso
            this.pw.flush();
        }

        else {
            this.pw.println("99"); // insucesso
            this.pw.flush();
        }
    }

    private void watchGame(String[] arr) {
        if (arr.length == 2) {
            this.playerId = "-1";
            this.gameId = arr[1];
            this.pw.println("0"); // sucesso
            this.pw.flush();
        }

        else {
            this.pw.println("99"); // insucesso
            this.pw.flush();
        }
    }

    private void parseMsg(String msg) {
        String[] tmp = msg.split("_");
        String op = tmp[0];

        if (this.playerId.equals("")) {
            switch (op) {
                case "a":
                    this.createPlayer(tmp);
                    break;
                case "e":
                    this.watchGame(tmp);
                    break;
                default:
                    this.pw.println("99"); // insucesso
                    this.pw.flush();
                    break;
            }
        }

        else if (this.gameId.equals("")) {
            switch (op) {
                case "b":
                    this.connect(tmp);
                    break;
                default:
                    this.pw.println("99"); // insucesso
                    this.pw.flush();
                    break;
            }
        }

        else {
            switch (op) {
                case "c":
                    this.play(tmp);
                    break;
                case "d":
                    this.getGameTable(tmp);
                    break;
                default:
                    this.pw.println("99"); // insucesso
                    this.pw.flush();
                    break;
            }
        }

        // System.out.println(this.gs);
    }

    @Override
    public void run() {
        String msg;
        System.out.println(this.cs + " >>> connected");
        try {
            while (true) {
                msg = br.readLine();
                if (msg == null) {
                    this.gs.removeGame(this.gameId);
                    break;
                }
                this.parseMsg(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println(this.cs + " >>> disconnected");
        }
    }
}