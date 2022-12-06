package Cliente;

import javax.swing.*;
import java.io.IOException;

public class GameFrame extends JFrame {

    GameFrame(Tela cliente){
        try{
            this.add(cliente);
        }catch (Exception e){
            System.out.println(e);
        }
        this.setTitle("Damas");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}