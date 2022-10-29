import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Peca {
    static boolean isDama = false;
    private boolean eliminada = false;
    private boolean selecionada = false;
    boolean isMouseOver = false;
    static final int border = 3;
    EnumCor cor;
    int x, y;
    private Tabuleiro tabuleiro;

    public Peca(int x, int y, EnumCor cor) {
        this.x = x;
        this.y = y;
        this.cor = cor;
    }

    //Getters e Setters
    public boolean isEliminada() {
        return eliminada;
    }
    public boolean isSelecionada() {
        return selecionada;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }
    public EnumCor getCor() {
        return cor;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setTabuleiro(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
    }
    public void setEliminada(boolean eliminada) {
        this.eliminada = eliminada;
    }
    public void setSelecionada(boolean selecionada) {
        this.selecionada = selecionada;
    }
    public void setCor(EnumCor cor) {
        this.cor = cor;
    }
    public boolean validacaoDeMovimento(int linha, int coluna) {
        if (linha < 0 || linha > 7 || coluna < 0 || coluna > 7) {
            return false;
        }
        if (Math.abs(linha - this.x) == 1 && Math.abs(coluna - this.y) == 1) {
            return true;
        }
        if (Math.abs(linha - this.x) == 2 && Math.abs(coluna - this.y) == 2) {
            return true;
        }
        return false;
    }

    public void draw(Graphics g) throws IOException {
        if (cor == cor.BRANCO) {
            g.setColor(new Color(234, 201, 188));
        } else {
            g.setColor(new Color(42, 29, 24));
        }
        // Desenha sombra:
        /*BufferedImage img = ImageIO.read(new File("src\\images\\icon.png"));

        g.drawImage(img, x * GamePanel.UNIT_SIZE -7, y * GamePanel.UNIT_SIZE -7,
                GamePanel.UNIT_SIZE+15, GamePanel.UNIT_SIZE+15, null);*/


        // Desenha peça:
        g.fillOval(x * GamePanel.UNIT_SIZE + border / 2, y * GamePanel.UNIT_SIZE + border / 2,
                GamePanel.UNIT_SIZE - border, GamePanel.UNIT_SIZE - border);
    }

    public void drawCirc(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawOval(x * GamePanel.UNIT_SIZE + border / 2, y * GamePanel.UNIT_SIZE + border / 2,
                GamePanel.UNIT_SIZE - border, GamePanel.UNIT_SIZE - border);
    }
}
