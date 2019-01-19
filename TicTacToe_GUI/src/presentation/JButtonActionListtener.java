/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author ricardopetronilho
 */
public class JButtonActionListtener implements ActionListener {
    
    private int l;
    private int c;
    private JPlayGame parent;
    
    public JButtonActionListtener(int l, int c, JPlayGame parent) {
        this.l = l;
        this.c = c;
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            parent.lcs.lock();
            parent.pw.println("c_" + l + "_" + c);
            parent.pw.flush();
            String msg = parent.br.readLine();    
            parent.lcs.unlock();
            switch (msg) {
                case "1":
                    JOptionPane.showMessageDialog(this.parent, "Erro interno, dados do jogo atual foram perdidos!", "ERRO", JOptionPane.ERROR_MESSAGE);
                    System.exit(3);
                    break;
                case "2":
                    JOptionPane.showMessageDialog(this.parent, "Erro interno, coordenadas invalidas!", "ERRO", JOptionPane.ERROR_MESSAGE);
                    System.exit(3);
                    break;
                case "3":
                    JOptionPane.showMessageDialog(this.parent, "Essa coordenada ja foi selecionada!", "Aviso", JOptionPane.WARNING_MESSAGE);
                    break;
                case "4":
                    JOptionPane.showMessageDialog(this.parent, "Espere pelo seu turno!", "Aviso", JOptionPane.WARNING_MESSAGE);
                    break;
                case "99":
                    JOptionPane.showMessageDialog(this.parent, "Erro interno, dados enviados pela rede corruptos!", "ERRO", JOptionPane.ERROR_MESSAGE);
                    System.exit(3);
                    break;
                default:
                    break; 
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(3);
        }
    }
    
    
    
}
