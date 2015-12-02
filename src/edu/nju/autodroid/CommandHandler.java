package edu.nju.autodroid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;

import android.os.Environment;
import edu.nju.autodroid.utils.Logger;

public class CommandHandler {
	public static Command Handle(Command cmd){
		Command backCmd = new Command();
		backCmd.cmd = cmd.cmd;
		switch(backCmd.cmd){
		case Command.cmdPressHome:
			backCmd.params = new String[1];
			backCmd.params[0] = Boolean.toString(pressHome());
			break;
		case Command.cmdPressBack:
			backCmd.params = new String[1];
			backCmd.params[0] = Boolean.toString(pressBack());
			break;
			
		case Command.cmdGetLayout:
			backCmd.params = new String[1];
			backCmd.params[0] = getLayout();
			break;
		case Command.cmdGetActivity:
			backCmd.params = new String[1];
			backCmd.params[0] = getActivity();
			break;
		case Command.cmdGetPackage:
			backCmd.params = new String[1];
			backCmd.params[0] = getPackage();
			break;
			
		case Command.cmdDoClick:
			backCmd.params = new String[1];
			if(doClick(cmd.params[0]))
				backCmd.params[0] = "true";
			else
				backCmd.params[0] = "false";
			break;
		case Command.cmdDoSetText:
			backCmd.params = new String[1];
			if(doSetText(cmd.params[0], cmd.params[1]))
				backCmd.params[0] = "true";
			else
				backCmd.params[0] = "false";
			break;
		case Command.cmdDoLongClick:
			backCmd.params = new String[1];
			if(doLongClick(cmd.params[0]))
				backCmd.params[0] = "true";
			else
				backCmd.params[0] = "false";
			break;
		case Command.cmdDoScrollBackward:
			backCmd.params = new String[1];
			if(doScrollBackBackward(cmd.params[0], Integer.parseInt(cmd.params[1])))
				backCmd.params[0] = "true";
			else
				backCmd.params[0] = "false";
		case Command.cmdDoScrollForward:
			backCmd.params = new String[1];
			if(doScrollForward(cmd.params[0], Integer.parseInt(cmd.params[1])))
				backCmd.params[0] = "true";
			else
				backCmd.params[0] = "false";
		default:
			backCmd.cmd = Command.cmdUnknown;
		}
		
		return backCmd;
	}
	
	//0x0001
	private static boolean pressHome(){
		return UiDevice.getInstance().pressHome();
	}
	
	private static boolean pressBack(){
		return UiDevice.getInstance().pressBack();
	}
	
	//0x1001
	private static String getLayout(){
		
		//simulator-local/tmp/dump.xml    real device-/local/tmp/local/tmp/dump.xml
		File dumpFile = new File(Environment.getDataDirectory().getAbsolutePath() + "/local/tmp/dump.xml");
		System.out.println("dumpFile " + dumpFile.getAbsolutePath() + " | " + dumpFile.getName());
		if(!dumpFile.getParentFile().exists())
			dumpFile.getParentFile().mkdirs();
		if(!dumpFile.exists())
			try {
				dumpFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		String layoutStr = null;
		try {
			UiDevice.getInstance().dumpWindowHierarchy(dumpFile.getName());
			 Long filelength = dumpFile.length();     //鑾峰彇鏂囦欢闀垮害
             byte[] filecontent = new byte[filelength.intValue()];
             FileInputStream fis = new FileInputStream(dumpFile);
             fis.read(filecontent);
             fis.close();
             layoutStr = new String(filecontent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return layoutStr;
	}
	
	//0x1002
	private static String getActivity(){
		return UiDevice.getInstance().getCurrentActivityName();
	}
	
	//0x1003
	private static String getPackage(){
		return UiDevice.getInstance().getCurrentPackageName();
	}
	
	//0x2001
	private static boolean doClick(String btnPath){
		UiObject btn = getObject(btnPath);
		if(btn == null)
			return false;
		try {
			return btn.click();
		} catch (UiObjectNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	//0x2002
	private static boolean doSetText(String xPath, String content){
		UiObject obj = getObject(xPath);
		if(obj == null)
			return false;
		try {
			return obj.setText(content);
		} catch (UiObjectNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private static boolean doLongClick(String xPath){
		UiObject obj = getObject(xPath);
		if(obj == null)
			return false;
		try {
			return obj.longClick();
		} catch (UiObjectNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	//steps 每步5ms。默认为55
	private static boolean doScrollBackBackward(String xPath, int steps){	
		UiObject obj = getObject(xPath);
		if(obj == null)
			return false;
		try {
			UiScrollable scroll = new UiScrollable(obj.getSelector());
			return scroll.scrollBackward(steps);
		} catch (UiObjectNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private static boolean doScrollForward(String xPath, int steps){
		UiObject obj = getObject(xPath);
		if(obj == null)
			return false;
		try {
			UiScrollable scroll = new UiScrollable(obj.getSelector());
			return scroll.scrollForward(steps);
		} catch (UiObjectNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static UiObject getObject(String indexXPath){
		String[] indexStrs = indexXPath.split(" ");
		int[] indexes = new int[indexStrs.length];
		for(int i=0; i<indexes.length; i++){
			indexes[i] = Integer.parseInt(indexStrs[i]);
		}
		UiObject obj = new UiObject(new UiSelector().index(indexes[0]));
		for(int i=1; i<indexes.length; i++){
			try {
				obj = obj.getChild(new UiSelector().index(indexes[i]));
			} catch (UiObjectNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		if(!obj.exists())
			return null;
		return obj;
	}
}
