package client;

public class Movimento {
    public int newX, newY;
    Peca pecaSelecionada;


    public Movimento(int newX, int newY, Peca pecaSelecionada) {
        this.newX = newX;
        this.newY = newY;
        this.pecaSelecionada = pecaSelecionada;
    }
}
