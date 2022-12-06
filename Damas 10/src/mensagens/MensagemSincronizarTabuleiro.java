package mensagens;

import client.Peca;

public class MensagemSincronizarTabuleiro extends Mensagem {
    private Peca[][] tabuleiro;

    public MensagemSincronizarTabuleiro(Peca[][] tabuleiro) {
        this.tabuleiro = tabuleiro;
    }

    public Peca[][] getTabuleiro() {
        return tabuleiro;
    }
}
