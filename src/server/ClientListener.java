package server;

import java.io.DataInputStream;

public interface ClientListener {
    public void receiveData(DataInputStream in);
}
