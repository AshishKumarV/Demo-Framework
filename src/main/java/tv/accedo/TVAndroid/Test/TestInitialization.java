package tv.accedo.TVAndroid.Test;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import tv.accedo.TVAndroid.Common.DeviceHelper;
import tv.accedo.TVAndroid.Utils.BaseUtil;
import tv.accedo.TVAndroid.Utils.IOUtil;

@Test
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class TestInitialization extends BaseUtil{
	private static final Logger LOGGER =  Logger.getLogger(TestInitialization.class);
	
	@Autowired
	DeviceHelper deviceHelper ;
	
	@Autowired
	IOUtil iOUtil ;
	

    @BeforeSuite
    @Override 
    protected void springTestContextPrepareTestInstance() throws Exception { 
    	super.springTestContextPrepareTestInstance(); 
    	deviceHelper.initDevices();
    	System.setProperty("ANDROID_HOME", System.getProperty("user.home")+"/Library/Android/sdk");
    	LOGGER.info("time stamp before start of execution : "+ getCurrentTimeStamp());
		iOUtil.startThread();
    }
    
    
    @AfterSuite
    public void afterSuite(ITestContext context)
    {
//    	try {
//			iOUtil.stopThread();
//		} catch (InterruptedException e) {
//	        LOGGER.info("exception in stopping thread "+ e);
//		}
    	LOGGER.info("time stamp after"+ getCurrentTimeStamp());
    	
    }
	
	
	
	@AfterSuite
	public void afterSuite()
	{
	//  	try {
	//			iOUtil.stopThread();
	//		} catch (InterruptedException e) {
	//	        LOGGER.info("exception in stopping thread "+ e);
	//		}
	  	System.out.println("time stamp after"+ getCurrentTimeStamp());
	  	
	  }
	
		
	
	

}
