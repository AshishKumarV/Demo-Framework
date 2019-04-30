package tv.accedo.TVAndroid.Common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import tv.accedo.TVAndroid.Utils.BaseUtil;
public class MusicHelper extends BaseUtil {

private WebDriver androidDriver;
public AndroidDriver<MobileElement> driver;	

public MusicHelper(WebDriver driver)
	{
	//	super(driver); 
		this.driver = (AndroidDriver<MobileElement>) driver;
	}
	
	public List<String> getConnectedDevicesList(){

		List<String> devicesID = new ArrayList<String>();

		String command = "adb devices";
		try {
			Process process = Runtime.getRuntime().exec(command);       
			
			BufferedReader reader=new BufferedReader( new InputStreamReader(process.getInputStream()));
			String s;                
			while ((s = reader.readLine()) != null){         
				if(s.contains("device") && ! s.contains("attached")){
					String[] device = s.split("\t");
					devicesID.add(device[0]);
				}
			}  
		} catch (IOException e) {
			e.printStackTrace();
		}
		return devicesID;
	}
	
	
	
	/***********************************************************************************************
	 * Function Description : Sets implicit Wait by accepting timeout in seconds
	 *  date: 04-May-2017
	 * *********************************************************************************************/

	public String setImplicitWaitInSeconds(int timeOut){
		driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
		return "Timeout set to "+timeOut+" seconds.";
	}

	
	
}
