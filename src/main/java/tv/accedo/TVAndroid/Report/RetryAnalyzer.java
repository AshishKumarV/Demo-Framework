package tv.accedo.TVAndroid.Report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import tv.accedo.TVAndroid.Utils.DriverFactory;

public class RetryAnalyzer implements IRetryAnalyzer {
	
	static Logger log = LoggerFactory.getLogger(RetryAnalyzer.class);
	
	private int MAX_RETRY_COUNT = 1;
	private int currentTest=-1;

	AtomicInteger count = new AtomicInteger(MAX_RETRY_COUNT);

	public String getPropertyValue(String propertyName) {

		String directory = System.getProperty("user.dir");

		String propFileName = directory+"/config.properties";

		File file = new File(propFileName);

		FileInputStream fileInput = null;

		try {
			fileInput = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Properties prop = new Properties();

		try {
			prop.load(fileInput);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String propertyValue = prop.getProperty(propertyName);

		return propertyValue;
	}

	public boolean isRetryAvailable() {
		return (count.intValue() > 0);
	}


//	@Override
	public boolean retry(ITestResult result) {

		String configRetry= getPropertyValue("retry");
		log.info("Retry = "+configRetry);

		if(configRetry.equals("true")){
			if(TestUtil.getId(result)!=currentTest){
				setCurrentTest(result);
				count = new AtomicInteger(MAX_RETRY_COUNT);
			}
			log.info("RETRY Available :: "+isRetryAvailable());
			boolean retry = false;
			if (isRetryAvailable()) {
				log.info("Going to retry test case: " + result.getMethod() + ", " + (MAX_RETRY_COUNT - count.intValue() + 1) + " out of " + MAX_RETRY_COUNT);
				retry = true;
				count.decrementAndGet();
			}
			return retry;
		}
		else{
			return false;
		}
	}

	private void setCurrentTest(ITestResult result) {
		currentTest= TestUtil.getId(result); 

	}
}