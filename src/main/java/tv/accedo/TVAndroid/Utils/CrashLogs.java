package tv.accedo.TVAndroid.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import tv.accedo.TVAndroid.Report.ExtentReporter;
import tv.accedo.TVAndroid.Report.TakeScreenshot;


public class CrashLogs extends Thread{
	
	private static String path = System.getProperty("user.home")+"/Library/Android/sdk";
	public static final Logger LOGGER =  Logger.getLogger(CrashLogs.class);	
	public String deviceId;
	public OutputStream os = null;
	public boolean exceptionPresent;
	TakeScreenshot takeScreenShot;
	ExtentReporter extentReporter;
	
	public CrashLogs(String deviceId,OutputStream os){
		super(deviceId);
		this.deviceId = deviceId;
		this.os = os;
		
	}

	
	public void run() {
		 try {	 
			   Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			   //String command =path+"/platform-tools/adb -s "+deviceId+" shell logcat | grep -i com.bsbportal ";
			   String command1 = path+"/platform-tools/adb -s "+deviceId+" shell logcat -c ";
			   String command =path+"/platform-tools/adb -s "+deviceId+" shell logcat";
			//  String command =path+"/platform-tools/adb -s "+deviceId+" logcat | grep -i com.bsbportal";
			   LOGGER.info(LOG.COMMAND_EXEC + command);
			   LOGGER.info(LOG.COMMAND_EXEC + command1);
			   Process process1 = Runtime.getRuntime().exec(command1);
			   Process process = Runtime.getRuntime().exec(command);
			   BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			   takeScreenShot = new TakeScreenshot();
			   extentReporter = new ExtentReporter();
			   String deviceLog;		
			   while (( deviceLog = reader.readLine()) != null) {
				   write(deviceId +" : "+deviceLog+System.getProperty( "line.separator" ),os);   
				   if(deviceLog.contains("FATAL EXCEPTION")) {			   
					   exceptionPresent = true;
					  // takeScreenShot.setExceptionFlag(true);
					   extentReporter.setExceptionFlag(true);
				   }
			   }
			   
			  } catch (IOException e) {
				  LOGGER.info("exception in logging "+ e);
			  }		
	}

	
	public static void write(String data,OutputStream os) throws IOException{
		
	      try {
	          os.write(data.getBytes() ,0, data.length());
	        }  catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	

}
