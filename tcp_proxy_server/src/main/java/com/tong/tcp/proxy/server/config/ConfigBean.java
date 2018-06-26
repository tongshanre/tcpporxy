package com.tong.tcp.proxy.server.config;

import com.tong.tcp.proxy.server.tool.ClientConnetManageThread;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConfigBean {
    static Logger logger = Logger.getLogger(ConfigBean.class);
    static Properties properties = new Properties();

    public static int PORT;
    public static int CLIENT_PORT;
    public static HashMap<Integer,String> PORT_ROXY_ITEMS_MAP = new HashMap<Integer, String>();
    public static HashMap<Integer,String> PORT_2_UUID = new HashMap<Integer, String>();
    public static HashMap<String,ClientConnetManageThread> UUID_2_CCMT=new HashMap<String, ClientConnetManageThread>();

    public static LinkedBlockingQueue SOCKETREQ_QUEEN = new LinkedBlockingQueue();
    static {
        try {
            properties.load(ConfigBean.class.getClassLoader().getResourceAsStream("config.properties"));
            PORT = Integer.parseInt(properties.getProperty("port", "10101").trim());
            CLIENT_PORT = Integer.parseInt(properties.getProperty("client.port", "10102").trim());
        } catch (IOException e) {
            logger.error("config.properties missing.!!!");
            System.exit(0);
        }
    }
}
