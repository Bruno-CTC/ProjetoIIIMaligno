package mensagens;

import client.Cor;

public class MensagemFimDeJogo extends Mensagem {
    Cor vencedor;
    public MensagemFimDeJogo(Cor vencedor) {
        this.vencedor = vencedor;
    }
    public Cor getVencedor() {
        return vencedor;
    }
}
