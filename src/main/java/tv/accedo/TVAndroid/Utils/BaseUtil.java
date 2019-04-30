package tv.accedo.TVAndroid.Utils;

import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.PointOption.point;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import tv.accedo.TVAndroid.Page.CommonPages;

import android.content.Intent;
import android.net.ParseException;
import android.util.EventLogTags.Description;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.android.StartsActivity;

//extends AbstractTestNGSpringContextTests
public class BaseUtil extends AbstractTestNGSpringContextTests {
	private WebDriver driver;
	private int time = 5;
	public AppiumDriver<?> appium;
	public AndroidDriver<WebElement> androidDriver;
	private static final Logger LOGGER = Logger.getLogger(BaseUtil.class);
	LinkedHashSet<String> songTitle;
	HashSet<String> fileSet;

	@Value("${waitTime}")
	private long waitTime;

	@FindBy(id = "floating_action_button")
	public WebElement keypadIcon;

	public BaseUtil() {
	}

	public BaseUtil(AndroidDriver androidDriver) {
		this.androidDriver = androidDriver;
		appium = (AppiumDriver<?>) androidDriver;
	}

	public AndroidDriver getDriver() {
		return androidDriver;
	}
	

	public String deviceName(String className) {
		String deviceName = null;
		try {
			Field device = Class.forName(className).getDeclaredField("deviceName");
			deviceName = (String) device.get(className.getClass());

		} catch (NoSuchFieldException | SecurityException | ClassNotFoundException | IllegalArgumentException
				| IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String deviceName =
		// driver.getCapabilities().getCapability("deviceName").toString();
		return deviceName;
	}

	// public String deviceName(AndroidDriver driver){
	// driver=this.androidDriver;
	// String deviceName =
	// driver.getCapabilities().getCapability("deviceName").toString();
	// return deviceName;
	// }
	//

	/***********************************************************************************************
	 * Function Description : It returns the value for the provided key from
	 * properties file date: 19-April-2018
	 *********************************************************************************************/
	public String getPropertyValue(String propertyFileName, String propertyName) {

		String directory = getUserDirectoryPath();
		String propFileName = directory + propertyFileName;

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

	/***********************************************************************************************
	 * Function Description : It returns the directory path for the current
	 * project date: 19-April-2018
	 *********************************************************************************************/
	public String getUserDirectoryPath() {
		return System.getProperty("user.dir");
	}

	/***********************************************************************************************
	 * Function Description : It returns the directory path for the current
	 * project date: 19-April-2018
	 *********************************************************************************************/

	public int getDeviceOsVersion(String deviceId) {
		String ADB = System.getenv("ANDROID_HOME");

		String command = "/platform-tools/adb shell getprop ro.build.version.sdk";
		// String command = "/platform-tools/adb -s "+deviceId+" shell getprop
		// ro.build.version.sdk";

		String os = "";

		try {
			Process process = Runtime.getRuntime().exec(ADB + command);
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			os = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int osVersion = Integer.parseInt(os);
		return osVersion;
	}

	public String getAndroidPlatformVersion() {
		String platformVersion = "";
		String ADB = System.getenv("ANDROID_HOME");
		String command = "/platform-tools/adb shell getprop | grep build.version.release";
		try {
			Process process = Runtime.getRuntime().exec(ADB + command);
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String s;
			while ((s = reader.readLine()) != null) {
				String[] platform = StringUtils.substringsBetween(s, "[", "]");
				platformVersion = platform[1];

				LOGGER.info("The platform version is :" + platformVersion);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return platformVersion;
	}

	public WebElement getElementWhenVisible(WebElement locator) {
		WebElement we = null;

		WebDriverWait wait = new WebDriverWait(androidDriver, time);
		we = wait.until(ExpectedConditions.elementToBeClickable(locator));

		return we;
	}

	public void clickOnWebElement(AndroidDriver<WebElement> androidDriver, WebElement element) {
		this.androidDriver = androidDriver;
		WaitForElement(androidDriver, element);
		element.click();
	}

	public void type(AndroidDriver androidDriver, WebElement element, String value) {
		this.androidDriver = androidDriver;
		WaitForElement(androidDriver, element);
		WebElement we = getElementWhenVisible(element);
		if (we != null) {
			we.clear();
			we.sendKeys(value);
		}
	}

	public WebElement findElement(By locator) {
		WebElement we = null;
		we = androidDriver.findElement(locator);
		return we;
	}

	public By songs_in_queue_xpath(int index) {
		//// UIAApplication[1]/UIAWindow[1]/UIATableView[2]/UIATableCell[1]
		String songs_xpath_1 = "//UIAApplication[1]/UIAWindow[1]/UIATableView[2]/UIATableCell[";
		String songs_xpath_2 = "]";
		String songs_xpath = songs_xpath_1 + index + songs_xpath_2;
		System.out.println(songs_xpath);
		By songxpath = By.xpath(songs_xpath);
		return songxpath;

	}

	public void lockDevice(int time_in_seconds) {
		lockDevice(time_in_seconds);
	}

	public boolean isElementDisplayed(AndroidDriver androidDriver, WebElement element) {
		this.androidDriver = androidDriver;
		boolean element_displayed_status = false;
		WebElement we = getElementWhenVisible(element);
		if (we != null) {
			element_displayed_status = true;
			return element_displayed_status;
		}
		return element_displayed_status;
	}

	/**
	 * Verify is element enabled
	 * 
	 * 
	 * @param locator
	 * @param objName
	 * @return
	 */
	public boolean isElementEnabled(AndroidDriver androidDriver, WebElement saveLanguageSetting) {
		this.androidDriver = androidDriver;
		boolean isEnabled = false;
		WebElement we = getElementWhenVisible(saveLanguageSetting);
		// boolean print = screenPrint.length > 0 ? screenPrint[0] : false;
		/*
		 * if(we == null){ /*if(print) Reporter.log("Validate " +
		 * objName.toLowerCase(), objName + " should be enabled", objName +
		 * " is not enabled", "Fail"); return isEnabled; }
		 */
		isEnabled = we.getAttribute("enabled").trim().equalsIgnoreCase("true") ? true : false;
		if (isEnabled)
		/*
		 * if(print) Reporter.log("Validate " + objName.toLowerCase(), objName +
		 * " should be enabled", objName + " is enabled successfully", "Pass");
		 * else if(print) Reporter.log("Validate " + objName.toLowerCase(),
		 * objName + " should be enabled", objName + " is not enabled", "Fail");
		 */
		{
			return isEnabled;
		}
		return isEnabled;
	}

	public void scrollingToBottomofAPage(AndroidDriver androidDriver) {
		this.androidDriver = androidDriver;
		((JavascriptExecutor) androidDriver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}

	public WebElement scrollToElementByVisibleTextAndClick(AndroidDriver androidDriver, String text) {
		this.androidDriver = androidDriver;
		try {
			Thread.sleep(1500);
			WebElement scrolledToElement = androidDriver.findElementByAndroidUIAutomator(
					"new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\""
							+ text + "\").instance(0))");
			scrolledToElement.click();
			return scrolledToElement;
		} catch (NoSuchElementException | InterruptedException e) {

			LOGGER.info(text + "element not found");
			return null;
		}

	}

	public WebElement scrollToElementByVisibleTextAndClick_V2(AndroidDriver androidDriver, String text) {
		this.androidDriver = androidDriver;
		WebElement scrolledToElement = null;

		long startTime = System.currentTimeMillis();
		while ((System.currentTimeMillis() - startTime) <= 10000 && scrolledToElement == null) {
			try {

				scrolledToElement = androidDriver.findElementByAndroidUIAutomator(
						"new UiScrollable(new UiSelector().scrollable(true).instance(0)).setMaxSearchSwipes(3).scrollIntoView(new UiSelector().textContains(\""
								+ text + "\").instance(0))");

			} catch (Exception e) {
				LOGGER.info(text + "element not found");
				scrolledToElement = androidDriver.findElementByAndroidUIAutomator(
						"new UiScrollable(new UiSelector().scrollable(true).instance(0)).setMaxSearchSwipes(3).scrollIntoView(new UiSelector().textContains(\""
								+ text + "\").instance(0))");
			}

		}

		scrolledToElement.click();
		return scrolledToElement;

	}

	/**
	 * Add one more code for scrolling Commented code by shubham verma one
	 * function is commet
	 */
	public WebElement scrollToElementByVisibleText(AndroidDriver androidDriver, String text) {
		this.androidDriver = androidDriver;
		WebElement scrolledToElement = androidDriver.findElementByAndroidUIAutomator(
				"new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().text(\""
						+ text + "\").instance(0))");
		return scrolledToElement;
	}

	public boolean scrollToElementByVisibleTextIsDisplayed(AndroidDriver androidDriver, String text) {
		try {
			return androidDriver.findElementByAndroidUIAutomator(
					"new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().text(\""
							+ text + "\").instance(0))")
					.isDisplayed();
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	
	}

	/**
	 * This element use when element not contain text or id
	 * 
	 * @param androidDriver
	 * @param className
	 * @exception NoSuchElementException
	 * @author Shubham verma
	 * @return
	 */
	public boolean scrollToElementByClassNameIsDisplayed(AndroidDriver androidDriver, String className) {
		return androidDriver.findElementByAndroidUIAutomator(
				"new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().className(\""
						+ className + "\").instance(0))")
				.isDisplayed();
	}

	/**
	 * This element use when element not contain text or id when function found
	 * element then perform click operation
	 * 
	 * @param androidDriver
	 * @param className
	 * @exception NoSuchElementException
	 * @author Shubham verma
	 * @return
	 */
	public void scrollToElementByClassNameAndClick(AndroidDriver androidDriver, String className) {
		androidDriver.findElementByAndroidUIAutomator(
				"new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().className(\""
						+ className + "\").instance(0))")
				.click();
	}

	/**
	 *  This Method use when element contain only
	 * resource id or class name >>>>>>> level1
	 * 
	 * @param androidDriver
	 * @param text
	 * @param ClassName
	 * @exception NullPointerException
	 * @return
	 */
	public WebElement scrollToElementByResourceIDandClassName(AndroidDriver androidDriver, String resourceId,
			String ClassName) {
		this.androidDriver = androidDriver;

		WebElement scrolledToElement = androidDriver.findElementByAndroidUIAutomator(
				"new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().resourceId(\""
						+ resourceId + "\").className(\"" + ClassName + "\").instance(0))");
		return scrolledToElement;
	}

	/**
	 * <<<<<<< HEAD Author shubham verma \n This Method use when element contain
	 * only resource id or class name
	 * 
	 * ======= Author shubham verma \n This Method use when element contain only
	 * resource id or class name >>>>>>> level1
	 * 
	 * @param androidDriver
	 * 
	 * @param resourceId
	 * @exception NullPointerException
	 * @return
	 */
	public WebElement scrollToElementByResourceID(AndroidDriver androidDriver, String resourceId) {
		this.androidDriver = androidDriver;

		WebElement scrolledToElement = androidDriver.findElementByAndroidUIAutomator(
				"new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().resourceId(\""
						+ resourceId + "\").instance(0))");
		return scrolledToElement;
	}

	/**
	 * Author shubham verma Exception handling
	 * 
	 * @param androidDriver
	 * @param text
	 * @exception NullPointerException
	 * @return
	 */
	public boolean scrollToElementByVisibleText_Beta(AndroidDriver androidDriver, String text) {
		this.androidDriver = androidDriver;
		try {
			WebElement scrolledToElement = androidDriver.findElementByAndroidUIAutomator(
					"new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().text(\""
							+ text + "\").instance(0))");
			LOGGER.info(text + " Text is visble");
			return scrolledToElement.isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public void scrollByPointer(AndroidDriver<WebElement> androidDriver) {
		Dimension size = androidDriver.manage().window().getSize();
		int startx = size.width / 2;
		int starty = (int) (size.height * 0.80);
		int endy = (int) (size.height * 0.01); // changed from 0.1 0.005
		TouchAction action = new TouchAction(androidDriver);
		action.press(startx, starty).waitAction(java.time.Duration.ofMillis(1000)).moveTo(startx, endy).release()
				.perform();
	}

	public void scrollByPointer(AndroidDriver<WebElement> androidDriver, double value) {
		Dimension size = androidDriver.manage().window().getSize();
		int startx = size.width / 2;
		int starty = (int) (size.height * 0.80);
		int endy = (int) (size.height * value); // changed from 0.1 0.005
		TouchAction action = new TouchAction(androidDriver);
		action.press(startx, starty).waitAction(java.time.Duration.ofMillis(1000)).moveTo(startx, endy).release()
				.perform();
	}

	public void scrollByPointer(AndroidDriver<WebElement> androidDriver, String direction) {
		this.androidDriver = androidDriver;
		if (direction.equalsIgnoreCase("down")) {
			Dimension size = androidDriver.manage().window().getSize();
			int startx = size.width / 2;
			int starty = (int) (size.height);
			int endy = (int) (size.height * 0.01);
			TouchAction action = new TouchAction(androidDriver);
			action.press(startx, starty).waitAction(java.time.Duration.ofMillis(1000)).moveTo(startx, endy).release()
					.perform();
		} else if (direction.equalsIgnoreCase("up")) {
			Dimension size = androidDriver.manage().window().getSize();
			int startx = size.width / 2;
			int starty = (int) (size.height * 0.2);
			int endy = (int) (size.height * 0.9);
			TouchAction action = new TouchAction(androidDriver);
			try {
				action.press(startx, starty).waitAction(java.time.Duration.ofMillis(1000)).moveTo(startx, endy)
						.release().perform();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	public void longPress(AndroidDriver<WebElement> androidDriver, WebElement elementInput) {
		this.androidDriver = androidDriver;
		TouchAction action = new TouchAction(androidDriver);
		action.longPress(elementInput).waitAction(java.time.Duration.ofMillis(3000)).release().perform();
	}

	public void scrollElement(WebElement elementInput, WebDriver androidDriver2) {

		WebElement element = elementInput;
		this.androidDriver = (AndroidDriver<WebElement>) androidDriver2;
		JavascriptExecutor js = (JavascriptExecutor) androidDriver;
		HashMap<String, String> scrollObjects = new HashMap<String, String>();
		scrollObjects.put("direction", "down");
		String id = androidDriver.findElement(By.id("rv_settings")).getAttribute("id");
		scrollObjects.put("element", ((RemoteWebElement) androidDriver.findElement(By.id("rv_settings"))).getId());
		js.executeScript("mobile: scroll", scrollObjects);
	}

	public void scrollDown(AndroidDriver androidDriver) {

		this.androidDriver = androidDriver;
		JavascriptExecutor js = (JavascriptExecutor) androidDriver;
		HashMap<String, String> scrollObject = new HashMap<String, String>();
		scrollObject.put("direction", "down");
		js.executeScript("mobile: scroll", scrollObject);
	}

	
	@SuppressWarnings("null")
	public LinkedHashSet<String> executeADBCommandToFetchData(String command) {
		LinkedHashSet<String> resultSet = new LinkedHashSet<>();
		String ADB = System.getenv("ANDROID_HOME");
		String commandToBeExecuted = "/platform-tools/adb shell " + command;
		try {
			System.out.println("command" + ADB + commandToBeExecuted);
			Process process = Runtime.getRuntime().exec(ADB + commandToBeExecuted);
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String s;
			while ((s = reader.readLine()) != null) {
				System.out.println("The result of adb command are :" + s);
				try {
					resultSet.add(s);
				} catch (NullPointerException e) {
					System.out.println("exception while aexecuting adb command " + command);
					return null;
				}
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		return resultSet;
	}

	public void executeADBShellCommand(String command) throws IOException, InterruptedException {
		String ADB = System.getProperty("user.home") + "/Library/Android/sdk";
		// String ADB=System.getenv("ANDROID_HOME");
		String commandToBeExecuted = "/platform-tools/adb shell " + command;

		LOGGER.info("ADB COMMAND to be executed : " + ADB + commandToBeExecuted);
		Process process = Runtime.getRuntime().exec(ADB + commandToBeExecuted);

		process.waitFor();

	}

	public void executeADBCommand(String command) throws IOException, InterruptedException {
		String ADB = System.getProperty("user.home") + "/Library/Android/sdk";
		// String ADB=System.getenv("ANDROID_HOME");
		String commandToBeExecuted = "/platform-tools/adb " + command;
		LOGGER.info("command " + ADB + commandToBeExecuted);
		Process process = Runtime.getRuntime().exec(ADB + commandToBeExecuted);

		BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;
		while (true) {
			line = r.readLine();
			if (line == null) {
				break;
			}
			LOGGER.info(line);
		}
		process.waitFor();

	}

	public void scrollAndSwipeByElementAndroid(AndroidDriver<?> androidDriver, By byElement, String direction) {
		String elementText = null;

		if ("left".equalsIgnoreCase(direction)) {
			Dimension size = androidDriver.manage().window().getSize();
			int startx = (int) (size.width * 0.80);
			int endx = (int) (size.width * 0.01);
			int starty = size.height / 2;
			System.out.println("startx = " + startx + " ,endx = " + endx + " , starty = " + starty);
			TouchAction action = new TouchAction(androidDriver);
			action.press(startx, starty).waitAction(java.time.Duration.ofMillis(1000)).moveTo(endx, starty).release()
					.perform();
		} else if ("right".equalsIgnoreCase(direction)) {

			Dimension size = androidDriver.manage().window().getSize();
			int endx = (int) (size.width * 0.80);
			int startx = (int) (size.width * 0.01);
			int starty = size.height / 2;
			System.out.println("startx = " + startx + " ,endx = " + endx + " , starty = " + starty);
			TouchAction action = new TouchAction(androidDriver);

			action.press(startx, starty).waitAction(java.time.Duration.ofMillis(1000)).moveTo(endx, starty).release()
					.perform();
		} else if ("up".equalsIgnoreCase(direction)) {
			Dimension size = androidDriver.manage().window().getSize();
			int startx = size.width / 2;
			int starty = (int) (size.height * 0.80);
			int endy = (int) (size.height * 0.20);
			System.out.println("startx = " + startx + " ,starty = " + starty + " , endy = " + endy);
			TouchAction action = new TouchAction(androidDriver);
			action.press(startx, starty).waitAction(java.time.Duration.ofMillis(1000)).moveTo(startx, endy).release()
					.perform();
		} else if ("down".equalsIgnoreCase(direction)) {
			Dimension size = androidDriver.manage().window().getSize();
			int startx = size.width / 2;
			int starty = (int) (size.height * 0.80);
			int endy = (int) (size.height * 0.02);
			System.out.println("startx = " + startx + " ,starty = " + starty + " , endy = " + endy);
			TouchAction action = new TouchAction(androidDriver);
			action.press(startx, starty).waitAction(java.time.Duration.ofMillis(1000)).moveTo(startx, endy).release()
					.perform();
		}

	}

	public void doubleTapOnElement(AndroidDriver<WebElement> androidDriver, WebElement element) {
		this.androidDriver = androidDriver;
		MultiTouchAction multiTouch = new MultiTouchAction(androidDriver);
		TouchAction action0 = new TouchAction(androidDriver).tap(element).waitAction(java.time.Duration.ofMillis(50))
				.tap(element);
		try {
			multiTouch.add(action0).perform();
		} catch (Exception e) {
			System.out.println(
					"Unable to do second tap on element, probably because element requieres single tap on this Android version");
		}
	}

	public void pressBackButton(AndroidDriver<WebElement> androidDriver) {
		this.androidDriver=androidDriver;
		androidDriver.pressKeyCode(AndroidKeyCode.BACK);
		LOGGER.info("press device back button");
	}

	public void flick_left(AndroidDriver<WebElement> androidDriver, WebElement flick_element) {
		this.androidDriver = androidDriver;
		Point location = flick_element.getLocation();
		Dimension size = flick_element.getSize();

		int flick_x, flick_y, flick_start_x, flick_end_x, flick_start_y, flick_end_y;

		flick_x = size.getWidth();
		flick_y = location.y + (size.getHeight() / 2);

		flick_start_x = flick_x - (int) ((double) flick_x * 0.25);
		flick_end_x = flick_x - (int) ((double) flick_x * 0.55);
		// swipe(androidDriver,flick_start_x, flick_y, flick_end_x,
		// flick_y,200);
		swipe(androidDriver, flick_element, flick_start_x, flick_y, flick_end_x, flick_y, 50);

	}

	public void swipe(AndroidDriver<WebElement> androidDriver, WebElement e, int swipe_start_x, int swipe_start_y,
			int swipe_end_x, int swipe_end_y, int duration) {
		this.androidDriver = androidDriver;
		int x = swipe_start_x;
		int y = swipe_start_y;
		int x_travel = swipe_end_x - swipe_start_x;
		int y_travel = swipe_end_y - swipe_start_y;
		TouchAction action1 = new TouchAction(androidDriver).longPress(e)
				.waitAction(java.time.Duration.ofMillis(duration)).moveTo(x_travel, y_travel).release();
		try {
			action1.perform();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	public boolean verifyIfElementIsPresent(AndroidDriver androidDriver, WebElement element) {
		this.androidDriver = androidDriver;
		boolean flag = false;
		if (element.isEnabled()) {
			flag = true;
		} else {
			flag = false;
			System.out.println(element + "is not present");
		}
		return flag;

	}

	/*
	 * Author shubham verma
	 */
	public boolean verifyIfElementIsPresent_V2(AndroidDriver androidDriver, WebElement element) {
		this.androidDriver = androidDriver;
		boolean flag = false;

		try {

			if (element.isEnabled()) {
				flag = true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			flag = false;
		}

		return flag;

	}

	public boolean verifyText(AndroidDriver androidDriver, WebElement element, String expectedText) {
		this.androidDriver = androidDriver;
		boolean flag = false;
		if (element.getAttribute("text").trim().equalsIgnoreCase(expectedText.trim().trim())) {
			LOGGER.info("text of element is " + element.getAttribute("text") + " and expected text is " + expectedText
					+ "returning true");
			return true;
		} else {
			LOGGER.info("text of element is " + element.getAttribute("text") + " and expected text is " + expectedText
					+ "returning false");
			return false;
		}
	}

	public WebElement WaitForElement(AndroidDriver<WebElement> androidDriver, WebElement element) {
		this.androidDriver = androidDriver;

		try {
			WebDriverWait wait = new WebDriverWait(androidDriver, 25);
			wait.until(ExpectedConditions.visibilityOf(element));
			return element;
		} catch (NoSuchElementException e) {
			LOGGER.info("wait condition for element not met");
		}
		return null;
	}

	/*
	 * author shubham verma
	 */
	public WebElement WaitForElement(AndroidDriver<WebElement> androidDriver, WebElement element, int second) {
		this.androidDriver = androidDriver;

		try {
			WebDriverWait wait = new WebDriverWait(androidDriver, second);
			wait.until(ExpectedConditions.visibilityOf(element));
			return element;
		} catch (NoSuchElementException e) {
			LOGGER.info("wait condition for element not met");
		}
		return null;
	}

	/**
	 * Author shubham verma
	 * 
	 * @param element
	 *            <<<<<<< HEAD
	 * @param Attribut
	 *            <<<<<<< HEAD
	 * @param expectedt
	 *            max time limit 3 min =======
	 * @param expectedt
	 *            max time limit 3 min >>>>>>> queuePlayer_Shreyans =======
	 * @param Attribut
	 * @param expectedt
	 *            max time limit 3 min >>>>>>> level1
	 */
	public boolean waitForElementValue(WebElement element, String Attribut, String expected) {
		long startTime = System.currentTimeMillis();

		while ((System.currentTimeMillis() - startTime) < 120000) {
			LOGGER.info(
					(System.currentTimeMillis() - startTime) + " " + element.getAttribute(Attribut) + " " + expected);

			if (element.getAttribute(Attribut).equals(expected)) {
				// download successful
				return true;
			}
		}

		// timer expired
		return false;
	}

	public LinkedHashSet<String> getSongsInList(AndroidDriver androidDriver, List<WebElement> songsElementInput)
			throws InterruptedException {
		this.androidDriver = androidDriver;
		songTitle = new LinkedHashSet<String>();
		List<WebElement> songsElement = new ArrayList<>();
		songsElement = songsElementInput;
		boolean flag = false;
		Thread.sleep(1000);
		flag = addSongsToSet(songsElement);

		if (flag == false) {
			for (String song : songTitle) {
				System.out.println("Songs finally added" + song);
			}
			return songTitle;
		}

		while (flag) {
			// androidDriver.findElements(By.xpath("//android.support.v4.widget.DrawerLayout[contains(@resource-id,'dl_navigation_drawer_container')]//android.support.v7.widget.RecyclerView[contains(@resource-id,'rv_item_list')]/*[contains(@class,'android.widget.LinearLayout')]"));
			songsElement = androidDriver.findElements(By.xpath(
					"//android.support.v4.widget.DrawerLayout[contains(@resource-id,'dl_navigation_drawer_container')]//android.support.v7.widget.RecyclerView[contains(@resource-id,'rv_item_list')]/*[contains(@class,'android.widget.FrameLayout')]"));
			flag = addSongsToSet(songsElement);
		}
		return songTitle;

	}

	public boolean addSongsToSet(List<WebElement> songsElement) throws InterruptedException {
		boolean flag = false;

		for (int i = 1; i < songsElement.size(); i++) {
			Thread.sleep(1000);
			System.out.println("ith " + i + " and size is" + songsElement.size());
			String song = songsElement.get(i)
					.findElement(By.xpath("//*[contains(@class,'android.widget.LinearLayout')]"))
					.findElement(By.xpath("//*[contains(@class,'android.widget.LinearLayout')]"))
					.findElement(By.className("android.widget.TextView")).getAttribute("text");
			System.out.println("songs in the list" + song);
			flag = songTitle.add(song);
		}
		scrollByPointer((AndroidDriver<WebElement>) androidDriver);
		return flag;
	}

	// @Test
	public void writePropFile() {
		Properties prop = new Properties();
		OutputStream output = null;

		try {

			output = new FileOutputStream(System.getProperty("user.dir") + "/testData.properties");

			prop.setProperty("searchTerm", "Bom Diggy Diggy");
			prop.setProperty("playListName", "Appium Automation");

			prop.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 * This fun created by shubham verma this method get device time Return
	 * Hours:Minute
	 */
	public String getCurrentDeviceTime() {

		String DeviceTimeAndDate = androidDriver.getDeviceTime();
		SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
		Date date = null;
		try {
			date = (Date) formatter.parse(DeviceTimeAndDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, 1);
		String Hours = String.valueOf(cal.get(Calendar.HOUR));
		String Minute = String.valueOf(cal.get(Calendar.MINUTE));

		if (String.valueOf(cal.get(Calendar.MINUTE)).length() == 1) {
			Minute = "0" + String.valueOf(cal.get(Calendar.MINUTE));

		}
		return Hours + ":" + Minute;
	}

	/*
	 * created by shubham verma this method use for scrolling by adb command
	 * this method also work when any other layout (like mini player)
	 */
	public void scrollHalfByADBToUp(WebDriver androidDriver) {
		Dimension size = androidDriver.manage().window().getSize();
		int X_1 = size.getWidth() / 2;
		int Y_1 = size.getHeight() / 2;
		int X_2 = size.getWidth() / 8;
		int Y_2 = size.getHeight() / 8;
		try {
			executeADBCommand("input swipe " + X_1 + " " + Y_1 + " " + X_2 + " " + 0);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * created by shubham verma this method use for scrolling by adb command
	 * this method also work when any other layout (like mini player)
	 */
	public void scrollfullByADBToUp(WebDriver androidDriver) {
		Dimension size = androidDriver.manage().window().getSize();
		int X_1 = size.getWidth() / 2;
		int Y_1 = (int) (size.getHeight() * 0.8);
		int X_2 = size.getWidth() / 2;
		int Y_2 = (int) (size.getHeight() * 0.2);
		try {
			executeADBShellCommand("input swipe " + X_1 + " " + Y_1 + " " + X_2 + " " + 0);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void scrollWith_X_Y_Coordinates(int X_1, int Y_1, int X_2, int Y_2) {
		try {
			executeADBShellCommand("input swipe " + X_1 + " " + Y_1 + " " + X_2 + " " + Y_2);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @author shubham verma Tab on scree by X and Y coordinates
	 * @param X_1
	 * @param Y_1
	 */

	public void TapWithADB(int X_1, int Y_1) {
		try {
			executeADBCommand("input tap " + X_1 + " " + Y_1);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void killApp(String appPackage) throws IOException, InterruptedException {
		executeADBShellCommand("am force-stop " + appPackage);

	}

	public void installApptoUpgrade(String appPath) throws IOException, InterruptedException {
		executeADBCommand("install -r " + appPath);
	}

	public String readPropFile(String fileName, String property) {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/" + fileName);
			prop.load(input);
			return prop.getProperty(property);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;

	}

	public String getCurrentTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");// dd/MM/yyyy
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}

	public void runAppInBackGroun(int seconds) {

		androidDriver.runAppInBackground(java.time.Duration.ofSeconds(seconds));

	}

	public void clickOnElementUsingText(String key) {
		WebElement textValue = androidDriver.findElement(By.xpath("//*[contains(@text,'" + key + "')]"));
		// WaitForElement(androidDriver, textValue);
		// textValue.click();
		clickOnWebElement(androidDriver, textValue);
	}

	// Tap by coordinates
	public void tapByCoordinates(int x, int y) {
		new TouchAction(androidDriver).tap(point(x, y)).waitAction(waitOptions(Duration.ofMillis(250))).perform();
	}

	public enum Direction {
		LEFTTORIGHT, RIGHTTOLEFT, TOPTOBOTTOM, BOTTOMTOTOP
	}

	public void swipeRailsHorizontally(AndroidDriver<WebElement> androidDriver, List<WebElement> element,
			Direction direction) {
		Dimension size = androidDriver.manage().window().getSize();
		int y1 = element.get(0).getLocation().getY();
		switch (direction) {
		case LEFTTORIGHT: {
			new TouchAction(androidDriver).press(100, (y1 + 10)).moveTo((size.getWidth() - 10), (y1 + 10)).release()
					.perform();
			break;
		}

		case RIGHTTOLEFT: {
			new TouchAction(androidDriver).press((size.getWidth() - 200), (y1 + 10)).waitAction(Duration.ofMillis(2000))
					.moveTo(30, (y1 + 10)).release().perform();

			break;
		}
		}
	}

	public void swipeRailsHorizontally(List<WebElement> element, Direction direction) {

		int elementSize = element.size();
		Dimension dimension = androidDriver.manage().window().getSize();
		int y1 = element.get(0).getLocation().getY();
		int X = element.get(0).getRect().getX();
		int Y = element.get(0).getRect().getY();
		int Y1 = element.get(elementSize - 2).getRect().getY();
		switch (direction) {
		case LEFTTORIGHT: {
			new TouchAction(androidDriver).press(100, (y1 + 10)).waitAction(Duration.ofMillis(2000))
					.moveTo((dimension.getWidth() - 10), (y1 + 10)).release().perform();
			break;
		}

		case RIGHTTOLEFT: {

			new TouchAction(androidDriver).press((dimension.getWidth() - 200), (y1 + 10))
					.waitAction(Duration.ofMillis(2000)).moveTo(30, (y1 + 10)).release().perform();

			break;
		}
		}
	}

	public void swipeRailsHorizontally_Beta(AndroidDriver<WebElement> androidDriver, List<WebElement> element,
			Direction direction) {
		Dimension size = androidDriver.manage().window().getSize();
		int y1 = element.get(0).getLocation().getY();
		switch (direction) {
		case LEFTTORIGHT: {
			new TouchAction(androidDriver).press(100, (y1 + 10)).moveTo((size.getWidth() - 10), (y1 + 10)).release()
					.perform();
			break;
		}

		case RIGHTTOLEFT: {
			new TouchAction(androidDriver).press((size.getWidth() - 200), (y1 + 10)).waitAction(Duration.ofMillis(2000))
					.moveTo(30, (y1 + 10)).release().perform();
			break;
		}
		}
	}

	/**
	 * 
	 * @param From
	 * @param To
	 */
	public void copyfolderOrFileInMobileLocation(String fromLocation, String toLocation) {
		LOGGER.info("LocalMP3 songs Folder copy from " + fromLocation + " To Mobile Location " + toLocation);
		try {
			executeADBCommand("push " + fromLocation + " " + toLocation);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void clickLockButton(CommonPages commonPages) {
		try {
			commonPages.executeADBCommand(" shell input keyevent 26");
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void unlockDevice(CommonPages commonPages) {
		try {
			commonPages.executeADBCommand(" shell am start -n io.appium.unlock/.Unlock");

		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static int counter = 0;

	public boolean scrollUntilTextNotFound(List<WebElement> title, String searchTerm) {
		int i;
		title = androidDriver.findElements(By.id("tv_title"));
		for (i = 0; i < title.size(); i++) {
			if (title.get(i).getText().equalsIgnoreCase(searchTerm)) {
				title.get(i).click();
				return true;
			} else if (!title.get(i).getText().equalsIgnoreCase(searchTerm) && i == title.size() - 1) {
				scrollByPointer(androidDriver, 0.3);
				counter++;
				if (counter > 5) {
					LOGGER.info("Search Term not found");
					return false;
				} else
					scrollUntilTextNotFound(title, searchTerm);
			}
		}
		if (counter > 5) {
			LOGGER.info("Search Term not found");
			return false;
		} else {
			LOGGER.info("Search Term found");
			return true;
		}
	}

	/**
	 * Author shubham verma <<<<<<< HEAD
	 * 
	 * @param text
	 *            {@link Description This Method use for text visible or not in
	 *            page} =======
	 * @param text
	 *            {@link Description This Method use for text visible or not in
	 *            page} >>>>>>> level1
	 * @return
	 * @throws InterruptedException
	 */
	public boolean verifyTextIsVisible(String text) {
		LOGGER.info("found element by " + text + " Text");
		try {
			return androidDriver
					.findElementByAndroidUIAutomator("new UiSelector().textContains(\"" + text + "\").instance(0)")
					.isDisplayed();
		} catch (Exception e) { // use specific exception like no such element
			return false;
		}

	}

	/**
	 * Author shubham verma
	 * 
	 * @param text
	 *            {@link Description This Method use for text visible or not in
	 *            page}
	 * @return
	 * @throws InterruptedException
	 */
	public void verifyTextIsVisibleAndClick(String text) {
		
		clickOnWebElement(androidDriver, androidDriver.findElementByAndroidUIAutomator("new UiSelector().textContains(\"" + text + "\").instance(0)"));
	
	}

	/**
	 * Author shubham verma <<<<<<< HEAD
	 * 
	 * @param text
	 *            Description This Method use for text visible or not in page
	 *            and click =======
	 * @param text
	 *            Description This Method use for text visible or not in page
	 *            and click >>>>>>> level1
	 * @return
	 * @throws InterruptedException
	 */
	public void findElementByTextAndClick(String text) {
		androidDriver.findElementByAndroidUIAutomator("new UiSelector().textContains(\"" + text + "\").instance(0)")
				.click();
		LOGGER.info("found element by " + text + " Text and the click");

	}

	/**
	 * Author shubham verma
	 * 
	 * @param text
	 *            Description This Method use for text visible or not in page
	 *            and click
	 * @return
	 * @throws InterruptedException
	 */
	public void findElementByTextAndClick_Beta(String text) {
		try {
			androidDriver.findElementByAndroidUIAutomator("new UiSelector().textContains(\"" + text + "\").instance(0)")
					.click();
		} catch (Exception e) {
			LOGGER.info(text + " is not visible");
		}

	}

	public void addLocalSongsToDevice(CommonPages commonPages, String from, String to) {
		try {
			commonPages.executeADBCommand(" push " + from + " " + to);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Author shubham verma Enable Sim Data
	 */
	public void enableSimData() {
		try {
			executeADBShellCommand(ADBCommands.MOBILE_DATA_ENABLE.getCommand());
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Author shubham verma Disable Sim Data
	 */
	public void disableSimData() {
		try {
			executeADBShellCommand(ADBCommands.MOBILE_DATA_DISABLE.getCommand());
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Author shubham verma Enable Wifi
	 */
	public void enableWifi() {

		try {
			executeADBShellCommand(ADBCommands.ENABLE_WIFI.getCommand());
//			if (androidDriver.findElementById("switch_widget").getAttribute("checked").equals("false")) {
//				androidDriver.findElementById("switch_widget").click();
//				LOGGER.info("Wifi is enable");
//			} else {
//				LOGGER.info("Wifi Already Enable");
//				System.out.println("Wifi Already Enable");
//			}
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Author shubham verma Disable wifi
	 * 
	 */
//	public void disableWifi() {
//
//		try {
//			executeADBShellCommand(ADBCommands.DISABLE_WIFI.getCommand());
////			if (androidDriver.findElementById("switch_widget").getAttribute("checked").equals("true")) {
////				androidDriver.findElementById("switch_widget").click();
////				LOGGER.info("Wifi is disable");
////			} else {
////				LOGGER.info("Wifi Already Disbale");
////				System.out.println("Wifi Already Disbale");
////			}
//
//		} catch (IOException | InterruptedException e) {
//			// TODO Auto-generated catch block
//		}
//	}

	public boolean isVisibleElement(WebElement element) {
		try {
			WaitForElement(androidDriver, element);
			return true;
		} catch (NoSuchElementException | org.openqa.selenium.TimeoutException e) {
			return false;
		}
	}

	public void backToPrevScreen() throws InterruptedException {
		Thread.sleep(2000);
		androidDriver.pressKeyCode(AndroidKeyCode.BACK);
		Thread.sleep(1000);

	}

	public enum networkType {
		_2G, _3G, _4G
	}

	/**
	 * Author Shubham verma selectNetworkType
	 */
	public void selectPreferredNetworkType(networkType type) {

		try {

			executeADBCommand("shell am start -n 'com.android.phone/.MobileNetworkSettings'");
			Thread.sleep(3000);
			scrollToElementByVisibleTextAndClick(androidDriver, "Preferred network type");
			switch (type) {
			case _2G: {
				findElementByTextAndClick_Beta("GSM only");
				break;
			}

			case _3G: {
				findElementByTextAndClick_Beta("WCDMA only");
				break;
			}

			case _4G: {
				findElementByTextAndClick_Beta("LTE");
				break;
			}

			}

		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static int i = 0;
	public void openAppDeeplinks(String targetType) throws IOException, InterruptedException {
		executeADBShellCommand("am start -n " + getPropertyValue("/config.properties", "musicAppPackage") + "/"
				+ getPropertyValue("/config.properties", "appActivity") + " -d " + "\"android-app://"
				+ getPropertyValue("/config.properties", "musicAppPackage") + "/http://www.wynk.in/music" + targetType
				+ "\"");
				Thread.sleep(5000);
	}

	public void backGroungApp() {
		androidDriver.closeApp();
	}

	@SuppressWarnings("resource")
	public static Object[][] readData(String filePath, String sheetName) throws IOException {
		String testData[][] = null;
		try {
			File myFile = new File(filePath);
			FileInputStream fis = new FileInputStream(myFile);
			XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
			XSSFSheet mySheet = myWorkBook.getSheet(sheetName);
			Iterator<Row> rowIterator = mySheet.iterator();
			int rows = mySheet.getLastRowNum();
			int columns = mySheet.getRow(0).getLastCellNum();
			testData = new String[rows][columns];
			rowIterator.next();
			for (int i = 1; i <= rows; i++) {
				Row row = rowIterator.next();
				for (int j = 0; j < columns; j++) {
					testData[i - 1][j] = row.getCell(j).toString();
				}
			}
		} catch (Exception e) {
		}
		return testData;
	}

	public void swipeVertically(List<WebElement> element, Direction direction) {
		int elementSize = element.size();
		int X = element.get(0).getRect().getX();
		int Y = element.get(0).getRect().getY();
		int Y1 = element.get(elementSize - 2).getRect().getY();
		switch (direction) {

		case TOPTOBOTTOM: {
			new TouchAction(androidDriver).press(X, Y).waitAction(Duration.ofMillis(1500)).moveTo(X, Y1).release()
					.perform();
			break;
		}

		case BOTTOMTOTOP: {
			new TouchAction(androidDriver).press(X, Y1).waitAction(Duration.ofMillis(2000)).moveTo(X, Y).release()
					.perform();
			break;
		}
		}
	}

	public void selectPrefferedNetwork(String networkType) throws IOException, InterruptedException {
		// executeADBCommand("shell am start com.android.dialer");
		String code = "*#*#4636#*#*";
		String appPackage = "com.android.dialer";
		String appActivity = ".DialtactsActivity";
		Activity activity = new Activity(appPackage, appActivity);
		activity.setStopApp(false);
		androidDriver.startActivity(activity);
		try {
			androidDriver.findElement(By.id("com.android.dialer:id/floating_action_button")).click();
		} catch (Exception e) {

		}
		androidDriver.findElement(By.id("com.android.dialer:id/digits")).clear();
		androidDriver.findElement(By.id("com.android.dialer:id/digits")).sendKeys(code);
		try {
			List<WebElement> options = androidDriver.findElements(By.id("android:id/title"));
			options.get(0).click();
		} catch (Exception e) {

		}

		androidDriver.findElement(By.id("com.android.settings:id/preferredNetworkType")).click();
		List<WebElement> networkOptions;
		boolean flag = false;
		switch (networkType) {
		case "2G": {
			networkOptions = androidDriver.findElements(By.id("android:id/text1"));
			swipeVertically(networkOptions, Direction.TOPTOBOTTOM);

			for (int l = 0; l < 5; l++) {
				networkOptions = androidDriver.findElements(By.id("android:id/text1"));
				int size = networkOptions.size();
				for (int i = 0; i < size; i++) {
					if (networkOptions.get(i).getText().equals("GSM only")) {
						networkOptions.get(i).click();
						flag = true;
						break;
					}
				}
				if (flag) {
					break;
				}
				swipeVertically(networkOptions, Direction.BOTTOMTOTOP);
			}
			break;
		}

		case "3G": {
			networkOptions = androidDriver.findElements(By.id("android:id/text1"));
			swipeVertically(networkOptions, Direction.TOPTOBOTTOM);
			for (int l = 0; l < 5; l++) {
				networkOptions = androidDriver.findElements(By.id("android:id/text1"));
				int size = networkOptions.size();
				for (int i = 0; i < size; i++) {
					if (networkOptions.get(i).getText().equals("GSM auto (PRL)")) {
						networkOptions.get(i).click();
						flag = true;
						break;
					}
				}
				if (flag) {
					break;
				}
				swipeVertically(networkOptions, Direction.BOTTOMTOTOP);
			}
			break;
		}

		case "4G": {
			networkOptions = androidDriver.findElements(By.id("android:id/text1"));
			swipeVertically(networkOptions, Direction.TOPTOBOTTOM);
			for (int l = 0; l < 5; l++) {
				networkOptions = androidDriver.findElements(By.id("android:id/text1"));
				int size = networkOptions.size();
				for (int i = 0; i < size; i++) {
					if (networkOptions.get(i).getText().equals("LTE/UMTS auto (PRL)")) {
						networkOptions.get(i).click();
						flag = true;
						break;
					}
				}
				if (flag) {
					break;
				}
				swipeVertically(networkOptions, Direction.BOTTOMTOTOP);
			}
			break;
		}

		}
		Thread.sleep(10000); // Sleep required for selected network to become
		// effective
	}

	public HashSet<String> getFilesUnderPath(String path) {
		fileSet = new HashSet<String>();
		File file = new File(path);
		String[] fileList = file.list();
		for (String name : fileList) {
			LOGGER.info("File under given path : " + path + " is :" + name);
			fileSet.add(name);
		}
		return fileSet;
	}
	
	
	public HashSet<String> getFilesUnderPath_v2(String path) {
		fileSet = new HashSet<String>();
		File file = new File(path);
		String[] fileList = file.list();
		for (String name : fileList) {
			LOGGER.info("File under given path : " + path + " is :" + name);
			fileSet.add(path+"/"+name);
		}
		return fileSet;
	}

	public boolean verifyTextIsVisibleOnPage(String text) {
		// System.out.println(androidDriver.getPageSource());
		WebElement ele = androidDriver.findElement(By.xpath("//*[@text='" + text + "']"));
		WaitForElement(androidDriver, ele);
		boolean isDisplay;
		try {
			isDisplay = ele.isDisplayed();
			return isDisplay;
		} catch (NoSuchElementException e) {
			isDisplay = false;
			return isDisplay;
		}
	}
	



	public void unInstallApp(String packageName ) throws IOException, InterruptedException {
		executeADBCommand("uninstall "+packageName);

	}
	
	public void disableWifi() throws IOException, InterruptedException {
		LinkedHashSet<String> result= executeADBCommandToFetchData("dumpsys wifi | grep \"Wi-Fi is\"");
		if(result.toString().contains("enabled")) {
			executeADBShellCommand("am start -a android.intent.action.MAIN -n com.android.settings/.wifi.WifiSettings");
			Thread.sleep(3000);
			executeADBShellCommand("input keyevent 19");
			executeADBShellCommand("input keyevent 23");
			androidDriver.pressKeyCode(AndroidKeyCode.BACK);
		}
	}
	
	public void enableAirplaneMode() throws IOException, InterruptedException {
		executeADBShellCommand("am start -a android.settings.AIRPLANE_MODE_SETTINGS");
		Thread.sleep(3000);
		WebElement airplaneMode=androidDriver.findElement(By.xpath("//*[contains(@text,'plane mode')]/parent::android.widget.RelativeLayout/following-sibling::android.widget.LinearLayout//android.widget.Switch"));
		if(airplaneMode.getText().equalsIgnoreCase("OFF")){
			airplaneMode.click();
		}
		androidDriver.pressKeyCode(AndroidKeyCode.BACK);
	}
	
	public void disableAirplaneMode() throws IOException, InterruptedException {
		executeADBShellCommand("am start -a android.settings.AIRPLANE_MODE_SETTINGS");
		Thread.sleep(3000);
		WebElement airplaneMode=androidDriver.findElement(By.xpath("//*[contains(@text,'plane mode')]/parent::android.widget.RelativeLayout/following-sibling::android.widget.LinearLayout//android.widget.Switch"));
		if(airplaneMode.getText().equalsIgnoreCase("ON")){
			airplaneMode.click();
		}
		androidDriver.pressKeyCode(AndroidKeyCode.BACK);
	}
	
	public boolean isElmtDisplayed(WebElement ele) {
		boolean isDisplay;
		try {
			isDisplay=ele.isDisplayed();
			return isDisplay;
		}catch(NoSuchElementException e) {
			isDisplay=false;
			return isDisplay;
		}
		
	}
}
