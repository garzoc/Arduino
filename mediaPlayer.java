package MARS;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import  javax. swing.JPanel;
import javax.swing.SwingUtilities;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
public class mediaPlayer{
	
	public static void main (String []args){
    NativeInterface.open();
    
    SwingUtilities.invokeLater(new Runnable(){
    	public void run(){
    		JFrame  frame = new  JFrame("video"); 
    		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    		new Thread(new Runnable(){

				@Override
				public void run() {
					while (true){
						//wb.navigate("https://www.youtube.com/watch_popup?v=MXk6sJ8xeTo");
						frame.getContentPane().add(getBrowser(), BorderLayout.CENTER);// TODO Auto-generated method stub
					}
					
				}}).start();
    		
    		frame.setSize(400,400);
    		frame.setVisible(true);
    	
     	}
    	
    });
    NativeInterface.runEventPump();
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
    	@Override
    public void  run(){
    	 NativeInterface.close();//closing Native Interface
    	}
    }));
		
	}
public static JPanel getBrowser(){
	JPanel wbPanel = new JPanel(new BorderLayout());
	JWebBrowser wb = new JWebBrowser();
    wbPanel.add(wb,BorderLayout.CENTER);
    wb.setBarsVisible(true);
    wbPanel.setBackground(Color.BLACK);
   //wb.navigate("https://www.youtube.com/watch_popup?v=MXk6sJ8xeTo");//using popup for fullscreen
    //wb.navigate("http://192.168.43.105");
  // wb.navigate("http://172.20.10.14/cam_pic.php");
	return wbPanel;
}
}
