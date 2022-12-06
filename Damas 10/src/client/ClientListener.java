package client;

import mensagens.Mensagem;

import java.io.ObjectInputStream;

public interface ClientListener {
    void receiveData(Mensagem msg);
}
