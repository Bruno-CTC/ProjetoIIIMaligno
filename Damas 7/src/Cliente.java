import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;

public class Cliente extends JPanel implements ActionListener {

    Peca[][] tabuleiro = new Peca[8][8];
    Peca pecaSelecionada;

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = SCREEN_HEIGHT / 8;

    Peca.Cor cliente;

    byte[][] possiveisMovimentos = new byte[8][8]; // 0 = não pode mover, 1 = pode mover, 2 = comer

    Peca.Cor vez = Peca.Cor.BRANCO;

    boolean invert = false;

    public void paintComponent(Graphics g) {
        // TODO Auto-generated method stub
        super.paintComponent(g);
        try {
            draw(g);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) throws IOException {

        // Desenha o tabuleiro:
        g.setColor(new Color(226, 169, 120));
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        g.setColor(new Color(82, 48, 34));

        for (int i = 0; i < UNIT_SIZE; i++) {
            for (int j = 0; j < UNIT_SIZE; j += 2) {
                g.fillRect((j + (i % 2)) * UNIT_SIZE, i * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            }
        }

        // Desenha as peças:
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tabuleiro[i][j] != null) {
                    tabuleiro[i][j].draw(g);
                }
            }
        }

        if (pecaSelecionada != null) {
            pecaSelecionada.drawCirc(g);
        }

        // Desenha as damas:
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tabuleiro[i][j] != null) {
                    if (tabuleiro[i][j].isDama) {
                        tabuleiro[i][j].drawDama(g);
                    }
                }
            }
        }

        // Desenha possíveis pulos:
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (possiveisMovimentos[i][j] > 0) {
                    g.setColor(new Color(255, 228, 228, 100));
                    if (invert) {
                        g.fillOval((7 - j) * UNIT_SIZE + UNIT_SIZE / 4 + 5 / 2, (7 - i) * UNIT_SIZE + UNIT_SIZE / 4 + 5 / 2,
                                UNIT_SIZE / 2 - 5, UNIT_SIZE / 2 - 5);
                    } else {
                        g.fillOval((j) * UNIT_SIZE + UNIT_SIZE / 4 + 5 / 2, (i) * UNIT_SIZE + UNIT_SIZE / 4 + 5 / 2,
                                UNIT_SIZE / 2 - 5, UNIT_SIZE / 2 - 5);
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public class MyMouseAdapter extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
        }
    }
}
