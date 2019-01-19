package business;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor extends ServerSocket {

    private static final int PORT = 10011;
    private GS gs; // objeto partilhado

    public Servidor() throws IOException {
        super(PORT);
        this.gs = new GS();
    }

    public void start() {
        System.out.println("Deamon started on port " + PORT);
        Socket cs;
        while (true) {
            try {
                cs = this.accept();
                new ClientHandler(cs, this.gs).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            new Servidor().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}