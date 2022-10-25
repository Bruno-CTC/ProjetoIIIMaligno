import client.Client;
import client.ClientListener;
import client.PacketType;

import javax.swing.*;
import java.io.DataInputStream;

public class Janela implements ClientListener {
    public Client client;
    public JTextField textField = new JTextField();

    public Janela() {
        client = new Client(this, "177.220.18.125", 1984);
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
