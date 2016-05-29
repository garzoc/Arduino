
//original code by Deepak Saini

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.net.*;
public class Main extends Canvas {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Map map;
	int windowWidth, windowHeight;
	
	
	
	
	public Main(int windowWidth,int windowHeight) {
		enableEvents(java.awt.AWTEvent.KEY_EVENT_MASK);
		this.map=new Map(25,4,1,10,windowWidth,windowHeight);
		
		for(int i=-180;i<180;i++){
			this.map.detectorRotation=i;
	
			this.map.detection(25);

		}
//		int angle=0;
//		double temp;
//		angle =(int) (angle*((temp=Math.pow(10,(int)(Math.log10(angle)+2)))==0?1:temp));
//		angle+=((int)'7'-48);
//		System.out.println("angle is "+angle);
//		int angle=0;
//		double temp;
//		System.out.println(((temp=Math.pow(10,(int)(Math.log10(angle)+2)))==0?1:temp));
//		//angle+=((int)'1'-48);

		new Thread(new Runnable(){
			@Override
			public void run() {
				openServer();
			}	
		}).start();
		
		Timer t = new Timer(true);
		t.schedule(new java.util.TimerTask() {
			//The game loop scheduled with a timer.
			public void run() {
				doSomething();
				repaint();
			}
		}, 0, 30);
	}
	
	
	public void openServer(){
		try {
			ServerSocket incomincSocket = new ServerSocket(6666);
			System.out.println("nej");
			Socket clientSocket = incomincSocket.accept();
			System.out.println("hej");
			byte[] output=new byte[15];
			//int n=0;
			while(clientSocket!=null){		
//				output=(n+"&24|").getBytes();
//				n++;
				byte outputByte=' ';
				int nBytes=0;
				while(outputByte!='|'){
					outputByte=(byte) clientSocket.getInputStream().read();
					if((int)outputByte!=13&&(int)outputByte!=10){
						//System.out.println((char)outputByte);
						output[nBytes]=outputByte;
						nBytes++;
					}
				}
				//clientSocket.getInputStream().read(output);	
				int angle=0;
				int distance=0;
				int logOffset=0;
				boolean proAngle=true;
				for(int i=0;i<output.length;i++){
					if(output[i]=='|')break;
					if(output[i]=='&'){ proAngle=!proAngle; i++; logOffset=2-i;}
					if(proAngle){
						logOffset=2-i;
						double temp;
						//System.out.println((output[i]&0xff));
						angle =(int) (angle*((temp=Math.pow(10,(int)(Math.log10(angle)+logOffset)))==0?1:temp));
						angle+=((int)output[i]-48);
						
					}else{
						double temp;
						//System.out.println("i is "+i+" and logOff is "+logOffset);
						distance =(int) (distance*((temp=Math.pow(10,(int)(Math.log10(distance)+(2-(logOffset-2)-i))))==0?1:temp));
						//System.out.println("distance is"+distance);
						distance+=((int)output[i]-48);
						
					}
				}
				
				this.map.detectorRotation=angle;
				this.map.detection(distance);
				//System.out.println("angle "+angle+" distance "+distance);
			}	
			System.out.println("Shutting down");
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, windowWidth, windowHeight);
		this.map.draw(g);
		this.map.drawDetector(g);
		//this.map.check(65);
		
		/*g.setColor(Color.black);
		g.fillOval(10, 10, 20, 20);
		//player 1
		g.setColor(Color.blue);
		g.fillRect(100, 100, 50, 10);
		//player2
		g.setColor(Color.red);
		g.fillRect(200, 200, 50, 10);*/

		//g.drawString("Player 2 Score : " + p2 + "Points ", 100, 200);
		//g.drawString("Player 1 Score : " + p1 + "Points ", 100, 230);
	}

	//This gets called only when a key is pressed
	public void processKeyEvent(KeyEvent e) {
		if (e.getID() == KeyEvent.KEY_PRESSED) {
			if (e.getKeyCode() == KeyEvent.VK_S ) {
				this.map.changeView(0, 1);
			}

			if (e.getKeyCode() == KeyEvent.VK_A ) {
				this.map.changeView(-1, 0);
			}

			if (e.getKeyCode() == KeyEvent.VK_D) {
				this.map.changeView(1, 0);
			}

			if (e.getKeyCode() == KeyEvent.VK_W ) {
				this.map.changeView(0, -1);
				//System.out.println("hej");
			}
		}
	}

	public void doSomething() {
		
	}

	public boolean isFocusable() {
		return true;
	}

	public static void main(String[] args) {
		final JFrame frame = new JFrame("MARS-MAP");

		Toolkit tool = frame.getToolkit();
		Dimension screenSize = tool.getScreenSize();
		int width = 380;
		int height = 500;
		
		frame.setBounds((screenSize.width / 3), (screenSize.height / 4), width, height);
//		ImageIcon img = new ImageIcon("frameIcon.GIF");
//		frame.setIconImage(img.getImage());
		//frame.setSize(375, 500);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new Main(width,height));
		SetLook.lookAndFeel();
		frame.setVisible(true);
	
	}
}

class SetLook {
	public static void lookAndFeel() {
		try {
			String s = UIManager.getSystemLookAndFeelClassName();
			UIManager.setLookAndFeel(s);
		} catch (Exception e) {
		}
	}
}