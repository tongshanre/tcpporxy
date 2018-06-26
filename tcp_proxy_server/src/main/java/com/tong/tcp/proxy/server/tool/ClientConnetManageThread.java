package com.tong.tcp.proxy.server.tool;

import com.tong.tcp.proxy.bean.ProxyMsg;
import com.tong.tcp.proxy.server.config.ConfigBean;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 处理来自客户端的连接的线程类对象
 */
public class ClientConnetManageThread extends Thread{
    private static final Logger logger = Logger.getLogger(ClientConnetManageThread.class);

    private String uuid;
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public ClientConnetManageThread(Socket socket,String uuid){
        this.socket = socket;
        this.uuid = uuid;
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        ProxyMsg proxyMsg;
        try {
            while((proxyMsg= (ProxyMsg) ois.readObject())!=null){
                switch (proxyMsg.getMsgType()){
                    case REGISTER:{
                        logger.info("received register msg :"+proxyMsg.getMsg());
                        proxyRegister(uuid,proxyMsg);
                    }break;
                }
            }
        } catch (IOException e) {
            logger.error("client connect closed!!");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void proxyRegister(String uuid, ProxyMsg proxyMsg){
        String msg =proxyMsg.getMsg();
        String ip = msg.substring(msg.indexOf(";")+1,msg.lastIndexOf(":"));
        int port = Integer.parseInt(msg.substring(msg.lastIndexOf(":")+1));
        ConfigBean.PORT_2_UUID.put(port,uuid);
        ConfigBean.PORT_ROXY_ITEMS_MAP.put(port,msg);
        new ServerProxyThread(ip,port).start();
    }

    /**
     * 发送消息
     * @param proxyMsg
     */
    public void wrieteAndFlush(ProxyMsg proxyMsg){
        try {
            oos.writeObject(proxyMsg);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
