package com.tong.tcp.proxy.bean;

import java.io.Serializable;

/**
 * 用于服务端和客户端传递消息的实体类对象
 */
public class ProxyMsg implements Serializable{
    private MsgType msgType;
    private String msg;

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
