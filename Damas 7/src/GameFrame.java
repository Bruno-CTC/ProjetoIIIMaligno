import javax.swing.*;
import java.io.IOException;

public class GameFrame extends JFrame {

    GameFrame(Peca.Cor cliente) throws IOException {

        this.add(new GamePanel1(cliente));
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