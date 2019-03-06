package sk.cpd;

import org.java_websocket.WebSocket;

class WebSocketServerHandler implements IServerHandler {

    private WebSocket conn;
    int id;

    public WebSocketServerHandler(WebSocket conn) {
        this.conn = conn;
        this.id = id;
    }

    @Override
    public void sendLine(String newLine) {
        if (conn != null) {
            conn.send(newLine);
        }

    }

    @Override
    public String getId() {
        return String.valueOf(conn.getRemoteSocketAddress());
    }

    @Override
    public boolean equals(Object obj) {
        if( obj instanceof WebSocketServerHandler)
        return ((WebSocketServerHandler)obj).conn.equals(conn);
        else return false;
    }


}
