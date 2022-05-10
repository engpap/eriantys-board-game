package it.polimi.ingsw.triton.launcher;

import it.polimi.ingsw.triton.launcher.server.Server;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) {
        Server server;
        int port = 50535;

        try {
            for(int i = 0; i< args.length; i++){
                if(args.length >= 2 && args[i].equals("--port"))
                    port = Integer.parseInt(args[i + 1]);
            }
        } catch (NumberFormatException e) {
            Server.LOGGER.severe("Invalid port. Using default port");
        }

        server = new Server(port);
        try {
            server.run();
        } catch (IOException e) {
            System.err.println("Impossible to initialize the server: " + e.getMessage() + "!");
        }
        Server.LOGGER.info("Server listening on port " + Server.PORT);
    }
}
