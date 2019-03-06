/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.cpd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martin
 */
public class SocketServerHandler implements Runnable, IServerHandler {

    private IDataReceived recvInterface;
    private Socket clientSocket;
    private BufferedReader in = null;
    private PrintWriter out = null;
    public String id;

    public SocketServerHandler(Socket clientSocket, IDataReceived recvInt, String id) {
        this.clientSocket = clientSocket;
        this.recvInterface = recvInt;
        this.id = id;
        LOG.log(Level.INFO, "Client ID: {0}", id);
    }
    
    @Override
    public void sendLine(String newLine){
        out.println(newLine); 
    }
    
    @Override
    public void run() {

        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            LOG.log(Level.INFO, "Client connected: {0}", clientSocket.getInetAddress());

            String newLine;
            while ((newLine = in.readLine()) != null) {
                System.out.println(newLine);
                if(recvInterface != null){
                    recvInterface.onReceived(newLine, this);
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(SocketServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static final Logger LOG = Logger.getLogger(SocketServerHandler.class.getName());

    @Override
    public String getId() {
        return String.valueOf(id);
    }

}
