package com.tong.tcp.proxy.server;

import com.tong.tcp.proxy.server.config.ConfigBean;
import com.tong.tcp.proxy.server.tool.ClientConnetManageThread;
import com.tong.tcp.proxy.server.tool.LoopConnectThread;
import org.apache.log4j.Logger;
import sun.security.krb5.Config;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class TcpProxyServer {
    private static final Logger logger = Logger.getLogger(TcpProxyServer.class);

    public void  start(){
        new LoopConnectThread().start();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(ConfigBean.PORT));
            logger.info("TcpProxyServer start...");
            Socket socket;
            while((socket = serverSocket.accept())!=null){
                logger.info("received remote client connect . The client address is '"+socket.getRemoteSocketAddress().toString()+"'");
                String uuid = UUID.randomUUID().toString();
                ClientConnetManageThread clientConnetManageThread = new ClientConnetManageThread(socket,uuid);
                clientConnetManageThread.start();
                ConfigBean.UUID_2_CCMT.put(uuid,clientConnetManageThread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    public static void main(String[] args) {
        new TcpProxyServer().start();
    }
}
