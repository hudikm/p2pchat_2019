package sk.cpd;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleSocketServer extends ServerSocket implements Runnable {

    private IConnectionChange iConnectionChange;
    private IDataReceived iDataReceived;

    public SimpleSocketServer(int port,IDataReceived iDataReceived,IConnectionChange iConnectionChange) throws IOException {
        super(port);
        this.iConnectionChange = iConnectionChange;
        this.iDataReceived = iDataReceived;
    }

    @Override
    public void run() {
        try {
            for (; ; ) {
                Socket clientSocket = this.accept();
                SocketServerHandler serverHandler = new SocketServerHandler(clientSocket, iDataReceived, clientSocket.getRemoteSocketAddress().toString());
                iConnectionChange.newConnection(serverHandler);// Add new socket handler
                new Thread(serverHandler).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
