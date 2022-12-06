package client;

import java.io.DataInputStream;

public interface ClientListener {
    void receiveData(DataInputStream in);
}
