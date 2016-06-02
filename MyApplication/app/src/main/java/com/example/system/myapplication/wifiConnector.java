package com.example.system.myapplication;

import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.widget.ImageView;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;



/**
 * @authors Emanuel Mellbom, John Sundling
 * This class handles the connection to the RaspberryPi over wifi
 *
 * info TCP: http://developer.android.com/reference/java/net/Socket.html
 * info UDP: http://developer.android.com/reference/java/net/DatagramSocket.html
 */

public class wifiConnector extends MainActivity{
        String Wifiadress;
        int Wifiport;
        //VideoView videoView;
    ImageView imageView;


    public wifiConnector(String adress, int port){
        Wifiadress = adress;
        Wifiport = port;
        //imageView = img;
    }


    boolean connected = false;
    String IpAdress;
    int raspberryPort = 6666;
    Socket socket;
    BufferedReader in = null;
    int port;

    public void setIP(String ip){
       IpAdress = ip;
    }


    private InetAddress setInetaddres(){
        InetAddress inetaddres = null;
        try {
            inetaddres = InetAddress.getByName(IpAdress);
        }catch (IOException io){
        }
        return inetaddres;
    }


    private Socket declareSocket(){
        try {
            socket = new Socket(setInetaddres(), 6666);
        }catch (IOException io){
            System.out.println(io);
        }
        return socket;
    }



    public void closeSocket(){
        try {
            socket.close();
        }catch (IOException io){
        }
    }

    public void connect(String ip, int port){
        new Thread(new Runnable() {
            @Override
            public void run() {
        InetSocketAddress distanceAdress = new InetSocketAddress(IpAdress,6666);
        try {
            declareSocket();
            connected = true;
            readTheChar();
        }
        catch (NullPointerException nu){
            System.out.println(nu);
        }
            }
        }).start();
    }

    public void readTheChar(){
            BufferedReader read;
            List<Integer> ints = new ArrayList<Integer>();
            boolean test = false;
            int pos = 0;
            if(socket.isConnected()) {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                while (true) {
                   byte[] newBytes =  inData(buffer);
                    if(newBytes.length>0) {
                        buffer = new ByteArrayOutputStream();
                        Bitmap im = BitmapFactory.decodeByteArray(newBytes, 0, newBytes.length);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            //theImageView.setImageDrawable(theimage);
                            }
                        });
                    }
                    if(!socket.isConnected()){
                        break;
                    }
                }
            }
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
            buff.close();
            return buff.toByteArray();
       }catch (IOException io){
           System.out.println(io);
       }
        return buff.toByteArray();
    }
}





