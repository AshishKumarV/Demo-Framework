package tv.accedo.TVAndroid.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.testng.annotations.Test;


@Component
public class HubNodeConfig {
	
	String hubIP="0.0.0.0";
	String hubPort = "4445";
	private static final Logger LOGGER =  Logger.getLogger(HubNodeConfig.class);

	
	 /* start selenium grid using standalone jar in resources folder
	  * port = 4444 , ip - localhost
	  */	
	
	//standalone jar chnages
	
	public void startGrid() throws InterruptedException  {
		stopPort(4445);
		String command = "java -jar "+System.getProperty("user.dir")+"/src/main/resources/selenium-server-standalone-3.9.0.jar -host "
				          + ""+hubIP+" -port "+ hubPort +" -role hub";
		try {
				this.runCommand(command);
			} catch (IOException |InterruptedException e){
				LOGGER.info("exception in running command to start selenium grid : "+ e);
			} 
	} 
	
	
	public String getIpAddress(){
		InetAddress ip = null;
		try {
			ip = InetAddress.getLocalHost();
			LOGGER.info("Current IP address : " + ip.getHostAddress());
			return ip.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public void stopPort(int port) throws InterruptedException {
		//String command ="killall -9 $(lsof -n -i:4723 | grep LISTEN | awk '{print $1}')";
		String command1 ="killall $(lsof -n -i:"+port+" | grep LISTEN | awk '{print $1}')";
		try {			
			LOGGER.info("command to kill the port "+ command1);
			//Runtime.getRuntime().exec(command1); 
			this.runCommand(command1);
		} catch (IOException e1) {
			LOGGER.info("Exception in killing  port");
		} 
	}
	

	public void connectNode(String fileName,String nodeIP,String nodePort,String deviceID)  {
		String command="/usr/local/bin/node /usr/local/bin/appium --nodeconfig "+fileName+" -p "+nodePort+" -U "+deviceID+" --no-reset";
		LOGGER.info("connecting appium node to : "+nodePort + "for device id : " +  deviceID);
		//runCommand("killall -9 node");	
		RuntimeExec appiumObj = new RuntimeExec( nodePort, deviceID );
		appiumObj.startAppium(command,nodePort, deviceID );
	}
	
	
	public int getFreePort() throws IOException{
		ServerSocket socket = new ServerSocket(0);
		socket.setReuseAddress(true);
		int port = socket.getLocalPort(); 
		socket.close();
		LOGGER.info("Free Port:"+port);
		return port;
	}

	
	public void runCommand(String command) throws IOException, InterruptedException {
		Process process ;
		try {
		process= Runtime.getRuntime().exec(command);  
		System.out.println("running command "+command);
//		BufferedReader reader=new BufferedReader( new InputStreamReader(process.getInputStream()));
//		String s;                
//		while ((s = reader.readLine()) != null){   
//			LOGGER.info(s);
//			}
		} catch(Exception e) {
			LOGGER.info(e+" line no :"+ Thread.currentThread().getStackTrace()[2].getLineNumber());
		}	
	}

	
	
	private class RuntimeExec {
		String logPort;
		String logDevice;
		public RuntimeExec(String nodePort, String deviceID) {
			this.logDevice= deviceID;
			this.logPort = nodePort;
		}
		public StreamWrapper getStreamWrapper(InputStream is, String type){
			return new StreamWrapper(is, type);
		}
		private class StreamWrapper extends Thread {
			private  final Logger APPIUMLOGS =  Logger.getLogger("StreamWrapper");
			
			InputStream is = null;
			String type = null;
			String message = null;

			StreamWrapper(InputStream is, String type) {
				this.is = is;
				this.type = type;
			}

			public void run() {
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(is));
					StringBuffer buffer = new StringBuffer();
					String line = null;
					while ( (line = br.readLine()) != null) {
					APPIUMLOGS.info("APPIUM LOGS : deviceID : "+ logDevice +"and port : "+ logPort+ " "+line);
						buffer.append(line);//.append("\n");
					}
					message = buffer.toString();
				} catch (IOException ioe) {
					message = ioe.getMessage() ;
				}
			}  
		}
		

		public void startAppium(String command, String nodePort, String deviceID) {
			Runtime rt = Runtime.getRuntime();
			RuntimeExec rte = new RuntimeExec(nodePort,deviceID);
			StreamWrapper error, output;
			
			

			try {
				Process proc = rt.exec(command);
				error = rte.getStreamWrapper(proc.getErrorStream(), "ERROR");
				output = rte.getStreamWrapper(proc.getInputStream(), "OUTPUT");
				//   int exitVal = 0;
				

				BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				String s;
				
				while((s = stdInput.readLine()) != null){
					System.out.println("APPIUM LOGS: "+ s);
					if(s.contains("Appium REST http")){
						System.out.println("Started Appium Server");
						break;
					}
				}
				Thread.sleep(6000);
				error.start();
				output.start();
//				error.join(3000);
//				output.join(3000);
				// exitVal = proc.waitFor();
				System.out.println("Output: "+output.message+"\nError: "+error.message);
			} catch (IOException e) {
				e.printStackTrace();
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		

		public void stopAppium(String command) {
			Runtime rt = Runtime.getRuntime();
			RuntimeExec rte = new RuntimeExec("","");
			StreamWrapper error, output;

			try {
				Process proc = rt.exec(command);
				error = rte.getStreamWrapper(proc.getErrorStream(), "ERROR");
				output = rte.getStreamWrapper(proc.getInputStream(), "OUTPUT");
				error.start();
				output.start();
				error.join(3000);
				output.join(3000);
				if(error.message.equals("") && output.message.equals(""))
					System.out.println("Closed Appium Server");
				else if(error.message.contains("No matching processes belonging to you were found")){
					//Display nothing as no instances of Appium Server were found running
				}
				else{
					System.out.println("Unable to Close Appium Server");
					System.out.println("Output: "+output.message+"\nError: "+error.message);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static Thread thread = new Thread(new Runnable() {		
		@Override
		public void run() {
		 	
	   }
	  });
	
	@Test
	public void test(){
		try {
			stopPort(4445);
//			this.startGrid();
//			this.connectNode("RedmiConfig.json");
//			this.connectNode("LenovoConfig.json");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 
	    }
