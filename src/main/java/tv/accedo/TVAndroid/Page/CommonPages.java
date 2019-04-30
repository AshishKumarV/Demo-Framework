package tv.accedo.TVAndroid.Page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.model.Author;

import io.appium.java_client.android.AndroidDriver;
import tv.accedo.TVAndroid.Utils.BaseUtil;

public class CommonPages extends BaseUtil {
	private AndroidDriver<WebElement> androidDriver;
	private static final Logger LOGGER = Logger.getLogger(CommonPages.class);
	
//	private AndroidDriver androidDriver;
	String quality;

	@FindBy(id = "tv_continue")
	public WebElement continueButton;

	@FindBy(xpath = ("//*[@class='android.widget.ImageView']"))
	public WebElement addPopup;

	@FindBy(id = "et_number")
	public WebElement enterMobileNumberField;

	@FindBy(id = "tv_title")
	public WebElement popUpTitle;

	@FindBy(id = "tv_subtitle")
	public WebElement popUpSubTitle;

	@FindBy(id = "iv_close")
	public WebElement closePopUp;

	@FindBy(id = "iv_stop_autostart")
	public WebElement stopAutoPlayStart;

	@FindBy(id = "otp_editor")
	public WebElement enterOtpField;

	@FindBy(id = "tv_otp_action")
	public WebElement otpContinueButton;

	@FindBy(id = "text_action")
	public WebElement continueMsisdnButton;

	@FindBy(id = "tv_edit_number")
	public WebElement editNumberButton;

	@FindBy(id = "tv_call_me")
	public WebElement callMeButton;

	@FindBy(id = "tv_otp_timer_resend")
	public WebElement otpTimerLabel;

	@FindBy(id = "btn_dialog_cancel")
	public WebElement localMp3LaterButton;

	@FindBy(id = "action_close")
	public WebElement closeWebViewButton;

	@FindBy(id = "iv_close_dialog")
	public WebElement closeDialogBox;
	
	@FindBy(id = "iv_navigation_up")
	public WebElement hamburgerOption;

	@FindBy(xpath = "//android.widget.TextView[contains(@text,'Music Language(s)')]")
	public WebElement musiclanguage;
	
	@FindBy(id="tv_done")
	public WebElement done;
	
	@FindBy(id= "iv_empty")
	public WebElement errorImageInOffline;
	
	@FindBy(id= "tv_empty_text")
	public WebElement errorMessageInOffline;
	
	@FindBy(id= "tv_empty_sub_text")
	public WebElement errorSubTextInOffline;
	
	@FindBy(id="iv_global_notification_close")
	public WebElement offlineMusicButton;
	
	@FindBy(id= "tv_empty_btn")
	public WebElement playOfflineMusicButton;

	@FindBy(id = "rl_global_notification")
	public WebElement croutonBar;
	
	@FindBy(id = "com.lenovo.FileBrowser:id/viewTitle")
	public List<WebElement> songTitlesInFolder;
	
	@FindBy(xpath = "//android.widget.TextView[contains(@text,'Wynk Music has stopped')]")
	public WebElement crashAlert;


	public enum Navigation {
		HOME, MY_MUSIC, RADIO, UPDATES, GO_PREMIUM, MY_ACCOUNT;
	}

	public enum PlaylistTarget {
		SaveQueue, Playlist, PlaylistWithNoOfSongs, SongFromQueue, QueueSelectionPage, OverFlowMenu;
	}

	public CommonPages(AndroidDriver androidDriver) {
		this.androidDriver = androidDriver;
		PageFactory.initElements(androidDriver, this);
	}

	public void clickClosePopUp() {
		clickOnWebElement(androidDriver, closePopUp);
	}

	public void clickStopAutoPlayStart() {
		clickOnWebElement(androidDriver, stopAutoPlayStart);
	}

	public void clickContinueMsisdnButton() {
		clickOnWebElement(androidDriver, continueMsisdnButton);
	}

	public void clickEnterMobileNumberField() {
		clickOnWebElement(androidDriver, enterMobileNumberField);
	}

