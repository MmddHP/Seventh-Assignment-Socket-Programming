package Client;

import Server.Server;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class ReceiveMassage implements Runnable {
    private DataInputStream in;

    public ReceiveMassage(Socket client) throws IOException {
        this.in = new DataInputStream(client.getInputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {
                String comeFromServer = in.readUTF();
                System.out.println(comeFromServer);
                if(Thread.interrupted()){
                    in.close();
                    break;
                }
            }
        } catch (IOException e) {
//            throw new RuntimeException(e);
        }
    }
}
