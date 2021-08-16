package main.java.com.hit.client;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * This class manages client connection to server and treatment of the recurrent reaction.
 */
public class CacheUnitClient {
    private Socket socket;
    private DataOutputStream writer;
    private DataInputStream reader;

    public CacheUnitClient() {

    }

    public String send(java.lang.String request) {
        String content = "";
        try {
            socket = new Socket("127.0.0.1", 12345);
            writer = new DataOutputStream(socket.getOutputStream());
            reader = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            writer.writeUTF(request);
            writer.flush();
            StringBuilder sb = new StringBuilder();
            do {
                content = reader.readUTF();
                sb.append(content);
            } while (reader.available() != 0);
            content = sb.toString();
            System.out.println("message from server: " + content);
            writer.close();
            reader.close();
            socket.close();
        } catch (IOException e) {
            return "failed";
        }
        return content;
    }
}
