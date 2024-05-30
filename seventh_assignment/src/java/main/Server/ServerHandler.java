package main.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServerHandler implements Runnable{

    private Socket client;
    private DataInputStream in;
    private DataOutputStream out;
    private ReentrantLock lock = new ReentrantLock();

    public ServerHandler(Socket client) throws IOException {
        this.client = client;
        this.in = new DataInputStream(client.getInputStream());
        this.out = new DataOutputStream(client.getOutputStream());
    }

    @Override
    public void run(){

        try{
            while (true){
                String request = this.in.readUTF();
                if(request.equals("add socket")){
                    Server.groupClients.add(client);
                } else if (request.equals("remove socket")) {
                    Server.groupClients.remove(client);
                } else {
                    System.out.println(request);
                    Server.massages.add(request);
                    sendToAll(request);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                in.close();
                out.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void sendToAll(String msg) throws IOException {
        for (Socket aClient : Server.groupClients) {
            if(aClient != client){
            DataOutputStream out = new DataOutputStream(aClient.getOutputStream());
            out.writeUTF(msg);
            }
        }
    }
}
