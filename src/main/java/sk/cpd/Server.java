/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.cpd;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Martin
 */
public class Server implements IDataReceived, IConnectionChange {

    public static final int WEB_SOCKET_PORT = 8887;
    public static final String HOST = "localhost";
    private static int SERVER_PORT_NUMBER = 8082;
    static Logger logger = Logger.getLogger("Server");


    List<IServerHandler> clientHanders = Collections.synchronizedList(new ArrayList());

    public static void main(String[] args) {

        new Server().runServers();

    }

    int id = 0;

    public void runServers() {

        // Create new websocket server
        logger.log(Level.INFO, "Start websocket server server");
        new SimpleWebSocketServer(new InetSocketAddress(HOST, WEB_SOCKET_PORT), this, this).start();

        logger.log(Level.INFO, "Start socket server");
        ServerSocket serverSocket = null;
        try {
            SimpleSocketServer simpleSocketServer = new SimpleSocketServer(SERVER_PORT_NUMBER, this, this);
            simpleSocketServer.run();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onReceived(String newLine, IServerHandler serverHandler) {
        synchronized (clientHanders) {
            clientHanders.forEach(s -> {
                if (!s.equals(serverHandler)) {
                    s.sendLine(serverHandler.getId() + " : " + newLine);
                }
            });
        }
    }

    @Override
    public void newConnection(IServerHandler iServerHandler) {
        clientHanders.add(iServerHandler);
    }

    @Override
    public void closedConnection(IServerHandler iServerHandler) {
        clientHanders.remove(iServerHandler);
    }

   }
