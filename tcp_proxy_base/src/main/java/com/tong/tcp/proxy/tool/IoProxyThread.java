package com.tong.tcp.proxy.tool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IoProxyThread extends Thread {

    private InputStream is;
    private OutputStream os;

    public IoProxyThread(InputStream is,OutputStream os){
        this.is = is;
        this.os = os;
    }

    @Override
    public void run() {
        int n = -1;
        byte []buffer = new byte[1024*4];
        try {
            while((n=is.read(buffer))>0){
                os.write(buffer,0,n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
