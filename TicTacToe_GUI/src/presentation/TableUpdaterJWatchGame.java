/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ricardopetronilho
 */
public class TableUpdaterJWatchGame implements Runnable {
    
    private JWatchGame parent;
    
    public TableUpdaterJWatchGame(JWatchGame parent) {
        this.parent = parent;
    }

    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(200);
                updateTable();
            } catch (InterruptedException ex) {
                Logger.getLogger(JPlayGame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void updateTable() {
        try {
            parent.lcs.lock();
            parent.pw.println("d");
            parent.pw.flush();
            String msg = parent.br.readLine();
            parent.lcs.unlock();
            switch (msg) {
                case "1":
                    JOptionPane.showMessageDialog(this.parent, "O jogo foi cancelado por parte dos jogadoes!", "Aviso", JOptionPane.WARNING_MESSAGE);
                    System.exit(3);
                    break;
                case "99":
                    JOptionPane.showMessageDialog(this.parent, "Erro interno, dados enviados pela rede corruptos!", "ERRO", JOptionPane.ERROR_MESSAGE);
                    System.exit(3);
                    break;
                default:
                    int l=0, c;
                    for(String line: msg.split("_")) { 
                        c = 0;
                        for(String col: line.split("-")) {
                            char state = col.charAt(0);                
                            if (state == 'X') parent.buttons[l][c].setIcon(new javax.swing.ImageIcon(getClass().getResource("/presentation/assets/x.png")));
                            else if (state == 'O') parent.buttons[l][c].setIcon(new javax.swing.ImageIcon(getClass().getResource("/presentation/assets/circle.png")));
                            c++;
                        }
                        l++;
                    }
            }         
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
    }
}
