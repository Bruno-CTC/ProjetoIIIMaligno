package server;

import java.io.DataInputStream;
import java.io.IOException;

public interface ServerListener {
    void receiveData(DataInputStream in, int id) throws IOException;
}
