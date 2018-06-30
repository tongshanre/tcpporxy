package com.tong.tcp.proxy.client.tool;

import com.tong.tcp.proxy.bean.ProxyMsg;
import com.tong.tcp.proxy.client.config.ConfigBean;
import com.tong.tcp.proxy.tool.IoProxyThread;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;
import java.net.Socket;

public class ConnectAndProxy {
    private static final Logger logger = Logger.getLogger(ConnectAndProxy.class);

    private ProxyMsg proxyMsg;

    public  ConnectAndProxy(ProxyMsg proxyMsg){
        this.proxyMsg = proxyMsg;
    }

    public void connectAndProxy() {
        logger.info("client received connect MSG...."+proxyMsg.getMsg());
        String msg = proxyMsg.getMsg();
        int server_port = Integer.parseInt(msg.substring(0,msg.indexOf("#")));
        String connect_ip = msg.substring(msg.indexOf("#")+1,msg.indexOf(":"));
        int connect_port = Integer.parseInt(msg.substring(msg.indexOf(":")+1,msg.indexOf(";")));

        //连接两个服务进行转接
        try {
            Socket localSocket = new Socket();
            localSocket.setKeepAlive(true);
            localSocket.connect(new InetSocketAddress(connect_ip,connect_port));
            Socket remoteSocket = new Socket();
            remoteSocket.setKeepAlive(true);
            remoteSocket.connect(new InetSocketAddress(ConfigBean.SERVER_IP,server_port));
            new IoProxyThread(localSocket.getInputStream(),remoteSocket.getOutputStream()).start();
            new IoProxyThread(remoteSocket.getInputStream(),localSocket.getOutputStream()).start();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("service connect fail.");
            return ;
        }

    }
}
