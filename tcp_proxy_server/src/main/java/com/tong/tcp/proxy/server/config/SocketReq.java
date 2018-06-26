package com.tong.tcp.proxy.server.config;

import java.net.Socket;

/**
 * 保存已建立服务端连接等待客户端连接建立
 */
public class SocketReq {
    private int port;
    private Socket socket;

    public SocketReq(int port, Socket socket) {
        this.port = port;
        this.socket = socket;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public String toString() {
        return "port="+port+"\t";
    }
}
