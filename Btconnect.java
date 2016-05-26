package MARS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.imageio.IIOException;

import com.fazecast.jSerialComm.*;
public class Btconnect {
	static OutputStream outputStream;

static	PrintWriter out;
static 	SerialPort[] ports;
static 	SerialPort port;
static  BufferedReader in;
static  BufferedReader sysIn;
static Thread listen;




	
	public static  SerialPort[] getPorts(){
	sysIn=new BufferedReader(new InputStreamReader(System.in));
	ports = SerialPort.getCommPorts();
	return ports;
}
  public static void sendMsg(char c){//send a message to the connected device 
	out.println(c);
}
public static void connect(int i){//create a connection from the selected port
	port = ports[i];
	System.out.println(port.getInputStream());
	
	try {
		port.openPort();
		port.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
		out = new PrintWriter(port.getOutputStream(),true);
		in = new BufferedReader(new InputStreamReader(port.getInputStream()));
	} catch (NullPointerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
//@SuppressWarnings("deprecation")
public static void disconnect(){
	
	try{
		port.closePort();
		out.close();
		in.close();
	}catch(Exception e){
		System.err.println("Couldnt close the streams");
	}
 listen.stop();
}
public static void listen() throws IOException{//listen to the input coming from the connected device and print it out to the developer console P.S: you should start this in a new thread
//String l="";
//	while((l=in.readLine())!=null){
//		System.out.println(l);
//	}
//}
	Scanner data;
	try {
		data = new Scanner(port.getInputStream());
		while(data.hasNextLine()){
			System.out.println(data.nextLine());
			
		}
	} catch (NullPointerException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
	}

}
//public static void send(){
//	Scanner send;
//	try{
//		send = new Scanner(port.getOutputStream(),true);
//	}catch(Exception io){
//		
//	}
//}

public static void OutputStream(byte[] bytes) {
	// TODO Auto-generated method stub
	try {
		port.isOpen();
		java.io.OutputStream s =   port.getOutputStream();
		s.write(bytes);
		s.close();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
	}
}

}
