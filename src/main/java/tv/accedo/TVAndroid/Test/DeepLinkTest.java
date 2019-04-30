package tv.accedo.TVAndroid.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedHashSet;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import tv.accedo.TVAndroid.Common.DeviceHelper;
import tv.accedo.TVAndroid.Utils.BaseUtil;
import tv.accedo.TVAndroid.Utils.DriverFactory;
import tv.accedo.TVAndroid.Utils.IOUtil;

@Test
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class DeepLinkTest extends BaseUtil{
	
	private static Logger LOGGER = Logger.getLogger(DeepLinkTest.class);
	public static String filePath = System.getProperty("user.dir")+"/src/main/resources/"+"DeepLink.xlsx";
	//DeepLinkUtil deeplink = new DeepLinkUtil(androidDriver);
	DriverFactory driverFactory;
	ITestResult result;

	@Autowired
	DeviceHelper deviceHelper;
	PageInit pageInit;
	LinkedHashSet<String> songsInPlayList;
	LinkedHashSet<String> songsInSelectionPage;
	LinkedHashSet<String> cachedFiles;
	public static String deviceName;

	@Value("${appPackage}")
	private String appPackage;

	
	@Autowired
	IOUtil iOUtil ;
	
	@BeforeSuite
	@Override 
	protected void springTestContextPrepareTestInstance() throws Exception { 
		super.springTestContextPrepareTestInstance(); 
		deviceHelper.initDevices();
		//System.setProperty("ANDROID_HOME", System.getProperty("user.home")+"/Library/Android/sdk");
		LOGGER.info("time stamp before start of execution : "+ getCurrentTimeStamp());
		iOUtil.startThread();
	}

	
	@Parameters({"url"})
	@BeforeTest
	public void launchApp( @Optional("http://0.0.0.0:4445/wd/hub")String url){
		driverFactory = new DriverFactory();
		try {
			androidDriver=driverFactory.startDriverParallel(url);
		} catch (MalformedURLException e) { 
			e.printStackTrace();
		}		
		pageInit = new PageInit(androidDriver);	
		deviceName = androidDriver.getCapabilities().getCapability("deviceName").toString();
		MDC.put("deviceName", deviceName);
	}
	
	
	@DataProvider(name="deepLinkData")
    public static Object[][] userFormData() throws Exception{
        Object[][] data = readData(filePath, "DeepLinks");
        return data;
        //return DeepLinkUtil.readData(filePath, "DeepLinks");
    }

	
	@Test (dataProvider="deepLinkData", priority=1, description="TC_001")
	public void validateDeepLinkTest(String url, String expectedData) throws Exception {
		SoftAssert softAssert= new SoftAssert();
		openAppDeeplinks(url);
		softAssert.assertTrue(verifyTextIsVisibleOnPage(expectedData));		
		softAssert.assertAll();		
//		androidDriver.runAppInBackground(Duration.ofSeconds(2));
	}
	
	
	@Test (dataProvider="deepLinkData", priority=2, description="TC_002")
	public void validateBackGroundDeepLinkTest(String url, String expectedData) throws IOException, InterruptedException {
		SoftAssert softAssert= new SoftAssert();
		backGroungApp();
		openAppDeeplinks(url);
		softAssert.assertTrue(verifyTextIsVisibleOnPage(expectedData));
		softAssert.assertAll();
	}
	
	
	@Test (dataProvider="deepLinkData", priority=3, description="TC_003")
	public void validateKillAppDeepLinkTest(String url, String expectedData) throws IOException, InterruptedException {
		SoftAssert softAssert= new SoftAssert();
		killApp(appPackage);
		openAppDeeplinks(url);
		softAssert.assertTrue(verifyTextIsVisibleOnPage(expectedData));
		softAssert.assertAll();
	}
	
	  
	@AfterMethod
	public void afterMethod(ITestResult result){
	    try{
	    	if(result.getStatus() == ITestResult.SUCCESS){
	    		Object[] link = result.getParameters();
	    		System.out.println("http://www.wynk.in/music" + link[0] + " : PASSED");
	    	}else if(result.getStatus() == ITestResult.FAILURE){
	    		Object[] link = result.getParameters();
	    		System.out.println("http://www.wynk.in/music" + link[0] + " : FAILED");
	    	}else if(result.getStatus() == ITestResult.SKIP ){
	    		Object[] link = result.getParameters();
	    		System.out.println("http://www.wynk.in/music" + link[0] + " : SKIPED");
	    	}
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	}
	

	
//	public boolean verifyTextIsVisibleOnPage(String text) {	
//		//System.out.println(androidDriver.getPageSource());
//		WebElement ele=androidDriver.findElement(By.xpath("//*[@text='"+text+"']"));
//		WaitForElement(androidDriver, ele);
//		boolean isDisplay;
//		try {
//			isDisplay=ele.isDisplayed();
//			return isDisplay;
//		}catch(NoSuchElementException e) {
//			isDisplay=false;
//			return isDisplay;
//		}
//	}
//	
//	
//	public void openAppDeeplinks(String targetType) throws IOException, InterruptedException {
//		executeADBShellCommand("am start -n " + getPropertyValue("/config.properties", "musicAppPackage") + "/"
//				+ getPropertyValue("/config.properties", "appActivity") + " -d " + "\"android-app://"
//				+ getPropertyValue("/config.properties", "musicAppPackage") + "/http://www.wynk.in/music"
//				+ targetType + "\"");
//		Thread.sleep(5000);
//	}
//	
//	
//	public void killApp(String appPackage) throws IOException, InterruptedException {
//		executeADBShellCommand("am force-stop " + appPackage);
//	}
//	
//	
//	public void backGroungApp() {
//		androidDriver.closeApp();
//	}
//	
//	
//	@SuppressWarnings("resource")
//	public static Object[][] readData(String filePath, String sheetName) throws IOException {
//		String testData[][]=null;
//		try {
//			File myFile = new File(filePath);
//		    FileInputStream fis = new FileInputStream(myFile);
//		    XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
//		    XSSFSheet mySheet = myWorkBook.getSheet(sheetName);
//		    Iterator<Row> rowIterator = mySheet.iterator();
//		    int rows = mySheet.getLastRowNum();
//		    int columns = mySheet.getRow(0).getLastCellNum();
//		    testData = new String[rows][columns];
//		    rowIterator.next();
//		    for(int i=1; i<rows; i++){
//		    	Row row = rowIterator.next();
//	            for(int j=0; j<columns; j++){          	
//	            	testData[i-1][j] = row.getCell(j).toString();
//	            }
//	        }
//		}catch(Exception e) {			
//		}
//		return testData;
//	}	
}