	public void fillTextEnterMobileNumberField(String msisdn) {
		WaitForElement(androidDriver, enterMobileNumberField);
		type(androidDriver, enterMobileNumberField, msisdn);
	}
	
	public boolean isCroutonBarVisible() {
		try {
			WaitForElement(androidDriver, croutonBar);
			return true;
		} catch (NoSuchElementException | org.openqa.selenium.TimeoutException e) {
			return false;
		}
	}
	
	public boolean isOfflineMusicButtonVisibleOnCroutonBar() {
		try {
			WaitForElement(androidDriver, offlineMusicButton);
			return true;
		} catch (NoSuchElementException | org.openqa.selenium.TimeoutException e) {
			return false;
		}
	}
	
	public void clickOfflineMusicButton() {
		WaitForElement(androidDriver,offlineMusicButton);
		clickOnWebElement(androidDriver,offlineMusicButton);
	}
	
	//Play offline music button shows up on empty state when user is offline
	public boolean isPlayOfflineMusicButtonVisible() {
		try {
			WaitForElement(androidDriver, playOfflineMusicButton);
			return true;
		} catch (NoSuchElementException | org.openqa.selenium.TimeoutException e) {
			return false;
		}
	}
	
	public void clickPlayOfflineMusicButton() {
		WaitForElement(androidDriver,playOfflineMusicButton);
		clickOnWebElement(androidDriver,playOfflineMusicButton);
	}
	
	public boolean isOfflineMessageVisible() {
		try {
			WaitForElement(androidDriver, errorMessageInOffline);
			return true;
		} catch (NoSuchElementException | org.openqa.selenium.TimeoutException e) {
			return false;
		}
	}
	
	public boolean isOfflineSubTextVisible() {
		try {
			WaitForElement(androidDriver, errorSubTextInOffline);
			return true;
		} catch (NoSuchElementException | org.openqa.selenium.TimeoutException e) {
			return false;
		}
	}
	
	public boolean isOfflineImageVisible() {
		try {
			WaitForElement(androidDriver, errorImageInOffline);
			return true;
		} catch (NoSuchElementException | org.openqa.selenium.TimeoutException e) {
			return false;
		}
	}

	public String getTextMsisdnFromPopUp() {
		return enterMobileNumberField.getText();
	}

	public void fillTextEnterOtpField(String otp) {
		type(androidDriver, enterOtpField, otp);
	}

	public void clickOtpContinueButton() {
		clickOnWebElement(androidDriver, otpContinueButton);
	}

	public void clickEditNumberButton() {
		clickOnWebElement(androidDriver, editNumberButton);
	}

	public String getTextPopUpTitle() {
		return popUpTitle.getText();
	}

	public String getTextPopUpSubTitle() {
		return popUpSubTitle.getText();
	}

	public boolean isEnabledContinueButton() {
		try {
			WaitForElement(androidDriver, continueMsisdnButton);
			return true;
		} catch (NoSuchElementException | org.openqa.selenium.TimeoutException e) {
			return false;
		}
	}

	public void clickCallMeButton() {
		clickOnWebElement(androidDriver, callMeButton);
	}

	public boolean isEnabledCallMeButton() {
		try {
			WaitForElement(androidDriver, callMeButton);
			return true;
		} catch (NoSuchElementException | org.openqa.selenium.TimeoutException e) {
			return false;
		}
	}

	public String getTextOtpTimerLabel() {
		return otpTimerLabel.getText();
	}

	public void clickLocalMp3LaterButton() {
		clickOnWebElement(androidDriver, localMp3LaterButton);
	}

	public boolean isVisibleLocalMp3LaterButton() {
		try {
			WaitForElement(androidDriver, localMp3LaterButton);
			return true;
		} catch (NoSuchElementException | org.openqa.selenium.TimeoutException e) {
			return false;
		}
	}

	public void clickCloseWebViewButton() {
		clickOnWebElement(androidDriver, closeWebViewButton);
	}

	public boolean isVisibleWebViewAfterRegistration() {
		try {
			WaitForElement(androidDriver, closeWebViewButton);
			return true;
		} catch (NoSuchElementException | org.openqa.selenium.TimeoutException e) {
			return false;
		}
	}

