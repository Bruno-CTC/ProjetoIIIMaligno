package server;
import client.*;
import mensagens.Mensagem;
import mensagens.MensagemDesconectar;

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
    private ArrayList<ObjectInputStream> inputs;
    // the data outputs
    private ArrayList<ObjectOutputStream> outputs;
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
            for (ObjectOutputStream out : outputs) {
                out.writeObject(new MensagemDesconectar());
                out.flush();
            }
            for (Thread receiver : receivers) {
                receiver.interrupt();
            }
            for (Socket s : connections)
            {
                s.close();
            }
            for (ObjectInputStream i : inputs)
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
    public void sendDataTo(int id, Mensagem msg)
    {
        ObjectOutputStream out = outputs.get(id);
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendDataToOthers(int id, Mensagem msg)
    {
        for (int i = 0; i < outputs.size(); i++)
        {
            if (i == id) continue;
            sendDataTo(i, msg);
        }
    }
    public void sendDataToAll(Mensagem msg) {
        for (int i = 0; i < outputs.size(); i++)
        {
            sendDataTo(i, msg);
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
                inputs.add(new ObjectInputStream(s.getInputStream()));
                outputs.add(new ObjectOutputStream(s.getOutputStream()));
                Thread receiver = new Thread(() -> {
                    while (!s.isClosed()) {
                        try {
                            int index = connections.indexOf(s);
                            Mensagem msg = (Mensagem) inputs.get(index).readObject();
                            if (msg != null)
                                listener.receiveData(msg, index);
                        }
                        catch (IOException | ClassNotFoundException e) {
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
