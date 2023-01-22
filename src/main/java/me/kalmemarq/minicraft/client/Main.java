package me.kalmemarq.minicraft.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import me.kalmemarq.minicraft.util.Util;
import me.kalmemarq.minicraft.util.optionparser.ArgOption;
import me.kalmemarq.minicraft.util.optionparser.ArgOptionParser;

public class Main {
    public static void main(String[] args) {
        Util.Logging.setupCustomOutputs();

        ArgOptionParser optionParser = new ArgOptionParser();
        ArgOption<String> serverIp = optionParser.accepts("serverIp", String.class);
        ArgOption<Integer> serverPort = optionParser.accepts("serverPort", Integer.class);
        optionParser.parse(args);

        Socket socket = null;
        DataInputStream input = null;
        DataOutputStream out = null;

        try {
            socket = new Socket(serverIp.getValue(), serverPort.getValue());
            System.out.println("Connected");

            input = new DataInputStream(System.in);

            out = new DataOutputStream(socket.getOutputStream());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String line = "";

        while (!line.endsWith("Over")) {
            try {
                line = input.readLine();
                out.writeUTF(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            input.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
