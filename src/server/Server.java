package server;
import client.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private ServerSocket server = null;
    private static Thread data;
    private static Thread connect;
    private ArrayList<Socket> connections;
    private ArrayList<DataInputStream> inputs;
    private ArrayList<DataOutputStream> outputs;
    private boolean updating;
    public Server(int port)
    {
        try{
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
    public void close()
    {
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
                    // receiving client data
                    if (i.available() <= 0) continue;
                    int packetType = i.read();
                    switch (packetType)
                    {
                        case PacketType.DISCONNECT:
                            System.out.println("Client with id: " + id + " disconnected");
                            connections.remove(id);
                            inputs.remove(id);
                            outputs.remove(id);
                            id--;
                            break;
                        case PacketType.SYNCVALUE:
                            String val = i.readUTF();
                            System.out.println("Received sync packet with value: " + val);
                            for (DataOutputStream o : outputs) {
                                if (o == outputs.get(id)) continue;
                                o.write(PacketType.SYNCVALUE);
                                o.writeUTF(val);
                            }
                            break;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            updating = false;
        }
    }
    private void handleConnections()
    {
        while (!server.isClosed())
        {
            try {
                Socket s = server.accept();
                System.out.println("Client with id " + connections.size() + " connected");
                connections.add(s);
                while (updating)
                {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                inputs.add(new DataInputStream(s.getInputStream()));
                outputs.add(new DataOutputStream(s.getOutputStream()));
            } catch (IOException e) {
            }
        }
    }
    public static void main(String[] args)
    {
        Server serv = new Server(1194);
    }
}
