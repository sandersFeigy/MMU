package main.java.com.hit.util;


import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.Locale;
import java.util.Scanner;

/**
 * This class implements command line interface with to command ,Start or shutdown is listeners.
 */
public class CLI implements Runnable {
    private Scanner reader;
    private DataOutputStream writer;
    private PropertyChangeSupport lisenres;

    public CLI(InputStream in, OutputStream out) {
        reader = new Scanner(in);
        writer = new DataOutputStream(out);
        lisenres = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(java.beans.PropertyChangeListener pcl) {
        lisenres.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(java.beans.PropertyChangeListener pcl) {
        lisenres.removePropertyChangeListener(pcl);
    }

    public void write(java.lang.String string) {
        try {
            writer.writeUTF(string);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        write("please enter your command\n");
        String cliCommand;
        while (true) {
            cliCommand = reader.nextLine().toUpperCase(Locale.ROOT);
            switch (cliCommand) {
                case "STOP":
                case "EXIT":
                    write("shutdown\n");
                    lisenres.firePropertyChange("command", null, "shutdown");
                    reader.close();
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                case "START":
                    write("starting...\n");
                    lisenres.firePropertyChange("command", null, "start");
                    break;
                default:
                    write("not a valid command ,please try again\n");
                    break;
            }
        }
    }
}

