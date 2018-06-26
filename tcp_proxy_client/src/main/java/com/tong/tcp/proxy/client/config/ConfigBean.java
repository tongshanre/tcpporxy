package com.tong.tcp.proxy.client.config;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigBean {
    static Logger logger = Logger.getLogger(ConfigBean.class);
    static Properties properties=new Properties();

    public static String SERVER_IP;
    public static int SRRVER_PORT;
    public static List<String> PROXY_ITEMS=new ArrayList<String>();
    public static int CONNECT_TIMES;

    private static Pattern pattern = Pattern.compile("\\d+.\\d+.\\d+.\\d+:\\d+;\\d+.\\d+.\\d+.\\d+:\\d+");
    static {
        try {
            properties.load(ConfigBean.class.getClassLoader().getResourceAsStream("config.properties"));

            CONNECT_TIMES=Integer.parseInt(properties.getProperty("server.connect.times","3"));
            SERVER_IP = properties.getProperty("server.ip").trim();
            SRRVER_PORT = Integer.parseInt(properties.getProperty("server.port").trim());
            String []items = properties.getProperty("proxy.items").trim().split("#");
            for (String item: items) {
                if(item.length()>0){
                    Matcher matcher = pattern.matcher(item);
                    if(matcher.find()&& (matcher.group().length()==item.length())){
                        PROXY_ITEMS.add(item);
                    }else{
                        logger.error("proxy.item error. :"+item);
                        System.exit(0);
                    }
                }
            }
            if(PROXY_ITEMS.size()==0){
                logger.error("proxy.items is null!!!");
                System.exit(0);
            }
        } catch (IOException e) {
            logger.error("config.properties missing.!!!");
            System.exit(0);
        }
    }


}
