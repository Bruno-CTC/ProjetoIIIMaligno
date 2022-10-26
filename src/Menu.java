import javax.swing.*;

public class Menu {
    public JButton btnPlay;
    public JPanel panel1;
    public JFrame janela;

    public Menu() {
        janela = new JFrame("Menu");
        janela.setContentPane(panel1);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.pack();
        janela.setVisible(true);
    }
    public static void main(String[] args){
        Menu menu = new Menu();
        menu.btnPlay.addActionListener(e -> {
            Main main = new Main();
            menu.janela.dispose();
        });
    }
}
