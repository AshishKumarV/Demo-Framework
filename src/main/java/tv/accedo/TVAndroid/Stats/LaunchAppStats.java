package tv.accedo.TVAndroid.Stats;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import tv.accedo.TVAndroid.Common.CommonFunctions;
import tv.accedo.TVAndroid.Common.DeviceHelper;
import tv.accedo.TVAndroid.Test.PageInit;
import tv.accedo.TVAndroid.Utils.BaseUtil;
import tv.accedo.TVAndroid.Utils.DriverFactory;
import tv.accedo.TVAndroid.Utils.EventUtil;
import tv.accedo.TVAndroid.Utils.IOUtil;
import tv.accedo.TVAndroid.Utils.LOG;
import tv.accedo.TVAndroid.Utils.S3Helper;

@Test
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class LaunchAppStats extends BaseUtil
{
	//public static AndroidDriver<WebElement> androidDriver;
	private static final Logger LOGGER =  Logger.getLogger(LaunchAppStats.class);
	
	DriverFactory driverFactory;
	SoftAssert softAssert;
	
	@Value("${appPackage}")
	private  String appPackage;
	
	@Value("${newAppPath}")
	private String newAppPath;
	
	@Autowired
	S3Helper s3Helper ;
	
	@Autowired
	IOUtil iOUtil ;
	
	@Autowired
	EventUtil eventUtil ;
	
	@Autowired
	CommonStatsFunctions commonStatsFunctions;
	
	@Autowired
	DeviceHelper deviceHelper ;
	
	@Autowired
	CommonFunctions commonFunctions ;
	
	PageInit pageInit;
	public static String deviceName;
	public static String sheetName = "LaunchApp";
	
	@BeforeSuite
    @Override 
    protected void springTestContextPrepareTestInstance() throws Exception { 
    	super.springTestContextPrepareTestInstance(); 
    	deviceHelper.initDevices();
    	System.out.println("time stamp before"+ getCurrentTimeStamp());
		iOUtil.startThread();
    }
	

	
	@Parameters({"url"})
	@BeforeTest
	public void launchApp(@Optional("http://0.0.0.0:4445/wd/hub") String url) throws IOException, InterruptedException
	{
//		executeADBCommand("install "+System.getProperty("user.dir")+newAppPath);
		driverFactory = new DriverFactory();
		try 
		{
			androidDriver=driverFactory.startDriverParallel(url);
			// single execution - 	androidDriver=driverFactory.startDriver(appLocation);
		} 
		catch (MalformedURLException e) { 
			e.printStackTrace();
		}		
		pageInit = new PageInit(androidDriver);	
		deviceName = androidDriver.getCapabilities().getCapability("deviceName").toString();
		MDC.put("deviceName", deviceName);
		
	}
	
	
	@BeforeMethod
	public void beforeMethod(Method method) throws IOException{	
		commonFunctions.clearAllDifferenceColumn(sheetName);
		LOGGER.info(LOG.EXECUTION_STARTED  + method.getName());
	}
	
	@Test(enabled=false)
	public void startStats_v2(String term) throws IOException, InterruptedException {
		iOUtil.startEventThread(term);
	}	
	
	@Test(enabled=true,description="\"APP_INSTALL_NEW\",\"APP_INFO\",\"FCM_REGISTRATION_FAILED\"")
	public void validateLaunchAppAStats() throws IOException, InterruptedException {
		LinkedHashSet<String> eventType = Stream.of("APP_INSTALL_NEW","APP_INFO","FCM_REGISTRATION_FAILED")			
				.collect(Collectors.toCollection(LinkedHashSet::new));
		
		softAssert=new SoftAssert();
		if(androidDriver.isAppInstalled(appPackage)) {
			unInstallApp(appPackage);
		}
		Thread.sleep(2000);
		androidDriver.installApp(System.getProperty("user.dir")+newAppPath);
		startStats_v2("FILE_EVENT_QUEUE");
		androidDriver.launchApp();
		
		boolean flag = commonStatsFunctions.verifyStats(sheetName,eventType);
		softAssert.assertTrue(flag);
		softAssert.assertAll();
	}
	
	@AfterMethod
	public void afterMethod(Method method){	
		LOGGER.info(LOG.EXECUTION_COMPLETED + deviceName +" " + method.getName());
	}
	
	@AfterTest
    public void closeDriver() {
		androidDriver.closeApp(); // Close the app which was provided in the capabilities at session creation   
		androidDriver.quit(); // quits the session
	}
	
}