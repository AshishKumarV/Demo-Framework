package tv.accedo.TVAndroid.Utils;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.testng.annotations.Test;

import tv.accedo.TVAndroid.Common.DeviceHelper;

@Component
public class IOUtil extends BaseUtil{
	
	private static String path = System.getProperty("user.home")+"/Library/Android/sdk";
	public static final Logger LOGGER =  Logger.getLogger(IOUtil.class);
	public static OutputStream os = null;
	public static String event;
	public String grepTerm;
	public 	LinkedHashSet<String> eventList ;
	LogThread logThread ;
	CrashLogs crashLogs = null ;

	
	private static Thread thread = new Thread(new Runnable() {		
		@Override
		public void run() {
			 for(String device : DeviceHelper.devices){
				 try {	 
//				   String command =path+"/platform-tools/adb -s "+device+" shell logcat | grep -i WYNK_ANALYTICS ";
				   String command = path+"/platform-tools/adb -s "+device+" shell logcat";
				  // String command = path+"/platform-tools/adb -s "+device+" | grep --line-buffered \"wynk\"";
				   LOGGER.info(LOG.COMMAND_EXEC + command);
				   
				   Process process = Runtime.getRuntime().exec(command);
				   BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				   
				   String deviceLog;		
				   while (( deviceLog = reader.readLine()) != null) {
					   write(device +" : "+deviceLog+System.getProperty( "line.separator" ),os);   
				   }
				   
				  } catch (IOException e) {
					  LOGGER.info("exception in logging "+ e);
				  }
			   } 	 	
	   }
	  });
	
	
	public Thread eventThread = new Thread(new Runnable(){
		@Override
		public void run() {
			
			try {
			  
				//String command = path+ADBCommands.WYNK_LOGS;
				String command = path+ADBCommands.WYNK_LOGS 
			   + "| grep -i --line-buffered "+ grepTerm;
			   LOGGER.info(LOG.COMMAND_EXEC + command);
			   Process process = Runtime.getRuntime().exec(command);
			   BufferedReader reader = new BufferedReader(new InputStreamReader(
			         process.getInputStream()));

			   String s=reader.readLine();		
			   while ((s) != null) {

				   if(s.contains("FILE_EVENT_QUEUE"))
				   {
				   LOGGER.info("Logs after grep "+s);
				   eventList.add(StringUtils.substringAfter(s, "Event added "));
				   event = s ; 
				   s=reader.readLine();

				   }
			   }
			
		       } catch (IOException e) {
		    	   LOGGER.info("exception in logging "+ e);
			} 
		}
	});
	
	public String getEvent() {
		System.out.println("event is :"+ StringUtils.substringAfter(event, "Event added "));
		return StringUtils.substringAfter(event, "Event added ");
	}
	
	public LinkedHashSet<String> getEventSet() {
		//System.out.println("event is :"+ StringUtils.substringAfter(event, "Event added "));
		return eventList;
	}
	


	public void startThread() {			
		try {
			os = new FileOutputStream(new File(System.getProperty("user.dir")+"/logs/device.txt"));
		} catch (FileNotFoundException e1) {
          LOGGER.info("device.txt file not present at "+System.getProperty("user.dir"));
		}
		
		for(String device:DeviceHelper.devices){
			logThread = new LogThread(device, os);
			new Thread(logThread).start();
		}
	}
	
	public CrashLogs startCrashLogs() {
		try {
			os = new FileOutputStream(new File(System.getProperty("user.dir")+"/logs/crashLogs.txt"),false);
		} catch (FileNotFoundException e1) {
          LOGGER.info("device.txt file not present at "+System.getProperty("user.dir"));
		}
		
		for(String device:DeviceHelper.devices){
			crashLogs=new CrashLogs(device, os);
			new Thread(crashLogs).start();
		}
		return crashLogs;
	}

	
	public void stopThread() throws InterruptedException{
		if(thread.isAlive()){
			LOGGER.info(LOG.THREAD_STOP);
			logThread.interrupt();
		}
		else{
			LOGGER.info(LOG.THREAD_INTERUPPTED);
		}	
	}
	
	
	public void stopCrashLog(CrashLogs crashLogs) throws InterruptedException{
		if(!crashLogs.isInterrupted()){
			LOGGER.info(LOG.THREAD_STOP);
			crashLogs.interrupt();
		}
		else{
			LOGGER.info(LOG.THREAD_INTERUPPTED);
		}	
	}

	
	public void startEventThread(String grepTerm) throws IOException, InterruptedException {
		this.grepTerm=grepTerm;
	    eventList= new LinkedHashSet<String>();
		LOGGER.info("logs to be grepped for term : "+ grepTerm);
		new Thread(eventThread).start();
//		if(eventThread.isInterrupted()){
//			LOGGER.info(LOG.THREAD_START);
//			eventThread.start();
//		}
//		Thread.sleep(2000);
//		if(!eventThread.isInterrupted()){
//			eventThread.interrupt();	
//			eventThread.start();
//		}
		
	}

	
	public void stopEventThread() throws InterruptedException{
		if(!eventThread.isInterrupted()) {
//		    int count = 0;
//		    while(event== null && count !=4) {
//		    	Thread.sleep(4000);
//		    	count++;
//		    }
//		    count =0;
//		    while(!event.contains(eventTerm) && count !=4) {
//		    	Thread.sleep(4000);
//		    	count++;
//		    }
		    LOGGER.info(LOG.THREAD_STOP);
		    eventThread.interrupt();
		  //  LOGGER.info("event fetched : "+ event);
		}
		else{
			eventThread.interrupt();
			LOGGER.info(LOG.THREAD_INTERUPPTED);
		}
		//return event;	
	}
	
