import javax.swing.*;

public class JPeca extends JLabel {

    private Peca peca;

    public JPeca(Peca peca){
        this.peca = peca;
        if (peca.getCor() == EnumCor.BRANCO) {
            this.setIcon(new ImageIcon("src\\images\\pecaBranca.png"));
        } else {
            this.setIcon(new ImageIcon("src\\images\\pecaPreta.png"));
        }
    }

    public Peca getPeca(){
        return peca;
    }
}
