package mensagens;

import client.Cor;

public class MensagemTrocarVez extends Mensagem {
    Cor vez;
    public MensagemTrocarVez(Cor vez) {
        this.vez = vez;
    }
    public Cor getVez()
    {
        return vez;
    }
}
