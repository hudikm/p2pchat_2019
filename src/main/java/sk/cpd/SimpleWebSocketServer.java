/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.cpd;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class SimpleWebSocketServer extends WebSocketServer {

    private final IDataReceived iDataReceived;

    private IConnectionChange iConnectionChange;

    public SimpleWebSocketServer(InetSocketAddress address, IDataReceived iDataReceived, IConnectionChange iConnectionChange) {
        super(address);
        this.iDataReceived = iDataReceived;
        this.iConnectionChange = iConnectionChange;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {

        iConnectionChange.newConnection(new WebSocketServerHandler(conn));
        conn.send("Welcome to the chat Room"); //This method sends a message to the new client
        //broadcast( "new connection: " + handshake.getResourceDescriptor() ); //This method sends a message to all clients connected
        System.out.println("new connection to " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
         iConnectionChange.closedConnection(new WebSocketServerHandler(conn));
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("received message from " + conn.getRemoteSocketAddress() + ": " + message);
        iDataReceived.onReceived(message, new WebSocketServerHandler(conn));
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        System.out.println("received ByteBuffer from " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("an error occured on connection " + conn.getRemoteSocketAddress() + ":" + ex);
    }

    @Override
    public void onStart() {
        System.out.println("server started successfully");
    }

    public static void main(String[] args) {

    }

}
