import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by 23878410v on 08/02/17.
 */
public class Client {
    public static void main(String[] args) throws IOException {
        System.out.println("Creando cliente");
            /* Socket TCP */
        Socket client = new Socket();

        System.out.println("Estableciendo conexión..");
        InetSocketAddress addr = new InetSocketAddress("localhost", 5555);
        client.connect(addr);

        InputStream inputStream = client.getInputStream();
        OutputStream outputStream = client.getOutputStream();
        outputStream.write(new String("init").getBytes());
        outputStream.flush();

        Boolean authenticated = false;
        Boolean waiting = false;
        Scanner sc = new Scanner(System.in);

        while(true) {
            if(!authenticated) {
                if (inputStream.available() > 0) {
                    byte[] hoho = new byte[inputStream.available()];
                    inputStream.read(hoho);
                    String msg = new String(hoho);
                    if (msg.equals("authenticated")) {
                        System.out.println("The client has succesfully authenticated.");
                        authenticated = true;
                    }
                }
            }
            if(authenticated) {
                if (!waiting) {
                    System.out.println("Put your calc: (ex: 2+2)");
                    String nt = sc.nextLine();
                    outputStream.write(new String("op" + nt.trim()).getBytes());
                    waiting = true;
                } else {
                    if(inputStream.available() > 0) {
                        byte[] response = new byte[inputStream.available()];
                        inputStream.read(response);
                        String msg = new String(response);
                        System.out.println(msg);
                        waiting=false;
                    }
                }
            }
        }

    }
}
