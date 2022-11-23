import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Peca {
    boolean isDama = false;

    static final int border = 3;

    enum Cor {BRANCO, PRETO}

    Cor cor = Cor.BRANCO;
    int x, y;

    BufferedImage img = ImageIO.read(new File("src\\images\\icon.png"));

    public Peca(int x, int y, Cor cor) throws IOException {
        this.x = x;
        this.y = y;
        this.cor = cor;
    }

    public void draw(Graphics g) throws IOException {
        if (cor == Cor.BRANCO) {
            g.setColor(new Color(234, 201, 188));
        } else {
            g.setColor(new Color(42, 29, 24));
        }
        // Desenha sombra:

        g.drawImage(img, (7-x) * GamePanel.UNIT_SIZE - 7, (7-y) * GamePanel.UNIT_SIZE - 7,
                GamePanel.UNIT_SIZE + 15, GamePanel.UNIT_SIZE + 15, null);


        // Desenha peça:
        g.fillOval((7-x) * GamePanel.UNIT_SIZE + border / 2, (7-y) * GamePanel.UNIT_SIZE + border / 2,
                GamePanel.UNIT_SIZE - border, GamePanel.UNIT_SIZE - border);
    }

    public void drawCirc(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawOval((7-x) * GamePanel.UNIT_SIZE + border / 2, (7-y) * GamePanel.UNIT_SIZE + border / 2,
                GamePanel.UNIT_SIZE - border, GamePanel.UNIT_SIZE - border);
    }

    public void drawDama(Graphics g) throws IOException {
        this.draw(g);

        if (cor == Cor.BRANCO) {
            g.drawImage(ImageIO.read(new File("src\\images\\coroaPreta.png")),
                    (7-x) * GamePanel.UNIT_SIZE + GamePanel.UNIT_SIZE / 4, (7-y) * GamePanel.UNIT_SIZE + GamePanel.UNIT_SIZE / 4 + 3,
                    GamePanel.UNIT_SIZE / 2, GamePanel.UNIT_SIZE / 2 - 6, null);
        }
        else {
            g.drawImage(ImageIO.read(new File("src\\images\\coroaBranca.png")),
                    (7-x) * GamePanel.UNIT_SIZE + GamePanel.UNIT_SIZE / 4, (7-y) * GamePanel.UNIT_SIZE + GamePanel.UNIT_SIZE / 4 + 3,
                    GamePanel.UNIT_SIZE / 2, GamePanel.UNIT_SIZE / 2 - 6, null);
        }
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}