import server.Client;
import server.ClientListener;
import server.PacketType;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;

public class Main {
    public static class Janela implements ClientListener {
        public Client client;
        public JTextField textField = new JTextField();
        public TextArea messages = new TextArea();
        private final String name = System.getProperty("user.name");
        public Janela() {
            client = new Client(this, "177.220.18.125", 1984);
            JFrame frame = new JFrame("Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 500);
            frame.setLayout(null);
            frame.setVisible(true);

            textField.setBounds(0, 60, 500, 20);
            frame.add(textField);

            messages.setBounds(0, 90, 500, 200);
            messages.setEditable(false);
            frame.add(messages);

            JButton btn = new JButton("Mandar Mensagem");
            btn.setBounds(0, 0, 500, 50);
            btn.addActionListener(e -> {
                messages.setText(messages.getText() + "\n" + name + ": " + textField.getText());
                client.sendPacket(PacketType.SYNCVALUE, name, textField.getText());
                textField.setText("");
                textField.grabFocus();
            });
            frame.add(btn);

            JButton btnFechar = new JButton("Fechar");
            btnFechar.setBounds(0, 400, 500, 50);
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
                    case PacketType.SYNCVALUE -> messages.setText(messages.getText() + "\n" + in.readUTF() + ": " + in.readUTF());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void main(String[] args) {
        new Janela();
    }
}