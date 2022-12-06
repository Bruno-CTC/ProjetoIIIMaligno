package client;

import java.io.*;
import java.net.Socket;

public class Client {
    // connects to the server
    private Socket socket = null;
    // outputs data
    private DataOutputStream out = null;
    // receives data
    private DataInputStream in = null;
    // the thread that receives data
    private static Thread data;
    // the class that handles the data received
    private ClientListener listener;
    public Client(ClientListener listener, String address, int port)
    {
        try
        {
            this.listener = listener;
            socket = new Socket(address, port);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        } catch(IOException u)
        {
            System.out.println(u);
        }
        data = new Thread(this::receiveData);
        data.start();
    }
    public void close()
    {
        try
        {
            out.write(PacketType.DISCONNECT);
            in.close();
            out.close();
            socket.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
    private void receiveData() {
        while (!socket.isClosed()) {
            try {
                if (in.available() <= 0) continue;
                listener.receiveData(in);
            }
            catch (IOException e) {
                System.out.println(e);
            }
        }
    }
    public void sendPacket(int id, Object... args)
    {
        try {
            out.write(id);
            for (Object arg : args) {
                if (arg instanceof Integer)
                {
                    out.writeInt((int)arg);
                }
                else if (arg instanceof String)
                {
                    out.writeUTF((String)arg);
                }
                else if (arg instanceof Float)
                {
                    out.writeFloat((float)arg);
                }
                else if (arg instanceof Boolean)
                {
                    out.writeBoolean((boolean)arg);
                }
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
}