	public String stopEventThreadToGetLogs() throws InterruptedException{
		if(!eventThread.isInterrupted()) {
//		    int count = 0;
//		    while(event== null && count !=4) {
//		    	Thread.sleep(4000);
//		    	count++;
//		    }
//		    count =0;
//		    while(!event.contains(eventTerm) && count !=4) {
//		    	Thread.sleep(4000);
//		    	count++;
//		    }
		    LOGGER.info(LOG.THREAD_STOP);
		    eventThread.interrupt();
		   LOGGER.info("event fetched : "+ event);
		}
		else{
			eventThread.interrupt();
			LOGGER.info(LOG.THREAD_INTERUPPTED);
		}
		return event;	
	}
	
	
	
	//Delete file on android device
	public void deleteFile(String fileLoc) throws InterruptedException{
		try {
			executeADBShellCommand("rm -rf "+fileLoc);
		} catch (IOException e) {
			logger.info("error in deleting file on android device at "+fileLoc + e);
		}
	}
	
	//modularize network
	public void setNeworkType(String networkValue) throws IOException, InterruptedException{
		executeADBShellCommand(ADBCommands.NETWORK+networkValue);
		executeADBShellCommand(ADBCommands.REBOOT.toString());
	}
	

	public void captureAndroidDevicesLog(String fileName) throws InterruptedException {
		String ADB=System.getenv("ANDROID_HOME");
		OutputStream os = null;
		
//		for (String device : DeviceHelper.devices)
//		{
		
		try {
			 os =new FileOutputStream(new File(System.getProperty("user.dir")+"/logs/device.txt"));
		} catch (FileNotFoundException e1) {
			logger.info("error in getting file output stream "+e1);
		}
		
		Process process1 = null;
		try {	
			String command =path+"/platform-tools/adb logcat -v threadtime | grep --line-buffered \"wynk\"";
			//String command = path+"/platform-tools/adb -s "+device+" shell cat /sdcard/logcat.txt | grep \"wynk\"";
			logger.info(LOG.COMMAND_EXEC+command);
			process1 = Runtime.getRuntime().exec(command);
			//| grep --line-buffered \"wynk\"
			BufferedReader reader = new BufferedReader(new InputStreamReader(
			process1.getInputStream()));
			String deviceLog;
        
			while ((deviceLog = reader.readLine()) != null ) 
			{
				System.out.println(deviceLog);
				write("device_ID:"+deviceLog,os);
			} 
		}catch (IOException e) {
			logger.info("error in writing device log to system file "+e);
		}
	    finally
        {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		//}
		  
		}
	
	
	public static void write(String data,OutputStream os)
	{
      try 
        {
          os.write(data.getBytes() ,0, data.length());
        } 
        
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    
	}
	
	@Test
	public void clearFile()
	{
		FileWriter fw = null;
		try {
			fw = new FileWriter("/Users/b0205009/os.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrintWriter pw = new PrintWriter(fw);
		pw.write("");
		pw.flush(); 
		pw.close();
	}
	
	public static FileInputStream getFile(File fileImport) throws IOException {
	      FileInputStream fileStream = null;
	      PrintWriter writer = new PrintWriter(fileImport);
	    try {
	        
	        writer.print(StringUtils.EMPTY);
	        fileStream = new FileInputStream(fileImport);
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        } finally {
	            
				writer.close();
	        }
	         return fileStream;
	}
	

	public void captureNUploadLog(S3Helper s3Upload,String fileName )
	{
		try {
			captureAndroidDevicesLog(fileName);
		} catch (InterruptedException e) {
		logger.info("error in capturing android logs to local file"+ e);
		}
		//s3Upload.uploadToS3(fileName);
	}
	
	public int getSizeOfRental()
	{   	
		LinkedHashSet<String> songsCachedSize = new LinkedHashSet<>();
		songsCachedSize = executeADBCommandToFetchData("ls -s /sdcard/\"Wynk Debug\"/rented/");
		List<String> list = new ArrayList<String>(songsCachedSize);
		int size = Integer.parseInt(list.get(0).substring(6));
		
		return size;	
	}
	
	public int getFilesCountOfRental()
	{   	
		LinkedHashSet<String> songsCachedCount = new LinkedHashSet<>();
		songsCachedCount = executeADBCommandToFetchData("ls -l /sdcard/\"Wynk Debug\"/rented/ | wc -l");
		List<String> list = new ArrayList<String>(songsCachedCount);
		
		return Integer.parseInt(list.get(0));	
	}
	
	public int getEventLog() throws InterruptedException{
		Thread.sleep(2000);
		LinkedHashSet<String> eventLog = new LinkedHashSet<>();
		eventLog = executeADBCommandToFetchData("grep --line-buffered SONG_PLAYED | grep -o 'SONG_PLAYED' | wc -l");
		List<String> list = new ArrayList<String>(eventLog);
		
		return Integer.parseInt(list.get(0));
	}
  

}

