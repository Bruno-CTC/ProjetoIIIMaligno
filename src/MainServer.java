import client.PacketType;
import server.Server;
import server.ServerListener;

import java.io.DataInputStream;
import java.io.IOException;

public class MainServer implements ServerListener {
    private static Server server;
    public static void main(String[] args) {
        server = new Server(new MainServer(), 5000);
    }

    @Override
    public void receiveData(DataInputStream in, int id) throws IOException {
        int packetType = in.read();
        switch (packetType)
        {
            case PacketType.DISCONNECT:
                server.disconnect(id);
                break;
            case PacketType.SYNCVALUE:
                String val = in.readUTF();
                server.sendDataToOthers(id, PacketType.SYNCVALUE, val);
                break;
        }
    }
}
