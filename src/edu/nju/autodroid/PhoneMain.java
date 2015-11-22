package edu.nju.autodroid;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

import edu.nju.uiautomator.UiAutomatorHelper;


public class PhoneMain extends UiAutomatorTestCase {
	public static final String TAG = "client";  
    public static int PHONE_PORT = 22222; 
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String jarName = "autodroidclient";
		String testClass = "edu.nju.autodroid.Client";
		String testName = "testMain";
		String androidId = "6";
		new UiAutomatorHelper(jarName, testClass, testName, androidId);
	}
	
	public void testDemo() throws UiObjectNotFoundException{
		UiObject btn = new UiObject(new UiSelector().index(0)).getChild(new UiSelector().index(0)).getChild(new UiSelector().index(1)).getChild(new UiSelector().index(0)).getChild(new UiSelector().index(0));
		System.out.println(btn.getText());
	}


	public void testMain() throws IOException, UiObjectNotFoundException{
		Socket client = null;
		ServerSocket server = new ServerSocket(PHONE_PORT);
		
		
		while(true){
			System.out.println("Waiting for new server!");
			try {
				client = server.accept();
				System.out.println("Get a connection from "  
	                    + client.getRemoteSocketAddress().toString());
				ObjectInputStream ois = new ObjectInputStream(  
	                    client.getInputStream());  
				
				ObjectOutputStream oos = new ObjectOutputStream(  
	                    client.getOutputStream());
				while(true){  
					Command cmd = (Command)ois.readObject();
	                System.out.println("Get command " + cmd.cmd);
	                Command backCmd = CommandHandler.Handle(cmd);
	                oos.writeObject(backCmd);
	                System.out.println("Send command " + backCmd.cmd);
				}	
			} catch (Exception e){
				e.printStackTrace();
				System.out.println("client error " + e.getMessage());
			}
		}
	}
}
