package com.tong.tcp.proxy.server.tool;

import com.tong.tcp.proxy.bean.MsgType;
import com.tong.tcp.proxy.bean.ProxyMsg;
import com.tong.tcp.proxy.server.config.ConfigBean;
import com.tong.tcp.proxy.server.config.SocketReq;
import com.tong.tcp.proxy.tool.IoProxyThread;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 循环建立连接的线程
 */
public class LoopConnectThread extends Thread {

    private static final Logger logger=Logger.getLogger(LoopConnectThread.class);

    @Override
    public void run() {
        logger.info("Loop Thread start...");
        SocketReq socketReq = null;
        try {
            while ((socketReq = (SocketReq) ConfigBean.SOCKETREQ_QUEEN.take()) != null) {
                logger.info("recieved  a socketReq."+socketReq.toString());
                //发送消息至客户端
                String uuid = ConfigBean.PORT_2_UUID.get(socketReq.getPort());
                ClientConnetManageThread ccmt = ConfigBean.UUID_2_CCMT.get(uuid);
                ProxyMsg proxyMsg = new ProxyMsg();
                proxyMsg.setMsgType(MsgType.CONNECT);
                proxyMsg.setMsg(ConfigBean.CLIENT_PORT+"#"+ConfigBean.PORT_ROXY_ITEMS_MAP.get(socketReq.getPort()));
                ccmt.wrieteAndFlush(proxyMsg);
                //等待客户端连接
                ServerSocket serverSocket = null;
                try {
                    serverSocket = new ServerSocket();
                    serverSocket.bind(new InetSocketAddress(ConfigBean.CLIENT_PORT));
                    logger.info("listenService start..");
                    Socket socket = serverSocket.accept();
                    logger.info("client connect this listenService.");
                    socket.setKeepAlive(true);
                    new IoProxyThread(socket.getInputStream(),socketReq.getSocket().getOutputStream()).start();
                    new IoProxyThread(socketReq.getSocket().getInputStream(),socket.getOutputStream()).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("Loop Thread return");
    }
}
