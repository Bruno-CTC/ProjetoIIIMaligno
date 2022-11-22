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
    int drawX, drawY;

    final boolean INVERT;

    BufferedImage img = ImageIO.read(new File("src\\images\\icon.png"));

    public Peca(int x, int y, Cor cor, boolean invert) throws IOException {
        this.x = x;
        this.y = y;
        this.cor = cor;
        this.INVERT = invert;
    }

    public void draw(Graphics g) throws IOException {
        if (INVERT) {
            this.drawX = 7 - this.x;
            this.drawY = 7 - this.y;
        } else {
            this.drawX = this.x;
            this.drawY = this.y;
        }


        if (cor == Cor.BRANCO) {
            g.setColor(new Color(234, 201, 188));
        } else {
            g.setColor(new Color(42, 29, 24));
        }

        // Desenha sombra:
        g.drawImage(img, drawX * GamePanel1.UNIT_SIZE - 7, drawY * GamePanel1.UNIT_SIZE - 7,
                GamePanel1.UNIT_SIZE + 15, GamePanel1.UNIT_SIZE + 15, null);


        // Desenha peça:
        g.fillOval(drawX * GamePanel1.UNIT_SIZE + border / 2, drawY * GamePanel1.UNIT_SIZE + border / 2,
                GamePanel1.UNIT_SIZE - border, GamePanel1.UNIT_SIZE - border);
    }

    public void drawCirc(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawOval(drawX * GamePanel1.UNIT_SIZE + border / 2, drawY * GamePanel1.UNIT_SIZE + border / 2,
                GamePanel1.UNIT_SIZE - border, GamePanel1.UNIT_SIZE - border);
    }

    public void drawDama(Graphics g) throws IOException {
        this.draw(g);

        if (cor == Cor.BRANCO) {
            g.setColor(new Color(42, 29, 24));

            g.drawImage(ImageIO.read(new File("src\\images\\coroaPreta.png")),
                    drawX * GamePanel1.UNIT_SIZE + GamePanel1.UNIT_SIZE / 4, drawY * GamePanel1.UNIT_SIZE + GamePanel1.UNIT_SIZE / 4 + 3,
                    GamePanel1.UNIT_SIZE / 2, GamePanel1.UNIT_SIZE / 2 - 6, null);
        } else {
            g.setColor(new Color(234, 201, 188));

            g.drawImage(ImageIO.read(new File("src\\images\\coroaBranca.png")),
                    drawX * GamePanel1.UNIT_SIZE + GamePanel1.UNIT_SIZE / 4, drawY * GamePanel1.UNIT_SIZE + GamePanel1.UNIT_SIZE / 4 + 3,
                    GamePanel1.UNIT_SIZE / 2, GamePanel1.UNIT_SIZE / 2 - 6, null);
        }

        g.drawOval(drawX * GamePanel1.UNIT_SIZE + border / 2 + 3, drawY * GamePanel1.UNIT_SIZE + border / 2 + 3,
                GamePanel1.UNIT_SIZE - border - 6, GamePanel1.UNIT_SIZE - border - 6);
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