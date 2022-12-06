package server;
import client.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    // what receives the data from the client
    private ServerListener listener;
    // the server's socket
    private ServerSocket server = null;
    // the connection handler
    private static Thread connect;
    // the connections
    private ArrayList<Socket> connections;
    // the data receivers
    private ArrayList<Thread> receivers;
    // the data inputs
    private ArrayList<DataInputStream> inputs;
    // the data outputs
    private ArrayList<DataOutputStream> outputs;
    public Server(ServerListener listener, int port) throws IOException {
        this.listener = listener;
        connections = new ArrayList<>();
        inputs = new ArrayList<>();
        outputs = new ArrayList<>();
        server = new ServerSocket(port);
        receivers = new ArrayList<>();

        connect = new Thread(this::handleConnections);
        connect.start();
    }
    public void close() {
        try {
            connect.interrupt();
            for (DataOutputStream out : outputs) {
                out.write(PacketType.DISCONNECT);
            }
            for (Thread receiver : receivers) {
                receiver.interrupt();
            }
            for (Socket s : connections)
            {
                s.close();
            }
            for (DataInputStream i : inputs)
            {
                i.close();
            }
            server.close();
        }
        catch (IOException e) {
            System.out.print(e);
        }
    }
    public void disconnect(int id) throws IOException {
        receivers.get(id).interrupt();
        receivers.remove(id);
        inputs.get(id).close();
        inputs.remove(id);
        outputs.get(id).close();
        outputs.remove(id);
        connections.get(id).close();
        connections.remove(id);
        listener.onDisconnect(id);
    }
    public void sendDataTo(int id, int type, Object... args)
    {
        DataOutputStream out = outputs.get(id);
        try {
            out.write(type);
            for (Object arg : args)
            {
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendDataToOthers(int id, int type, Object... args)
    {
        for (int i = 0; i < outputs.size(); i++)
        {
            if (i == id) continue;
            sendDataTo(i, type, args);
        }
    }
    public void sendDataToAll(int type, Object... args)
    {
        for (int i = 0; i < outputs.size(); i++)
        {
            sendDataTo(i, type, args);
        }
    }
    public int getClientCount()
    {
        return connections.size();
    }
    private void handleConnections() {
        while (!server.isClosed())
        {
            try {
                Socket s = server.accept();
                connections.add(s);
                inputs.add(new DataInputStream(s.getInputStream()));
                outputs.add(new DataOutputStream(s.getOutputStream()));
                Thread receiver = new Thread(() -> {
                    while (!s.isClosed()) {
                        try {
                            int index = connections.indexOf(s);
                            if (index > inputs.size()) continue;
                            if (inputs.get(index).available() <= 0) continue;
                            listener.receiveData(inputs.get(index), index);
                        }
                        catch (IOException e) {
                        }
                    }
                });
                receivers.add(receiver);
                receiver.start();
                listener.onConnect(connections.size() - 1);
            } catch (IOException e) {
            }
        }
    }
}
