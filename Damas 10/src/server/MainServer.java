package server;

import client.Cor;
import mensagens.*;

import java.io.IOException;

public class MainServer implements ServerListener {
    private static Server server;
    public static void main(String[] args) {
        try {
            server = new Server(new MainServer(), 12345);
            System.out.println("Started server on localhost:12345");
        } catch (IOException e) {
            System.out.println("Error starting server:");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void receiveData(Mensagem msg, int id)
    {
        if (msg instanceof MensagemTrocarVez)
        {
            server.sendDataToAll(msg);
        }
        else if (msg instanceof MensagemSincronizarTabuleiro)
        {
            server.sendDataToAll(msg);
        }
        else if (msg instanceof MensagemFimDeJogo)
        {
            server.sendDataToAll(msg);
        }
    }

    @Override
    public void onDisconnect(int id) {
        System.out.println("Client " + id + " disconnected");
    }

    @Override
    public void onConnect(int id) {
        System.out.println("Client " + id + " connected");
        if (id == 1)
        {
            server.sendDataTo(0, new MensagemInicioDeJogo(Cor.BRANCO));
            server.sendDataTo(1, new MensagemInicioDeJogo(Cor.PRETO));
        }
    }
}
