import javax.swing.*;

public class GameFrame extends JFrame {

    public GameFrame(){

        this.setTitle("Damas");
        this.add(new GamePanel(new Tabuleiro()));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}