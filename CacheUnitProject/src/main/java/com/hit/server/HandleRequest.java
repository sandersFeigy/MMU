package main.java.com.hit.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.java.com.hit.dm.DataModel;
import main.java.com.hit.services.CacheUnitController;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This class accepts the clients from the server and handles their requests.
 * Type parameters:
 * <T> – Generic type of the requests body.
 */
public class HandleRequest<T> implements Runnable {
    private Socket socket;
    private final CacheUnitController cacheUnitController;
    private String req;
    private Request<DataModel<T>[]> request;
    private DataInputStream reader;
    private DataOutputStream writer;

    public HandleRequest(java.net.Socket s, CacheUnitController<T> controller) {
        socket = s;
        cacheUnitController = controller;
        try {
            reader = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            writer = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            try {
                req = reader.readUTF();
            } catch (IOException e) {
                try {
                    socket.close();
                    System.out.println("client closed");
                    break;
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            Type ref = new TypeToken<Request<DataModel<T>[]>>() {
            }.getType();
            request = new Gson().fromJson(req, ref);
            String action = request.toString();
            if (action.equals("update")) {
                try {
                    writer.writeUTF(String.valueOf(cacheUnitController.update(request.getBody())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (action.equals("get")) {
                try {
                    ArrayList<Object> data = cacheUnitController.get(request.getBody());
                    data.set(0, new Gson().toJson(data.get(0)));
                    writer.writeUTF(String.valueOf(data));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    writer.writeUTF(String.valueOf(cacheUnitController.delete(request.getBody())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // cacheUnitController.onServerShutdown();
        try {
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
