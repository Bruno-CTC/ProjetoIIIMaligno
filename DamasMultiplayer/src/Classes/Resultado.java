package Classes;

public class Resultado extends Comunicado {

    double valorResultante;

    public Resultado(double valorResultante){
        this.valorResultante = valorResultante;
    }

    public double getValorResultante(){
        return valorResultante;
    }

    // To string
    @Override
    public String toString() {
        return "Classes.Resultado{" +
                "valorResultante=" + valorResultante +
                '}';
    }
}
