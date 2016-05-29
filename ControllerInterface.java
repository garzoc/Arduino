package MARS;
import java.awt.event.ActionListener;//wiats for the user to press something
import java.awt.event.ActionEvent;//this are events
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.PrintStream;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.io.OutputStream;
import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.fazecast.jSerialComm.SerialPort;


//import mars.remote.src.Conn;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
public class ControllerInterface extends JFrame implements KeyListener{
	private JPanel Pane;
	private JTextField textField;
//	static OutputStream outputStream;

//@SuppressWarnings("deprecation")
public ControllerInterface(){
	setResizable(true);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//setBounds(205, 187, 290, 250);
	Pane = new JPanel();
	Pane.setBorder(new EmptyBorder(5, 5, 5, 5));
	setContentPane(Pane);
	Pane.setLayout(null);
	requestFocus();
	JComboBox<String> comboBox = new JComboBox<String> ();
	comboBox.setBounds(10, 5, 150, 20);
	for(int i=0; i<Btconnect.getPorts().length; i++){
		comboBox.addItem(((SerialPort) Btconnect.getPorts()[i]).getSystemPortName());
	}
	Pane.add(comboBox);
	//creating text field
	textField = new JTextField();
	textField.addKeyListener(new KeyAdapter() {
		@Override
		public void keyTyped(KeyEvent arg0) {
			switch (arg0.getKeyChar()){
			case 'w': Btconnect.sendMsg('w');
			          textField.setText("");
			  break;
			case 'e': Btconnect.sendMsg('e');
			textField.setText("");
	         break;
			case 'r': Btconnect.sendMsg('r');
			textField.setText("");
	         break;
			
			}
		}
	});
	textField.setBounds(100, 25, 20, 10);
	Pane.add(textField);
	textField.setColumns(10);
	//end
	textField.setEnabled(false);
	
	JButton ButtonConn = new JButton("Connect");
	ButtonConn.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			
			Btconnect.connect(comboBox.getSelectedIndex());
			textField.setEnabled(true);
			Runnable run = new Runnable(){
				
				public  void run(){
					//Btconnect.listen();
				}
			};
			Btconnect.listen = new Thread(run);
			Btconnect.listen.start();
			}
		});
	ButtonConn.setBounds(10, 40, 89, 15);
	Pane.add(ButtonConn);
	 JButton forward = new JButton("forward");
	forward.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
//			Btconnect.disconnect();
			//ttext.field
			forward.setEnabled(true);
			String forward = "c50|0";
			byte[]test=forward.getBytes();
			Btconnect.OutputStream(test);
			try{
				//Btconnect.write(up.getBytes());
			Btconnect.OutputStream(forward.getBytes());
			}catch(NullPointerException io){
		}}
			
	});
	//mainPane.add(forward);
	Pane.setBounds(20, 162, 89, 23);
	JButton ButtonDisconn = new JButton("Disconnect");
	ButtonDisconn.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			Btconnect.disconnect();
			textField.setEnabled(false);
		    forward.setEnabled(false);
		}
	});
	ButtonDisconn.setBounds(10, 25, 89, 15);
	Pane.add(ButtonDisconn);
	
   Pane.setLayout(new BorderLayout());
   Pane.add(panelbutton(), BorderLayout.SOUTH);
   Pane.add(videoPanel(), BorderLayout.CENTER);
   
   
   return ;


}
// panel for control buttons	
private Component panelbutton() {
	// TODO Auto-generated method stub
		JPanel panelbutton = new JPanel();
		 panelbutton.setLayout(new GridBagLayout());
		 GridBagConstraints gbc = new GridBagConstraints();
		//   if(rootPaneCheckingEnabled){
        // gbc.fill = GridBagConstraints.	n	o;
		//   }
		   gbc.insets = new Insets(5,5,5,5);
		   JButton up = new JButton("forward");
		   gbc.gridx = 1;
		   gbc.gridy = 0;
		   panelbutton.add(up,gbc);
		   up.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					up.setEnabled(true);
					String forward = "c50|0";
					byte[]test=forward.getBytes();
					Btconnect.OutputStream(test);

					try{
						//Btconnect.write(up.getBytes());
					//Conn.OutputStream(forward.getBytes());
					}catch(NullPointerException io){
				}}
					
			});
		   JButton down = new JButton("backward");
		   gbc.gridx = 1;
		   gbc.gridy = 2;
		   panelbutton.add(down,gbc);
		   down.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					down.setEnabled(true);
					String down= "c-50|0";
					byte[]test=down.getBytes();
					Btconnect.OutputStream(test);
					}
					
			});
		   JButton horn = new JButton("stop");
		   gbc.gridx = 1;
		   gbc.gridy = 1;
		   panelbutton.add(horn,gbc);
		   horn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					horn.setEnabled(true);
					String horn= "c0|0";
					byte[]test=horn.getBytes();
					Btconnect.OutputStream(test);
					}
					
			});
		   JButton left = new JButton("left");
		   gbc.gridx = 0;
		   gbc.gridy = 1;
		   panelbutton.add(left,gbc);
		   left.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					left.setEnabled(true);
					String left= "c50|-180";//-90
					byte[]test=left.getBytes();
					Btconnect.OutputStream(test);
					}
					
			});
		   JButton right = new JButton("right");
		   gbc.gridx = 2;
		   gbc.gridy = 1;
		   panelbutton.add(right,gbc);
		   right.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					right.setEnabled(true);
					String right= "c50|180";//-90
					byte[]test=right.getBytes();
					Btconnect.OutputStream(test);
					}
					
			});
	return panelbutton;
}


//video panel
private Component videoPanel(){
	JPanel videoPanel = new JPanel();
	videoPanel.setBackground(Color.black);
	 return videoPanel;
}

	@Override
public void keyPressed(KeyEvent e) {
		 if ( e.getID() == KeyEvent.KEY_PRESSED )
		    {
		// TODO Auto-generated method stub
if (e.getKeyCode()==KeyEvent.VK_UP){
	String forward = "c50|0";
	byte[]test=forward.getBytes();
	Btconnect.OutputStream(test);
			//B.Send();
	}else if(e.getKeyCode()==KeyEvent.VK_DOWN){
		String down= "c-50|0";
		byte[]test=down.getBytes();
		Btconnect.OutputStream(test);
	}else if (e.getKeyCode()==KeyEvent.VK_SHIFT){
		String horn= "c0|0";
		byte[]test=horn.getBytes();
		Btconnect.OutputStream(test);
	}else if(e.getKeyCode()==KeyEvent.VK_LEFT){
		String left= "c50|-180";//-90
		byte[]test=left.getBytes();
		Btconnect.OutputStream(test);
	}else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
		String right= "c50|180";//-90
		byte[]test=right.getBytes();
		Btconnect.OutputStream(test);
	}
}}
	

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		String horn= "c0|0";
		byte[]test=horn.getBytes();
		Btconnect.OutputStream(test);
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	

	//main
	public static void main(String[] args) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
			try {
			ControllerInterface frame = new ControllerInterface();
			frame.setVisible(true);
			frame.setSize(300,600);
			//Btconnect.listen();
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}});
		
		}


}
