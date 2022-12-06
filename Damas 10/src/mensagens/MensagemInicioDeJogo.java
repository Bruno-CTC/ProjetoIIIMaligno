package mensagens;

import client.Cor;

public class MensagemInicioDeJogo extends Mensagem {
    Cor cor;
    public MensagemInicioDeJogo(Cor cor) {
        this.cor = cor;
    }
    public Cor getCor()
    {
        return cor;
    }
}
