package tv.accedo.TVAndroid.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.exec.ExecuteException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;
import tv.accedo.TVAndroid.Utils.DriverFactory;

public class ParrallelTest {
	
	public static AndroidDriver<WebElement> androidDriver;
	private static final Logger LOGGER =  Logger.getLogger(ParrallelTest.class);
	DriverFactory driverFactory ;
	
	@Parameters({"url","udid"})
	@Test
	public void launchApp(String url,String udid) throws ExecuteException, IOException, InterruptedException
	{
		driverFactory = new DriverFactory();		
		String appLocation = "";
		
		try {

			driverFactory.startAppiumServer_v1(StringUtils.substringBetween(url, "http://0.0.0.0:","/wd/hub"));
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability("deviceName", "ANDROID");
			capabilities.setCapability("udid", udid);
			capabilities.setCapability("platformVersion", driverFactory.getAndroidPlatformVersion());
			capabilities.setCapability("platformName", "Android");
			capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
			capabilities.setCapability("autoGrantPermissions",false);
			capabilities.setCapability("appPackage", "tv.accedo.airtel.wynk.debug");
			//capabilities.setCapability("appPackage", "com.bsbportal.music");
			capabilities.setCapability("appActivity", "tv.accedo.airtel.wynk.presentation.view.activity.SplashActivity");
			capabilities.setCapability("noReset", "true");
			androidDriver = new AndroidDriver<WebElement>(new URL(url), capabilities);
			
		} catch (MalformedURLException e) { 
			e.printStackTrace();
		}
		
        
   }

}
