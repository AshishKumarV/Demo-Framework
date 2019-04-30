package tv.accedo.TVAndroid.Test;




import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import tv.accedo.TVAndroid.Common.DeviceHelper;
import tv.accedo.TVAndroid.Page.CommonPages;
import tv.accedo.TVAndroid.Page.SplashScreenPage;
import tv.accedo.TVAndroid.Test.PageInit;
import tv.accedo.TVAndroid.Utils.BaseUtil;
import tv.accedo.TVAndroid.Utils.DriverFactory;
import tv.accedo.TVAndroid.Utils.EventPOJO;
import tv.accedo.TVAndroid.Utils.IOUtil;
import tv.accedo.TVAndroid.Utils.LOG;
import com.google.gson.Gson;

//@Test
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class StartPlayer extends BaseUtil {
	
	private static final Logger LOGGER =  Logger.getLogger(StartPlayer.class);
	DriverFactory driverFactory;

	@Autowired
	IOUtil iOUtil ;

	@Autowired
	DeviceHelper deviceHelper ;

	@Value("${latestHindiRail}")
	private String latestHindiRail;
	
	@Value("${msisdndownload}")
	private String msisdn;
	
	@FindBy(id = "actionBtnD")
	public WebElement register;
	
	@FindBy(id = "you_receive_pin")
	public WebElement mobOTP;
	
	@FindBy (id = "edit_phone_number")
	public WebElement enterMobNo;
	
	@FindBy (id = "btn_next")
	public WebElement continueBut;
	
	@FindBy (id = "you_receive_pin")
	public WebElement receiveOPT;
	
	@FindBy (id = "edit_pin_code")
	public WebElement enterOPT;
	
	@FindBy (id = "verify_btn")
	public WebElement verifyBut;

	PageInit pageInit;
	public static String deviceName;
	public static String sheetName = "Player&Streaming";

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
	public void launchApp(@Optional("http://0.0.0.0:4445/wd/hub") String url){
		driverFactory = new DriverFactory();
		try {
			androidDriver=driverFactory.startDriverParallel(url);
		} 
		catch (MalformedURLException e) { 
			e.printStackTrace();
		}		
		pageInit = new PageInit(androidDriver);	
		deviceName = androidDriver.getCapabilities().getCapability("deviceName").toString();
		System.out.println("deviceName : " + deviceName);
		MDC.put("deviceName", deviceName);
		androidDriver.resetApp();
		try {
			launchAppAndRegister(pageInit.splashScreenPage, pageInit.commonPages, msisdn, deviceName);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@AfterMethod
	public void afterMethod(Method method) {	

	}
	
	@Test
	public void liveTVVideoPlay() throws IOException, InterruptedException {
		LinkedHashSet<String> eventTypes = Stream.of("play_init","play_start","play_status").collect(Collectors.toCollection(LinkedHashSet::new));
		
		setQuality("High");
		selectChannelAndPlay("sab");
		clearLogcat();
		startStats("FILE_EVENT_QUEUE");
		//androidDriver.findElement(By.xpath("//android.widget.TextView[contains(@text,'Show info not available')]")).click();
		androidDriver.findElement(By.xpath("//android.support.v7.widget.RecyclerView[@index='1']/android.widget.FrameLayout[@index='0']")).click();			
		Thread.sleep(90000);
		LinkedHashSet<String> eventLogsSet = iOUtil.getEventSet();
		iOUtil.stopEventThread();
		String eventLogData= null;	
		EventPOJO eventLogsObj=null;
		Gson gson = new Gson();
		for(String eventType : eventTypes){
			eventLogData= getEventFromLogs(eventType, eventLogsSet);
			System.out.println("Starting evaluation for Event "+ eventType +" logs : "+ eventLogData);			
			eventLogsObj = gson.fromJson(eventLogData,EventPOJO.class);
			System.out.println(eventLogsObj.getEventType() + " --> " + eventLogsObj.getTimestamp() + " --> " + eventLogsObj.getMeta().getNetwork_Type());
			if(eventType.equals("play_status")) {
				System.out.println("Manifest_selected_bitrate -> "+eventLogsObj.getMeta().getManifest_selected_bitrate());
				System.out.println("Rebuffer_count -> "+eventLogsObj.getMeta().getRebuffer_count());
				System.out.println("Rebuffer_time -> "+eventLogsObj.getMeta().getRebuffer_time());
				System.out.println("Playback_bandwidtht -> "+eventLogsObj.getMeta().getPlayback_bandwidtht());
				System.out.println("Bitrate -> "+eventLogsObj.getMeta().getBitrate());
			}
			//writeExcel("SongPlay",timeStamp, row, col++);
		}
	}
	
	public void launchAppAndRegister(SplashScreenPage splash, CommonPages common, String msisdn, String deviceId) throws Exception{
		
		//Permission Allow
		splash.launchAndDirectlyGoToHomePage(deviceId);
		Thread.sleep(5000);
		//Registration 
		androidDriver.findElement(By.id("actionBtnD")).click();
		Thread.sleep(2000);
		androidDriver.findElement(By.id("edit_phone_number")).sendKeys("1234123412");
		androidDriver.findElement(By.id("btn_next")).click();
		Thread.sleep(2000);
		androidDriver.findElement(By.id("edit_pin_code")).sendKeys("1234");
		androidDriver.findElement(By.id("verify_btn")).click();
		Thread.sleep(2000);
		androidDriver.findElement(By.id("confirmBtn")).click();
		androidDriver.navigate().back();
		Thread.sleep(2000);
	}
	
	public void setQuality(String quality) throws InterruptedException {
		//Set quality from Setting
		androidDriver.findElement(By.xpath("//android.widget.ImageButton[contains(@content-desc,'Open navigation drawer')]")).click();
		Thread.sleep(2000);
		androidDriver.findElement(By.xpath("//android.widget.TextView[contains(@text,'Settings')]")).click();
		androidDriver.findElement(By.id("quality_selection_heading")).click();
		androidDriver.findElement(By.xpath("//android.widget.RadioButton[contains(@text,'"+quality+"')]")).click();
		androidDriver.findElement(By.id("quality_dialog_confirm")).click();
		androidDriver.navigate().back();
	}
	
	public void selectChannelAndPlay(String channel) throws InterruptedException {
		//Redirect to LiveTV section and select any channel
		androidDriver.findElement(By.xpath("//android.widget.TextView[contains(@text,'Live TV')]")).click();
		
		androidDriver.findElement(By.id("search_button")).click();
		androidDriver.findElement(By.id("search_src_text")).sendKeys(channel);
		androidDriver.pressKeyCode(66);
		Thread.sleep(5000);
	}	


	

	public void startStats(String term) throws IOException, InterruptedException {
		iOUtil.startEventThread(term);
	}

	public void clearLogcat() {
		String command = "adb logcat -c ";
		String ADB = System.getProperty("user.home") + "/Library/Android/sdk/platform-tools/";
		LOGGER.info(LOG.COMMAND_EXEC + command);
		try {		
			Process process = Runtime.getRuntime().exec(ADB+command);
			process.waitFor();
		} catch (IOException | InterruptedException e) {
        } 
	}
	
	public String getEventFromLogs(String eventType,LinkedHashSet<String> eventLogs) {
		String eventLogData = null;
		for(String eventLog :eventLogs) {
			//System.out.println(eventLog);
			if(eventLog.contains(eventType+"\"")&& !eventLog.contains(":\"ad_")) {
				eventLogData= eventLog;
				break;
			}
		}
		return eventLogData;
	}
	
	public void writeExcel(String sheetName,String time,int rowNo, int colNo) throws IOException{
		LOGGER.info("writing into file");
	    FileInputStream fis = new FileInputStream(new File(System.getProperty("user.dir")+"/src/main/resources/"+"ClickToPlay.xlsx"));
		XSSFWorkbook workbook = new XSSFWorkbook (fis);
		XSSFSheet sheet = workbook.getSheet(sheetName);
		Row row = sheet.getRow(rowNo);
		row.getCell(colNo).setCellType(0);
		row.createCell(colNo, CellType.NUMERIC).setCellValue(time);	
		sheet.autoSizeColumn(colNo);
		
		fis.close();
		FileOutputStream fos =new FileOutputStream(System.getProperty("user.dir")+"/src/main/resources/"+"ClickToPlay.xlsx");
	    workbook.write(fos);
	    fos.close();	    
	    LOGGER.info("writing into excel is completed");	
	 }

}