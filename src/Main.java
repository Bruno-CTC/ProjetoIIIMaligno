import client.Client;
import client.ClientListener;
import client.PacketType;

import javax.swing.*;
import java.io.DataInputStream;

public class Main {
    public static class Janela implements ClientListener {
        public Client client;
        public JTextField textField = new JTextField();

        public Janela() {
            client = new Client(this, "localhost", 5000);
            JFrame frame = new JFrame("Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 500);
            frame.setLayout(null);
            frame.setVisible(true);

            textField.setBounds(0, 60, 100, 20);
            frame.add(textField);

            JButton btn = new JButton("Send packet");
            btn.setBounds(0, 0, 100, 50);
            btn.addActionListener(e -> client.sendPacket(PacketType.SYNCVALUE, textField.getText()));
            frame.add(btn);

            JButton btnFechar = new JButton("Fechar");
            btnFechar.setBounds(0, 100, 100, 50);
            btnFechar.addActionListener(e -> {
                client.close();
                frame.dispose();
            });
            frame.add(btnFechar);
        }

        @Override
        public void receiveData(DataInputStream in) {
            try {
                int id = in.read();
                switch (id) {
                    case PacketType.SYNCVALUE:
                        textField.setText(in.readUTF());
                        break;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void main(String[] args) {
        new Janela();
        new Janela();
    }
}