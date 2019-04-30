package tv.accedo.TVAndroid.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.log4j.Logger;

public class LogThread extends Thread{
	
	private static String path = System.getProperty("user.home")+"/Library/Android/sdk";
	public static final Logger LOGGER =  Logger.getLogger(LogThread.class);	
	public String deviceId;
	public OutputStream os = null;
	
	public LogThread(String deviceId,OutputStream os){
		super(deviceId);
		this.deviceId = deviceId;
		this.os = os;
	}

	
	public void run() {
		 try {	 
			   String command =path+"/platform-tools/adb -s "+deviceId+" shell logcat | grep -i WYNK ";
			   LOGGER.info(LOG.COMMAND_EXEC + command);
			   
			   Process process = Runtime.getRuntime().exec(command);
			   BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			   
			   String deviceLog;		
			   while (( deviceLog = reader.readLine()) != null) {
				   write(deviceId +" : "+deviceLog+System.getProperty( "line.separator" ),os);   
			   }
			   
			  } catch (IOException e) {
				  LOGGER.info("exception in logging "+ e);
			  }		
	}

	
	public static void write(String data,OutputStream os){
	      try {
	          os.write(data.getBytes() ,0, data.length());
	        }  catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	

}
