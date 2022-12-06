package Cliente;

import Classes.Cor;
import Classes.Parceiro;

import javax.swing.*;
import java.io.*;
import java.net.*;

public class Cliente {
    public static void main(String[] args){
        Socket socket = null;
        ObjectInputStream receptor = null;
        ObjectOutputStream transmissor = null;
        Parceiro parceiro = null;
        try {
            socket = new Socket("localhost", 12345);
            receptor = new ObjectInputStream(socket.getInputStream());
            transmissor = new ObjectOutputStream(socket.getOutputStream());
            parceiro = new Parceiro(socket, receptor, transmissor);
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Erro ao conectar com o servidor");
        }
        new GameFrame(new Tela(parceiro));
    }
}
