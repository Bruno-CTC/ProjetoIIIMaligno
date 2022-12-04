package Classes;

public class PedidoDeSoma extends Comunicado {

    double valor;

    public PedidoDeSoma(double valor){
        this.valor = valor;
    }

    public double getValor(){
        return valor;
    }

    // To string
    @Override
    public String toString() {
        return "Classes.PedidoDeSoma{" +
                "valor=" + valor +
                '}';
    }
}
