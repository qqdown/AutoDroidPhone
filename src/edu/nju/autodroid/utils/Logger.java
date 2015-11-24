package edu.nju.autodroid.utils;
/**
 * @author machiry, ��Dynodroid�л�ȡ��
 *
 */
import java.io.File;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is the public class for all logging functionality
 * 
 * @author machiry
 */
public abstract class Logger {

	private static PrintStream logobject = System.out;
	private static boolean isInitialized = false;
	private static final String infoPrefix = "INFO:";
	private static final String errorPrefix = "ERROR:";
	private static final String exceptionPrefix = "EXCEPTION:";
	private static final String warningPrefix = "WARNING:";
	private static final SimpleDateFormat DateFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	
	/***
	 * Use this method to initialize the logging functionality
	 * @param logFileName the target name of the log file where all the logging goes
	 */
	public static final synchronized void initalize(String logFileName) {
		if (!isInitialized) {
			if (logFileName != null) {
				try {
					logobject = new PrintStream(new File(logFileName));
					isInitialized = true;
				} catch (Exception e) {
					logobject = System.out;
				}
			} else{
				logobject = System.out;
			}
		}
	}
	
	/***
	 *Use this method to end logging, this ensures all the streams are righly closed
	 */

	public static final synchronized void endLogging() {
		if (logobject != System.out && logobject != null) {
			try {
				logobject.close();
			} catch (Exception e) {

			}
		}
		isInitialized = false;

	}

	/***
	 * This is the method that is used to log the provided msg as info
	 * @param msg target message that needs to be logged
	 */
	public static final void logInfo(String msg) {
		writeMsg(infoPrefix + msg);
	}

	/***
	 * Use this to log error message
	 * @param msg target message that needs to be logged
	 */
	public static final void logError(String msg) {
		writeMsg(errorPrefix + msg);
	}

	/***
	 * Use this to log exception 
	 * @param msg target Message
	 */
	public static final void logException(String msg) {
		writeMsg(exceptionPrefix + msg);
	}
	
	/***
	 * This method is to log the provided exception
	 * @param e the target exception that needs to be logged
	 */
	public static final synchronized void logException(Exception e) {
		if (!isInitialized) {			
			System.out.println(exceptionPrefix + " Stack Trace:");
			e.printStackTrace(System.out);
			System.out.println(e.getMessage());
			System.out.flush();
		} else {
			e.printStackTrace(System.out);
			e.printStackTrace(logobject);
			logobject.println(DateFORMAT.format(new Date()) + "\t" + e.getMessage());
			logobject.flush();
		}
	}

	/***
	 * This method is to log msg as warning
	 * @param msg the target message that needs to be logged
	 */
	public static final void logWarning(String msg) {
		writeMsg(warningPrefix + msg);
	}

	private static final synchronized void writeMsg(String msg) {
		msg = DateFORMAT.format(new Date()) + "\t" + msg;
		if (!isInitialized) {
			System.out.printf("%s\n", msg);
			System.out.flush();
		} else {
			System.out.printf("%s\n", msg);
			System.out.flush();
			logobject.printf("%s\n", msg);
			logobject.flush();
		}
	}	
}