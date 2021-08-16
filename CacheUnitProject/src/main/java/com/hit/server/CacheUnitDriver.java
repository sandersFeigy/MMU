package main.java.com.hit.server;

import main.java.com.hit.util.CLI;

class CacheUnitServerDriver extends java.lang.Object  {

    public static void main(String[] args) {
        CLI cli = new CLI(System.in, System.out);
        Server server = new Server();
        cli.addPropertyChangeListener(server);
        new Thread(cli).start();
    }
}


