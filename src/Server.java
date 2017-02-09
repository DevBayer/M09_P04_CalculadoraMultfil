import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by 23878410v on 08/02/17.
 */
public class Server {
    public static void main(String[] args) {
        try {
            ServerData data = new ServerData();
            ServerSocket server = new ServerSocket(5555);
            System.out.println("Running server on 0.0.0.0:5555");
            while(true) {
                Socket socket = server.accept();
                System.out.println("Accepted connection from "+socket.getInetAddress());
                data.getListSockets().add(socket);
                ServerWorker worker = new ServerWorker(data, socket);
                worker.start();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
