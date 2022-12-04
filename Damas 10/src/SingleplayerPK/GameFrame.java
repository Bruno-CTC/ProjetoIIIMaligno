package SingleplayerPK;

import javax.swing.*;
import java.io.IOException;

public class GameFrame extends JFrame {

    GameFrame() throws IOException {

        this.add(new SinglePlayer());
        this.setTitle("Damas");
        //ImageIcon img = new ImageIcon("src\\images\\icon.png");
        //this.setIconImage(img.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}