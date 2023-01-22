package me.kalmemarq.minicraft.server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import me.kalmemarq.minicraft.util.optionparser.ArgOption;
import me.kalmemarq.minicraft.util.optionparser.ArgOptionParser;

public class Main {
    public static void main(String[] args) {
        ArgOptionParser optionParser = new ArgOptionParser();
        ArgOption<Integer> serverPort = optionParser.accepts("serverPort", Integer.class);
        optionParser.parse(args);

        Socket socket = null;
        ServerSocket server = null;
        DataInputStream in =  null;
    
        try {
            server = new ServerSocket(serverPort.getValue());
            System.out.println("Server started");
            System.out.println("Waiting for client");
            socket = server.accept();
            System.out.println("Client accepted");
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            String line = "";

            while (!line.equals("Over")) {
                try {
                    line = in.readUTF();
                    System.out.println(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Closing connection");
        
            socket.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
