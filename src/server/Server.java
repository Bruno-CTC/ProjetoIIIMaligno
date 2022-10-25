package server;
import client.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private ServerListener listener;
    private ServerSocket server = null;
    private static Thread data;
    private static Thread connect;
    private ArrayList<Socket> connections;
    private ArrayList<DataInputStream> inputs;
    private ArrayList<DataOutputStream> outputs;
    private boolean updating;
    public Server(ServerListener listener, int port)
    {
        try{
            this.listener = listener;
            connections = new ArrayList<>();
            inputs = new ArrayList<>();
            outputs = new ArrayList<>();
            server = new ServerSocket(port);
            System.out.println("Server started on localhost:" + port);

            connect = new Thread(this::handleConnections);
            connect.start();
            data = new Thread(this::receiveData);
            data.start();
        }
        catch(IOException i){
            System.out.println(i);
        }
    }
    public void close() {
        System.out.println("Closing connection");
        try {
            connect.interrupt();
            data.interrupt();
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
    private void receiveData() {
        while (!server.isClosed()) {
            updating = true;
            for (int id = 0; id < inputs.size(); id++) {
                DataInputStream i = inputs.get(id);
                try {
                    if (i.available() <= 0) continue;
                    listener.receiveData(i, id);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            updating = false;
        }
    }
    public void disconnect(int id)
    {
        System.out.println("Client with id: " + id + " disconnected");
        connections.remove(id);
        inputs.remove(id);
        outputs.remove(id);
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
    private void handleConnections() {
        while (!server.isClosed())
        {
            try {
                Socket s = server.accept();
                System.out.println("Client with id " + connections.size() + " connected");
                while (updating)
                {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                connections.add(s);
                inputs.add(new DataInputStream(s.getInputStream()));
                outputs.add(new DataOutputStream(s.getOutputStream()));
            } catch (IOException e) {
            }
        }
    }
}
