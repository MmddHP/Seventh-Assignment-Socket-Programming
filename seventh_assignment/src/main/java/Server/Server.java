package Server;

import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Server Class
public class Server {

    private static final int port = 2020;
    public static ArrayList<Socket> groupClients = new ArrayList<Socket>();
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(8);
    public static ArrayList<String> massages = new ArrayList<String>();

    public static void main(String[] args) throws EOFException{
        ServerSocket listener = null;
        try{
            listener = new ServerSocket(port);
            System.out.println("Waiting for client ...");
            while (true){
                Socket socket = listener.accept();
                System.out.println("New client joined:) ");
                threadPool.execute(new ServerHandler(socket));
            }
        } catch (IOException  e){
//            throw new RuntimeException(e);
        } finally {
            if (listener != null) {
                try {
                    listener.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            threadPool.shutdown();
        }

    }
}