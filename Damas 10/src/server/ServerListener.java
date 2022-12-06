package server;

import mensagens.Mensagem;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public interface ServerListener {
    void receiveData(Mensagem msg, int id) throws IOException;
    void onDisconnect(int id);
    void onConnect(int id);
}
