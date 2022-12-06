package server;

import client.PacketType;
import server.Server;
import server.ServerListener;

import java.io.DataInputStream;
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
    public void receiveData(DataInputStream in, int id) throws IOException {
        int packetType = in.read();
        switch (packetType)
        {
            case PacketType.DISCONNECT:
                server.disconnect(id);
                break;
            case PacketType.VEZ:
                server.sendDataToOthers(id, PacketType.VEZ);
                break;
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
            server.sendDataToAll(PacketType.STARTGAME);
            server.sendDataTo(0, PacketType.SYNCVALUE, 0);
            server.sendDataTo(1, PacketType.SYNCVALUE, 1);
        }
    }
}
