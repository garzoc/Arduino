package com.example.system.myapplication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

/**
 * Created by System on 2016-05-17.
 */
public class MapDataReciever {
    private static Socket socket;

    public static void connect(final String ip, final int port){
        new Thread(new Runnable() {
            @Override
            public void run() {
                InetSocketAddress distanceAdress = new InetSocketAddress(ip,port);
                try {
                    declareSocket();
                    sendInitaialData();
                    try {
                        socket.connect(socket.getRemoteSocketAddress());
                    }catch (IOException io){}
                }
                catch (NullPointerException nu){
                    System.out.println("null in connect");
                }
            }
        }).start();
    }

    public static Socket declareSocket(){
        try {
            socket = new Socket(InetAddress.getByName("172.20.10.5"), 6666);
            System.out.println("Socket connected = " + socket.isConnected());
        }catch (IOException io){
            System.out.println("No socket created");
            System.out.println(io);
        }
        return socket;
    }



    public byte[] inData(ByteArrayOutputStream buff){
        try {
            boolean waitingConnection = false;
            while(true){
                if(socket.getInputStream().available() <= 0 && waitingConnection) {
                    break;
                }
                waitingConnection = true;
                int ch = socket.getInputStream().read();
                buff.write(ch);
            }
            System.out.println("finished");
            buff.close();
            return buff.toByteArray();
        }catch (IOException io){
            System.out.println(io);
        }
        return buff.toByteArray();
    }

    public static void sendInitaialData(){
        try {
            while(true) {
                String output = "";
                char inputChar = ' ';
                while(inputChar!='|') {
                    inputChar = (char)BtConnection.btInputStream.read();
                    output = output.concat("" + inputChar);
                }
                    System.out.println(output);
                socket.getOutputStream().write(output.getBytes());
            }
        }catch (IOException io){
        }
    }
}
