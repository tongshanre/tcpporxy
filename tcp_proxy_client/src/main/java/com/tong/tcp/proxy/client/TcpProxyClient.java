package com.tong.tcp.proxy.client;


import com.tong.tcp.proxy.bean.MsgType;
import com.tong.tcp.proxy.bean.ProxyMsg;
import com.tong.tcp.proxy.client.config.ConfigBean;
import com.tong.tcp.proxy.client.tool.ConnectAndProxy;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Random;

public class TcpProxyClient {
    private static final Logger logger = Logger.getLogger(TcpProxyClient.class);

    /**
     * 服务启动方法
     */
    private void start(){
        new ServerConnectThread().start();
    }

    /**
     * 维持与服务端的通信的线程类
     */
    private class ServerConnectThread extends Thread{
        @Override
        public void run() {
            int connectTimes=1;
            Socket socket=null;
            while (connectTimes<= ConfigBean.CONNECT_TIMES){
                if(connectTimes>1){
                    try {
                        Thread.sleep((new Random().nextInt(10))*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                logger.info("client connect server "+connectTimes++ +" times");
                try {
                    socket = new Socket();
                    socket.setKeepAlive(true);
                    socket.connect(new InetSocketAddress(ConfigBean.SERVER_IP,ConfigBean.SRRVER_PORT));
                    logger.info("client connect server success.");

                    final ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    //发送注册信息
                    logger.info("send register msg:"+ConfigBean.PROXY_ITEMS.size());
                    for (String item:ConfigBean.PROXY_ITEMS) {
                        ProxyMsg proxyMsg = new ProxyMsg();
                        proxyMsg.setMsgType(MsgType.REGISTER);
                        proxyMsg.setMsg(item);
                        oos.writeObject(proxyMsg);
                        oos.flush();
                    }
                    //心跳检测
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                while(true){
                                    Thread.sleep(new Random().nextInt(10)*1000);
                                    ProxyMsg proxyMsg = new ProxyMsg();
                                    proxyMsg.setMsgType(MsgType.HEART);
                                    oos.writeObject(proxyMsg);
                                    oos.flush();
                                    logger.info("heart beep....");
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    //监听输入流
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    ProxyMsg proxyMsg=null;
                    while((proxyMsg= (ProxyMsg) ois.readObject())!=null){
                        switch (proxyMsg.getMsgType()){
                            case CONNECT:{
                                new ConnectAndProxy(proxyMsg).connectAndProxy();
                            }break;
                        }
                    }
                } catch (IOException e) {
                    logger.error("client connect server fail!!!");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    if(socket!=null){
                        try {
                            socket.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
        }
    }


    public static void main(String[] args) {
        new TcpProxyClient().start();
    }
}
