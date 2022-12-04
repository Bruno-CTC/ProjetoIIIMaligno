package SingleplayerPK;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Peca {
    boolean isDama = false;

    private static final int border = 3;

    enum Cor {BRANCO, PRETO}

    Cor cor = Cor.BRANCO;
    int x, y;
    static BufferedImage img;

    static {
        try {
            img = ImageIO.read(new File("src\\images\\icon.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
        g.drawImage(img, x * SinglePlayer.UNIT_SIZE - 7, y * SinglePlayer.UNIT_SIZE - 7,
                SinglePlayer.UNIT_SIZE + 15, SinglePlayer.UNIT_SIZE + 15, null);


        // Desenha peça:
        g.fillOval(x * SinglePlayer.UNIT_SIZE + border / 2, y * SinglePlayer.UNIT_SIZE + border / 2,
                SinglePlayer.UNIT_SIZE - border, SinglePlayer.UNIT_SIZE - border);
    }

    public void drawCirc(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawOval(x * SinglePlayer.UNIT_SIZE + border / 2, y * SinglePlayer.UNIT_SIZE + border / 2,
                SinglePlayer.UNIT_SIZE - border, SinglePlayer.UNIT_SIZE - border);
    }

    public void drawDama(Graphics g) throws IOException {
        this.draw(g);

        if (cor == Cor.BRANCO) {
            g.setColor(new Color(42, 29, 24));

            g.drawImage(ImageIO.read(new File("src\\images\\coroaPreta.png")),
                    x * SinglePlayer.UNIT_SIZE + SinglePlayer.UNIT_SIZE / 4, y * SinglePlayer.UNIT_SIZE + SinglePlayer.UNIT_SIZE / 4 + 3,
                    SinglePlayer.UNIT_SIZE / 2, SinglePlayer.UNIT_SIZE / 2 - 6, null);
        } else {
            g.setColor(new Color(234, 201, 188));

            g.drawImage(ImageIO.read(new File("src\\images\\coroaBranca.png")),
                    x * SinglePlayer.UNIT_SIZE + SinglePlayer.UNIT_SIZE / 4, y * SinglePlayer.UNIT_SIZE + SinglePlayer.UNIT_SIZE / 4 + 3,
                    SinglePlayer.UNIT_SIZE / 2, SinglePlayer.UNIT_SIZE / 2 - 6, null);
        }

        g.drawOval(x * SinglePlayer.UNIT_SIZE + border / 2 + 3, y * SinglePlayer.UNIT_SIZE + border / 2 + 3,
                SinglePlayer.UNIT_SIZE - border - 6, SinglePlayer.UNIT_SIZE - border - 6);
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

    public Peca(Peca peca) throws IOException {
        this.x = peca.x;
        this.y = peca.y;
        this.cor = peca.cor;
        this.isDama = peca.isDama;
    }

    public Peca clone() {
        try {
            return new Peca(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}