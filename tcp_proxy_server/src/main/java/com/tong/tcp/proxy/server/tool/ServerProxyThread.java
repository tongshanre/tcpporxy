package com.tong.tcp.proxy.server.tool;

import com.tong.tcp.proxy.server.config.ConfigBean;
import com.tong.tcp.proxy.server.config.SocketReq;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 代理客户端服务进行监听的线程
 */
public class ServerProxyThread extends Thread {
    private static final Logger logger = Logger.getLogger(ServerProxyThread.class);
    private String ip;
    private int port;

    public ServerProxyThread(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            logger.info("server proxy thread start .The address is :"+ip+":"+port);
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(ip, port));
            Socket socket = null;
            while ((socket = serverSocket.accept()) != null) {
                socket.setKeepAlive(true);
                logger.info("custom connect the address is :" + socket.getRemoteSocketAddress().toString());
                SocketReq socketReq = new SocketReq(port, socket);
                ConfigBean.SOCKETREQ_QUEEN.add(socketReq);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }
}
