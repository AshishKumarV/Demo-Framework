package tv.accedo.TVAndroid.Page;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import tv.accedo.TVAndroid.Utils.BaseUtil;

import io.appium.java_client.android.AndroidDriver;

public class Notifications extends BaseUtil {
	private AndroidDriver<WebElement> androidDriver;
	private static final Logger LOGGER = Logger.getLogger(Notifications.class);
	
	public Notifications(AndroidDriver androidDriver)
	{
		this.androidDriver = androidDriver;
		PageFactory.initElements(androidDriver, this);
	}
	
	@FindBy(id="com.android.systemui:id/delete")
	public WebElement deleteAllNotifications;
	
	@FindBy(id="android:id/app_name_text")
	public List<WebElement> appName;
	
	@FindBy(id="android:id/header_text")
	public List<WebElement> songTitleWithAppName;
	
	@FindBy(id="android:id/title")
	public List<WebElement> songTitle;
	
	@FindBy(id="android:id/action0")
	public List<WebElement> playerControls;
	
	@FindBy(id="android:id/expand_button")
	public WebElement expandButton;
	
	@FindBy(id="android:id/right_icon")
	public WebElement imageIcon;
	
	
	
	public void clickDeleteAllNotifications(){
		try{
			clickOnWebElement(androidDriver, deleteAllNotifications);
		}
		catch(NoSuchElementException | org.openqa.selenium.TimeoutException e){
			LOGGER.info("Delete button is not present");
		}	
	}
	
	public String getTextAppName(){
		return appName.get(0).getText();		
	}
	
	public String getTextSongTitleWithAppName(){
		return songTitleWithAppName.get(0).getText();		
	}
	
	public String getTextSongTitle(){
		return songTitle.get(0).getText();		
	}
	
	public void clickPreviousButton(){
		clickOnWebElement(androidDriver, playerControls.get(0));
	}
	
	public void clickNextButton(){
		clickOnWebElement(androidDriver, playerControls.get(2));
	}
	
	public void clickPlayPauseButton(){
		clickOnWebElement(androidDriver, playerControls.get(1));
	}
	
	public boolean isVisibleimageIcon(){
		try{
			WaitForElement(androidDriver, imageIcon);
			return true;
		}
		catch(NoSuchElementException | org.openqa.selenium.TimeoutException e){
			return false;
		}
	}
	
	public void clickExpandButton(){
		clickOnWebElement(androidDriver, expandButton);
	}
	
	public void clickImageIcon(){
		clickOnWebElement(androidDriver, imageIcon);
		
	}
}
