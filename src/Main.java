import server.Client;
import server.ClientListener;
import server.PacketType;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;

public class Main {
    public JPanel panel1;
    public JButton button1;
    public JButton button2;

    public Main() {
        JFrame frame = new JFrame("Main");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    public static void main(String[] args){

    }
}