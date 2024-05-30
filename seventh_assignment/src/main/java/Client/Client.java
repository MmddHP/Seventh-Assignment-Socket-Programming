package Client;


import Server.Server;

import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

// Client Class
public class Client {
    private static final int port = 2020;
    private DataOutputStream out;

    public static void main(String[] args) throws IOException{

        Socket socket = new Socket("localhost", port);

        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        System.out.println("Enter your name:");
        String name = new Scanner(System.in).nextLine();
        Thread receiver = new Thread(new ReceiveMassage(socket));
        receiver.start();
        while (true) {
            System.out.println("1.Join group \n2.Download\n3.Exit");
            String userIn = new Scanner(System.in).nextLine();
            if (Objects.equals(userIn, "1")) {

                out.writeUTF("add socket");
                System.out.println("Joined to group chat successfully.\nIf you want to exit press 0.\n=========================");
                try {
                    out.writeUTF("show history");
                    while (true) {
                        String clientInput = new BufferedReader(new InputStreamReader(System.in)).readLine();
                        if (Objects.equals(clientInput, "0")) {
                            break;
                        }
                        String massage = "\033[38m" + name + ":\033[0m " + clientInput;
                        out.writeUTF(massage);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                out.writeUTF("remove socket");

            } else if (Objects.equals(userIn, "2")) {
                download();
            } else if (Objects.equals(userIn, "3")) {
                break;
            }else{
                System.out.println("Invalid demand!");
            }

        }
        out.writeUTF("CLOSE");
        out.close();
        receiver.interrupt();
        socket.close();
    }

    public static void download() {
        System.out.println("\033[34mHere is the name of files you can download:\n\033[0m");
        File directory = new File("C:\\Users\\Lenovo\\Desktop\\IdeaProjects\\Seventh-Assignment-Socket-Programming\\seventh_assignment\\src\\main\\java\\Server\\data");
        File[] files = directory.listFiles();

        for (File file : files) {
            System.out.println("\033[37m" + file.getName() + "\033[0m");
        }

        System.out.println("\033[32mType the name of file you want to download.\033[0m");
        String fileName = new Scanner(System.in).nextLine();

        System.out.println("\033[32mWhere do you want to save this file? give me a full path directory.\033[0m");
        String savePath = new Scanner(System.in).nextLine();

        File copyFile = new File("C:\\Users\\Lenovo\\Desktop\\IdeaProjects\\Seventh-Assignment-Socket-Programming\\seventh_assignment\\src\\main\\java\\Server\\data\\" + fileName);
        File pasteFile = new File(savePath + "/" + fileName);

        try {
            FileInputStream fileInputStream = new FileInputStream(copyFile);
            FileOutputStream fileOutputStream = new FileOutputStream(pasteFile);

            byte[] bytes = new byte[1024];
            int data;
            while ((data = fileInputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, data);
            }
            System.out.println("\033[32mFile downloaded successfully.\033[0m");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}