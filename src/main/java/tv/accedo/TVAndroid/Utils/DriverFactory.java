package tv.accedo.TVAndroid.Utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.exec.ExecuteException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import tv.accedo.TVAndroid.Common.MusicHelper;


@Component
public class DriverFactory {
	protected WebDriver driver;
	public DesiredCapabilities capabilities;
	private AndroidDriver androidDriver;
	//static Logger log = LoggerFactory.getLogger(DriverFactory.class);
	AppiumDriverLocalService appiumService;
	public MusicHelper musicHelper;
	private static final Logger LOGGER =  Logger.getLogger(DriverFactory.class);



	/***********************************************************************************************
	 * Function Description : Initiates the driver
	 *  date: 04-May-2017
	 * @throws MalformedURLException 
	 * *********************************************************************************************/

	public AndroidDriver<WebElement> startDriver(String appLocation) throws MalformedURLException{
		try{
		    startAppiumServer();
			init(appLocation);
			androidDriver = new AndroidDriver<WebElement>(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
			LOGGER.info("\nExecuting Test...\n");
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("App Launch failed");
			LOGGER.info("\nApp launch fail\n");
			LOGGER.info(e.getMessage());
			LOGGER.info("\nRetrying...\n");
			try{
				stopDriver();
				init(appLocation);
				androidDriver = new AndroidDriver<WebElement>(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
				LOGGER.info("Retry Success...Starting execution...\n");
			}
			catch(Exception e1){
				LOGGER.info("\nApp launch fail\n");
				LOGGER.info(e.getMessage());
				LOGGER.info("\nRetrying...\n");
			}
		}
		return androidDriver;

	}
	
	public AndroidDriver<WebElement> startDriverParallel(String url) throws MalformedURLException{
		try{
            System.setProperty("ANDROID_HOME", System.getProperty("user.home")+"/Library/Android/sdk");
			String appLocation= "";
		//	startAppiumServer_v1(StringUtils.substringBetween(url, ":","/wd/hub"));
			init(appLocation);
			androidDriver= new AndroidDriver<WebElement>(new URL(url), capabilities);
			LOGGER.info("\nExecuting Test...\n");
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("App Launch failed");
			LOGGER.info("\nApp launch fail\n");
			LOGGER.info(e.getMessage());
			LOGGER.info("\nRetrying...\n");
			
			try{
				stopDriver();
				driver=  new AndroidDriver<WebElement>(new URL(url), capabilities);
				LOGGER.info("Retry Success...Starting execution...\n");
			}
			catch(Exception e1){
				LOGGER.info("\nApp launch fail\n");
				LOGGER.info(e.getMessage());
				LOGGER.info("\nRetrying...\n");
			}
		}
		return androidDriver;

	}

	/***********************************************************************************************
	 * Function Description : Starts appium server, first stops appium server if any running
	 *  date: 04-May-2017 
	 * @throws IOException 
	 * @throws ExecuteException 
	 * @throws InterruptedException 
	 * *********************************************************************************************/
	public void startAppiumServer() throws ExecuteException, IOException, InterruptedException{
		RuntimeExec appiumObj = new RuntimeExec();
		appiumObj.stopAppium("killall -9 node");
		appiumObj.startAppium("/usr/local/bin/node /usr/local/bin/appium --address 0.0.0.0  --port 4445 --no-reset --command-timeout 90 --backend-retries 2"); 

	}
	
	public void startAppiumServer_v1(String url) throws ExecuteException, IOException, InterruptedException{
		RuntimeExec appiumObj = new RuntimeExec();
		appiumObj.stopAppium("killall -9 node");
		//appiumObj.startAppium("/usr/local/bin/node /usr/local/bin/appium --nodeconfig LenovoConfig.json  --address 0.0.0.0 -p 4725 -bp 4724  --no-reset --command-timeout 90 --backend-retries 2"); 
		appiumObj.startAppium("/usr/local/bin/node /usr/local/bin/appium --address 0.0.0.0 --port "+url+" --no-reset --command-timeout 180 --backend-retries 2"); 
		//  appium --nodeconfig LenovoConfig.json -p 4725 -bp 4724 

	}

	

	public void init(String appLocation){
		capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", "ANDROID");
		//capabilities.setCapability("platformVersion", this.getAndroidPlatformVersion());
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
		capabilities.setCapability("newCommandTimeout", 60 * 3);
		capabilities.setCapability("autoGrantPermissions",false);
		capabilities.setCapability("noReset", "true");
		capabilities.setCapability("appPackage", "tv.accedo.airtel.wynk.debug");
		capabilities.setCapability("appActivity", "tv.accedo.airtel.wynk.presentation.view.activity.SplashActivity");
	}



	/***********************************************************************************************
	 * Function Description : Stops the driver
	 *  date: 04-May-2017
	 * ********************************************************************************************/
	public void stopDriver(){

		try{
			androidDriver.close();
		}catch(Exception e){
			e.printStackTrace();
			
		}
	}


	/***********************************************************************************************
	 * Class Description : Class for providing functions to Start & Stop Appium
	 *  date: 04-May-2017
	 * *********************************************************************************************/	

	private class RuntimeExec {
		public StreamWrapper getStreamWrapper(InputStream is, String type){
			return new StreamWrapper(is, type);
		}
		private class StreamWrapper extends Thread {
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
						buffer.append(line);//.append("\n");
					}
					message = buffer.toString();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}  
		}
		

		public void startAppium(String command) {
			Runtime rt = Runtime.getRuntime();
			RuntimeExec rte = new RuntimeExec();
			StreamWrapper error, output;

			try {
				Process proc = rt.exec(command);
				error = rte.getStreamWrapper(proc.getErrorStream(), "ERROR");
				output = rte.getStreamWrapper(proc.getInputStream(), "OUTPUT");
				//   int exitVal = 0;

				BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				String s;
				while((s = stdInput.readLine()) != null){
					System.out.println(s);
					if(s.contains("Appium REST http")){
						System.out.println("Started Appium Server");
						break;
					}
				}
				error.start();
				output.start();
				error.join(3000);
				output.join(3000);
				// exitVal = proc.waitFor();
				System.out.println("Output: "+output.message+"\nError: "+error.message);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		/***********************************************************************************************
		 * Function Description : It stops appium server
		 *  date: 04-May-2017
		 * *********************************************************************************************/
		public void stopAppium(String command) {
			Runtime rt = Runtime.getRuntime();
			RuntimeExec rte = new RuntimeExec();
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

	
	
	public String getAndroidPlatformVersion(){
		String platformVersion ="";
		//String ADB=System.getenv("ANDROID_HOME");
		String ADB = System.getProperty("user.home")+"/Library/Android/sdk";

		String command = "/platform-tools/adb shell getprop | grep build.version.release";
		try {
			Process process = Runtime.getRuntime().exec(ADB+command);       
			BufferedReader reader=new BufferedReader( new InputStreamReader(process.getInputStream()));
			String s;                
			while ((s = reader.readLine()) != null){   
				String[] platform = StringUtils.substringsBetween(s, "[", "]");
				platformVersion = platform[1];
				LOGGER.info("The platform version is :" + platformVersion);
				}
			  
		} catch (IOException e) {
			e.printStackTrace();
		}
		return platformVersion;
	}
	
	public String deviceName(AndroidDriver driver){
		driver=this.androidDriver;
		String deviceName = driver.getCapabilities().getCapability("deviceName").toString();
		return deviceName;
	}
	
	
	public AndroidDriver<WebElement> getDriver(){
		System.out.println("driver is"+ this.androidDriver);
			return this.androidDriver;
	}
	
	
}