	public void clickCloseDialogBox() {
		clickOnWebElement(androidDriver, closeDialogBox);
	}

	public boolean isVisibleDialogAfterRegistration() {
		try {
			WaitForElement(androidDriver, closeDialogBox);
			return true;
		} catch (NoSuchElementException | org.openqa.selenium.TimeoutException e) {
			return false;
		}
	}

	public void skipTutorial() {
		// TouchAction action = new TouchAction(androidDriver);
		// WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		// wait.until(ExpectedConditions.visibilityOf(androidDriver.findElement(By.id("download_button"))));
		// action.press(10, 10);
		pressBackButton(androidDriver);
	}

	public void selectSectionFromBottomNavigation(Navigation navigation) {
		try {
			WaitForElement(androidDriver, androidDriver.findElement(By.id("bottom_navigation_bar")));
		} catch(NoSuchElementException e) {
			pressBackButton(androidDriver);
			WaitForElement(androidDriver, androidDriver.findElement(By.id("bottom_navigation_bar")));
		}
		WebElement bottomNavigation = androidDriver.findElement(By.id("bottom_navigation_bar"));
		WebElement bottomNavigationLayout = bottomNavigation.findElement(By.className("android.widget.LinearLayout"));
		List<WebElement> bottomTabs = bottomNavigationLayout
				.findElements(By.className("android.support.v7.app.ActionBar$Tab"));

		switch (navigation) {
		case HOME: {
			bottomTabs.get(0).click();
			break;
		}
		case MY_MUSIC: {
			bottomTabs.get(1).click();
			break;

		}
		case RADIO: {
			bottomTabs.get(2).click();
			break;
		}
		case UPDATES: {
			bottomTabs.get(3).click();
			break;
		}
		case GO_PREMIUM: {
			bottomTabs.get(4).click();
			break;
		}
		case MY_ACCOUNT: {
			bottomTabs.get(4).click();
			break;
		}

		}

	}

	public void openDeeplinkUrl(String targetType, String targetID) throws IOException, InterruptedException {
		String resolvedTarget = "";

		switch (targetType) {
		case "Package":
			resolvedTarget = "/package/" + targetID + ".html";
			break;
		case "Song":
			resolvedTarget = "/song/" + targetID + ".html";
			break;
		case "Album":
			resolvedTarget = "/album/" + targetID + ".html";
			break;
		case "Settings":
			resolvedTarget = "/settings" + targetID + ".html";
			break;
		case "Artist":
			resolvedTarget = "/artist/" + targetID + ".html";
			break;
		case "Playlist":
			resolvedTarget = "/playlist/" + targetID + ".html";
			break;
		default:
			resolvedTarget = "/index.html";
			break;
		}
		executeADBShellCommand("am start -n " + getPropertyValue("/config.properties", "musicAppPackage") + "/"
				+ getPropertyValue("/config.properties", "appActivity") + " -d " + "\"android-app://"
				+ getPropertyValue("/config.properties", "musicAppPackage") + "/http://www.wynk.in/music"
				+ resolvedTarget + "\"");
		
		Thread.sleep(8000);
	}

	public void clickOnMoreButtonOfAnyRail(String sectionName) {
		Set<String> menuItems = new HashSet<String>();
		for (int i = 0; i < 20; i++) {
			List<WebElement> heading = androidDriver.findElements(By.id("tv_title"));
			int size = heading.size();
			for (int j = 0; j < size; j++) {
				menuItems.add(heading.get(j).getText());
			}
			if (menuItems.contains(sectionName)) {
				WebElement moreButtonForAlbums = androidDriver
						.findElement(By.xpath("//android.widget.TextView[contains(@text,'" + sectionName
								+ "')]/following-sibling::android.widget.TextView"));
				clickOnWebElement(androidDriver, moreButtonForAlbums);
				break;
			}
			scrollByPointer(androidDriver, 0.0001);
		}
	}

