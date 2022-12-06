package client;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Peca {
    boolean isDama = false;

    private static final int border = 3;
    Cor cor;
    int x, y;
    int drawX, drawY;
    boolean invert;

    BufferedImage img;
    {
        try {
            img = ImageIO.read(new File("src\\images\\icon.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar a imagem");
        }
    }

    public Peca(int x, int y, Cor cor){
        this.x = x;
        this.y = y;
        this.cor = cor;
    }

    public void draw(Graphics g, boolean invert) throws IOException {
        if (invert) {
            this.invert=invert;
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
        g.drawImage(img, drawX * Tela.UNIT_SIZE - 7, drawY * Tela.UNIT_SIZE - 7,
                Tela.UNIT_SIZE + 15, Tela.UNIT_SIZE + 15, null);


        // Desenha peça:
        g.fillOval(drawX * Tela.UNIT_SIZE + border / 2, drawY * Tela.UNIT_SIZE + border / 2,
                Tela.UNIT_SIZE - border, Tela.UNIT_SIZE - border);
    }

    public void drawCirc(Graphics g) {
        g.setColor(Color.WHITE);
        /*g.drawOval(drawX * SinglePlayer.UNIT_SIZE + border / 2, drawY * SinglePlayer.UNIT_SIZE + border / 2,
                SinglePlayer.UNIT_SIZE - border, SinglePlayer.UNIT_SIZE - border);*/
    }

    public void drawDama(Graphics g) throws IOException {
        this.draw(g, invert);

        if (cor == Cor.BRANCO) {
            g.setColor(new Color(42, 29, 24));

            /*g.drawImage(ImageIO.read(new File("src\\images\\coroaPreta.png")),
                    drawX * SinglePlayer.UNIT_SIZE + SinglePlayer.UNIT_SIZE / 4, drawY * SinglePlayer.UNIT_SIZE + SinglePlayer.UNIT_SIZE / 4 + 3,
                    SinglePlayer.UNIT_SIZE / 2, SinglePlayer.UNIT_SIZE / 2 - 6, null);*/
        } else {
            g.setColor(new Color(234, 201, 188));

            /*g.drawImage(ImageIO.read(new File("src\\images\\coroaBranca.png")),
                    drawX * SinglePlayer.UNIT_SIZE + SinglePlayer.UNIT_SIZE / 4, drawY * SinglePlayer.UNIT_SIZE + SinglePlayer.UNIT_SIZE / 4 + 3,
                    SinglePlayer.UNIT_SIZE / 2, SinglePlayer.UNIT_SIZE / 2 - 6, null);*/
        }

        /*g.drawOval(drawX * SinglePlayer.UNIT_SIZE + border / 2 + 3, drawY * SinglePlayer.UNIT_SIZE + border / 2 + 3,
                SinglePlayer.UNIT_SIZE - border - 6, SinglePlayer.UNIT_SIZE - border - 6);*/
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