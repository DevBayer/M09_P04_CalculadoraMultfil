import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.net.Socket;

/**
 * Created by 23878410v on 08/02/17.
 */
public class ServerWorker extends Thread {

    private ServerData data;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Boolean validated = false;

    public ServerWorker(ServerData data, Socket socket) throws IOException {
        this.data = data;
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
    }

    public void run() {
        while(true){
            try {
                if(inputStream.available() > 0) {
                    byte[] mensaje = new byte[inputStream.available()];
                    inputStream.read(mensaje);
                    String msg = new String(mensaje);
                    if (!validated) {
                        if (msg.equals("init")) {
                            validated = true;
                            sendMsg("info",msg,"authenticated");
                        } else {

                            sendMsg("error",msg,"no authenticated, disconnecting..");
                            break;
                        }
                    } else {// do actions!!
                        if(msg.startsWith("op")){
                            String operation = msg.substring(2, msg.length());
                            ScriptEngineManager manager = new ScriptEngineManager();
                            ScriptEngine se = manager.getEngineByName("JavaScript");
                            try {
                                Object result = se.eval(operation);
                                sendMsg("info","op",result.toString());
                            }catch(ScriptException e){
                                sendMsg("error", msg, "Error when process operation: "+operation);
                                System.out.println("Error when process operation: "+e.getMessage());
                            }
                        }
                    }
                }
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
        try {
            socket.close();
            inputStream.close();
            outputStream.close();
            data.getListSockets().remove(socket);
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    private void sendMsg(String type, String request, String data) throws IOException {
        this.data.Log(type, socket.getInetAddress().getHostAddress(), request, data);
        outputStream.write(data.getBytes());
        outputStream.flush();
    }

}