	public int getNoOfItemsPresentInRail(AndroidDriver<WebElement> androidDriver,List<WebElement> element, Direction direction, int noOfSwipes) {
		Set<String> items = new HashSet<String>();
		for (int i = 0; i <= noOfSwipes; i++) {
			int listSize = element.size();
			for (int j = 0; j < listSize; j++) {
				items.add(element.get(j).getText());
			}
			swipeRailsHorizontally(androidDriver,element, direction);
		}
		return items.size();
	}
	
	public int getNoOfItemsPresentInRail(List<WebElement> element, Direction direction, int noOfSwipes) {
		Set<String> items = new HashSet<String>();
		for (int i = 0; i <= noOfSwipes; i++) {
			int listSize = element.size();
			for (int j = 0; j < listSize; j++) {
				items.add(element.get(j).getText());
			}
			swipeRailsHorizontally(element, direction);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return items.size();
	}
	
	public Set<String> getTitleOfItemsPresentInRail(AndroidDriver<WebElement> androidDriver,List<WebElement> element, Direction direction, int noOfSwipes) {

		Set<String> titles = new HashSet<String>();
		for (int i = 0; i <= noOfSwipes; i++) {
			int listSize = element.size();
			for (int j = 0; j < listSize; j++) {
				if(!element.get(j).getText().trim().equals("EXCLUSIVE")) {
				titles.add(element.get(j).getText().trim());
				}
			}
			swipeRailsHorizontally(androidDriver,element, direction);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return titles;
	}

	
	
	/**
	 * {@link Author shubham verma} this method use for open navigator
	 * 
	 */
	public void clickOnNavigationBar() {
		hamburgerOption.click();
	}
	
	public void clicksongTitlesInFolder(int index){
		songTitlesInFolder.get(index).click();
	}
	
	public boolean isSectionAvlOnPage(WebElement section, int noOfSwipes) {
		for(int i=0;i<noOfSwipes;i++) {
			if(isWebElementDisplayed(section)) {
				return true;
			}
			scrollByPointer(androidDriver,0.0001);
		}
		return false;
	}

	public LinkedHashSet<String> getLatestLangRail(int noOfSwipes){
		LinkedHashSet<String>rails=new LinkedHashSet<>();
		selectSectionFromBottomNavigation(Navigation.HOME);
		selectSectionFromBottomNavigation(Navigation.HOME);
		for(int i=0;i<noOfSwipes;i++) {
			List<WebElement> langRails=androidDriver.findElements(By.xpath("//*[contains(@text,'Latest')]"));
			int size=langRails.size();
			for(int j=0; i<size;i++) {
				rails.add(langRails.get(j).getText());
			}
			scrollByPointer(androidDriver,0.0001);
		}
		return rails;
	}
	
	public List<String> getSelectedLangOrder(){
		List<String> order=new ArrayList<String>();
		selectSectionFromBottomNavigation(Navigation.MY_ACCOUNT);
		clickOnWebElement(androidDriver, musiclanguage);
		List<WebElement> orderOfLang=androidDriver.findElements(By.xpath("//*[contains(@resource-id,'iv_lang_selected_tick')]/preceding-sibling::android.widget.TextView"));
		int size=orderOfLang.size();
		for(int i=0;i<size;i++) {
			String lang= StringUtils.substringBefore(orderOfLang.get(i).getText(), " ");
			order.add(lang);
		}
		return order;
	}
	
	public boolean isWebElementDisplayed(WebElement ele) {
		boolean isDisplay;
		try {
			isDisplay=ele.isDisplayed();
			return isDisplay;
		}catch(NoSuchElementException e) {
			isDisplay=false;
			return isDisplay;
		}
		
	}
	
	public void selectLangAsPerConfig(List<String> selectedContentLang ) {
		String fullLangName="";
		int contentLangSize=selectedContentLang.size();
		selectSectionFromBottomNavigation(Navigation.MY_ACCOUNT);
		clickOnWebElement(androidDriver, musiclanguage);
		List<WebElement> selectedLang=androidDriver.findElements(By.xpath("//*[contains(@resource-id,'iv_lang_selected_tick')]/preceding-sibling::android.widget.TextView"));
		int selectedLangSize=selectedLang.size();
		for(int i=0;i<selectedLangSize;i++) {
			clickOnWebElement(androidDriver, selectedLang.get(i));
		}
		List<WebElement>lang=androidDriver.findElements(By.id("tv_lang_name"));
		String firstLang = getcompleteLangName(selectedContentLang.get(0));
		int langSize=lang.size();
		for(int j=0; j<langSize; j++) {
			if(lang.get(j).getText().contains(firstLang)) {
				clickOnWebElement(androidDriver, lang.get(j));
				clickOnWebElement(androidDriver, done);
				break;
			}
		}
		if(contentLangSize>1) {
			
		clickOnWebElement(androidDriver, musiclanguage);
		for(int k=1;k<contentLangSize;k++) {
			fullLangName=getcompleteLangName(selectedContentLang.get(k));
			for(int l=0;l<langSize;l++) {
			if(lang.get(l).getText().contains(fullLangName)) {
				clickOnWebElement(androidDriver, lang.get(l));
				break;
				}
			}
		}
		clickOnWebElement(androidDriver, done);
		}
	}  
	
	
	  public void clickOnSearchResultUsingSection(String sectionName, String searchValue) {
			Set<String> sectionTitles=new HashSet<>();
			boolean flag=false;
			for(int noOfScroll=1;noOfScroll<6;noOfScroll++) {
				List<WebElement> titles = androidDriver.findElements(By.id("tv_title"));
				int size = titles.size();
				for(int i=0;i<size;i++) {
					sectionTitles.add(titles.get(i).getText());
				}
				if(sectionTitles.contains(sectionName)) {
					if(sectionTitles.contains(searchValue)) {
						WebElement value = androidDriver.findElement(By.xpath("//android.widget.TextView[@text='"+searchValue+"' and contains(@resource-id,'tv_title')]"));
						clickOnWebElement(androidDriver, value);
						flag=true;
						break;
					}else {
						scrollByPointer(androidDriver,0.0001);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						WebElement value = androidDriver.findElement(By.xpath("//android.widget.TextView[@text='"+searchValue+"']"));
						clickOnWebElement(androidDriver, value);
						flag=true;
						break;
					}
					
				}
				if(flag) {
					break;
				}else{
				scrollByPointer(androidDriver,0.0001);
					}
			}
		}

	
	public String getcompleteLangName(String configLang) {
		String result="";
		switch(configLang) {
		case "hi":
			result= "Hindi";
			break;
		case "en":
			result= "English";
			break;
		case "pa":
			result= "Punjabi";
			break;
		case "bj":
			result= "Bhojpuri";
			break;
		case "te":
			result= "Telugu";
			break;
		case "ta":
			result= "Tamil";
			break;
		case "ba":
			result= "Bengali";
			break;
		case "ml":
			result= "Malayalam";
			break;
		case "gu":
			result= "Gujrati";
			break;
		case "ra":
			result= "Rajasthani";
			break;
		case "mr":
			result= "Marathi";
			break;
		case "or":
			result= "Oriya";
			break;
		case "as":
			result= "Assamese";
			break;
		case "kn":
			result= "Kannada";
			break;
		case "ha":
			result= "Haryanvi";
			break;
		}
		return result;
	}

	public void removeDialogAfterReg(CommonPages common){
		try{
			common.clickCloseDialogBox();
		}catch(Exception e){
			common.clickCloseWebViewButton();
		}
	}
	
	public void clickOnPlayIconOfAnyRail(String railName,int playIconIndex) {
		List<WebElement> playIcons=androidDriver.findElements(By.xpath("//*[@text='"+railName+"']//following-sibling::android.support.v7.widget.RecyclerView//android.widget.ImageView[contains(@resource-id,'iv_play_icon')]"));
		clickOnWebElement(androidDriver, playIcons.get(playIconIndex));
	}
	
	public void followArtistFromAnyRail(String railName,int index) {
		List<WebElement> follow=androidDriver.findElements(By.xpath("//*[contains(@text,'"+railName+"')]/following-sibling::android.support.v7.widget.RecyclerView//android.widget.ImageView[contains(@resource-id,'iv_artist_relationship')]"));
		clickOnWebElement(androidDriver, follow.get(index));
	}
}
