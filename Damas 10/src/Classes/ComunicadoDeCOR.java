package Classes;
import Cliente.*;

public class ComunicadoDeCOR extends Comunicado{
    Cor cor;
    public ComunicadoDeCOR(Cor cor){
        this.cor = cor;
    }
    public Cor getCor(){
        return cor;
    }
}
