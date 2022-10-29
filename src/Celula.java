import javax.swing.*;
import java.awt.*;

public class Celula extends JPanel {

    private JPeca jPeca;
    private int linha, coluna;

    public Celula(int l, int c){
        linha = l;
        coluna = c;
    }
    public Celula(JPeca peca) {
        jPeca = peca;
        linha = peca.getPeca().getX();
        coluna = peca.getPeca().getY();
        this.add(jPeca);
        if (jPeca.getPeca() != null && jPeca.getPeca().isSelecionada()) {
            this.setBorder(BorderFactory.createLineBorder(Color.RED, 5));
        }
    }

    public int getLinha() {
        return linha;
    }
    public int getColuna() {
        return coluna;
    }
}
