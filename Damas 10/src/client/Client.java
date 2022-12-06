package client;

import mensagens.Mensagem;
import mensagens.MensagemDesconectar;

import java.io.*;
import java.net.Socket;

public class Client {
    // connects to the server
    private Socket socket = null;
    // outputs data
    private ObjectOutputStream out = null;
    // receives data
    private ObjectInputStream in = null;
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
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
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
            data.interrupt();
            out.writeObject(new MensagemDesconectar());
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
                Mensagem msg = (Mensagem) in.readObject();
                if (msg != null)
                    listener.receiveData(msg);
            }
            catch (IOException e) {
                System.out.println(e);
            } catch (ClassNotFoundException e) {
                System.out.println(e);
            }
        }
    }
    public void sendData(Mensagem msg)
    {
        try
        {
            out.writeObject(msg);
            out.flush();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
}
