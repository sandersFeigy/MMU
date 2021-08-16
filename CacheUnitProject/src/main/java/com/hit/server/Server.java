package main.java.com.hit.server;

import main.java.com.hit.services.CacheUnitController;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class communicates with clients and sends them to the HandleRequest class.
 */
public class Server extends java.lang.Object implements Runnable, PropertyChangeListener {
    private ServerSocket server;
    private Socket socket;
    private boolean flag;
    private boolean socketFlag;
    private boolean startFlag;
    private CacheUnitController controller;

    public Server() {
        flag = true;
        socketFlag = true;
        startFlag = false;
        socket = new Socket();
        controller = new CacheUnitController();
        try {
            server = new ServerSocket(12345);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (flag) {
            try {
                socket = server.accept();
                System.out.println("new client start");
                if (!socketFlag) {
                    server.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (socketFlag) {
                HandleRequest<Object> hr = new HandleRequest<>(socket, controller);
                Thread client = new Thread(hr);
                client.start();
            }
        }
    }

    //The function start or determinate the server according to the messages from the Cli.
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue().equals("start")) {
            if (startFlag) {
                return;
            }
            startFlag = true;
            new Thread(this).start();
        } else if (evt.getNewValue().equals("shutdown")) {
            socketFlag = false;
            try {
                socket.close();
                controller.onServerShutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
            flag = false;
        }
    }
}
