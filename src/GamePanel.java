import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.IOException;

public class GamePanel extends JPanel implements MouseListener {
    private Tabuleiro tabuleiro;
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = SCREEN_HEIGHT / 8;
    static int mouseX;
    static int mouseY;
    boolean isPeca = false;
    public GamePanel(Tabuleiro t) {
        this.tabuleiro = t;
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        desenhaTabuleiro();
    }
    public void desenhaTabuleiro(){
        this.removeAll();
        this.setLayout(new GridLayout(8, 8));
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                Celula celula;
                Peca peca = tabuleiro.getPeca(j, i);
                if(peca == null){
                    celula = new Celula(j, i);
                }else{
                    celula = new Celula(new JPeca(peca));
                }
                if((i+j)%2 == 0){
                    celula.setBackground(new Color(226, 169, 120));
                }else{
                    celula.setBackground(new Color(82, 48, 34));
                }
                celula.addMouseListener(this);
                this.add(celula);
            }
        }
        this.revalidate();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Celula celula = (Celula) e.getSource();
        this.tabuleiro.realizaJogada(celula.getLinha(), celula.getColuna());
        this.desenhaTabuleiro();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}