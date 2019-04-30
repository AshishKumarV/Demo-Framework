package tv.accedo.TVAndroid.Page;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;
import tv.accedo.TVAndroid.Utils.BaseUtil;


public class DownloadPage extends BaseUtil{
	

	public AndroidDriver<WebElement>  androidDriver;
	private static final Logger LOGGER =  Logger.getLogger(DownloadPage.class);
	
	@FindBy(xpath=("//android.widget.FrameLayout[contains(@resource-id,'ll_item_actions')]/*[contains(@class,'android.widget.LinearLayout')]"
			+ "/*[contains(@class,'android.widget.FrameLayout')]/android.widget.TextView[contains(@resource-id,'tv_item_action_1')]"))
	public WebElement downloadAll;
	
	@FindBy(xpath="//android.widget.ImageView[contains(@resource-id,'iv_empty')]")
	public WebElement noDownloadedSongs;
	
	@FindBy(xpath="//android.widget.TextView[contains(@resource-id,'tv_dialog_message')]")
	public WebElement downloadMoreThan500Songs;
	
	
	@FindBy(xpath="//android.widget.TextView[contains(@resource-id,'btn_dialog_2')]")
	public WebElement downloadButtonOnDialogBox;
	
	@FindBy(xpath="//android.widget.TextView[contains(@resource-id,'tv_item_action_1')]")
	public WebElement downloadButton;
	
	
	@FindBy(id="tv_empty_text")
	public WebElement emptyPlaylist;
	
	@FindBy(id="pin_progress")
	public WebElement pinProgress;

	@FindBy(id="title")
	public List<WebElement> menuItels;
	
	@FindBy(id="tv_item_subtitle")
	public WebElement songCountTitle;
	
	//mapping_desc
	@FindBy(id="tv_mapping_finished_desc")
	public WebElement mappingDesc;
	
	@FindBy(id="tv_empty_text")
	public WebElement blankUnfinishedDownload;
	
	@FindBy(id="tv_empty_btn")
	public WebElement scanNowBtn;

	@FindBy(id="song_album")
	public List<WebElement> playListSubTitle;
	
	@FindBy(id="iv_follow_icon")
	public WebElement followedButton;
	
	@FindBy(id="tv_dialog_message")
	public WebElement followedPopperMsg;
	
	@FindBy(id="iv_close_dialog")
	public WebElement closeFollowedPopUp;
	
	@FindBy(id="tv_view_all")
	public WebElement redioMoreLink;
	
	@FindBy(id="iv_radio_image")
	public WebElement radoiStation;

	@FindBy(id="player_btn_play_stop")
	public WebElement stopRadio;
	
	WebDriverWait wait;
	
	
	public void playFirstIndexStationOfGrid() throws InterruptedException {
		Thread.sleep(2000);
		redioMoreLink.click();  
		Thread.sleep(2000);
		radoiStation.click();
		Thread.sleep(2000);
	}
	
	public void stopAndCloseRadio() {
		stopRadio.click();
	}
	
	public void followArtist() {
		followedButton.click();
	}
	
	public boolean isFollowedPopperMsgVisible() {
		return followedPopperMsg.isDisplayed();
	}
	
	public void closePopper() {
		closeFollowedPopUp.click();
	}
	
	public DownloadPage(AndroidDriver androidDriver)
	{
		this.androidDriver = androidDriver;
		//wait = new WebDriverWait(androidDriver, 30);
		PageFactory.initElements(androidDriver, this);
		//PageFactory.initElements(new AppiumFieldDecorator(androidDriver),this);
	}
	
	
	public boolean verifyDownloadSongsPlayListEmpty() throws InterruptedException{
		try {
		WaitForElement(androidDriver, noDownloadedSongs);
		} catch (org.openqa.selenium.TimeoutException e) {
			return false;
		}
		if(noDownloadedSongs.isEnabled()){
			LOGGER.info("playlist is empty");
			return true;
		}
		return false;
	}
	
	
	public boolean verifyDownloadMoreThan500Songs() throws InterruptedException
	{
		WaitForElement(androidDriver, downloadMoreThan500Songs);
		String popUpText = downloadMoreThan500Songs.getAttribute("text");
		LOGGER.info("Pop Up text"+popUpText);
		if(popUpText.contains("Downloading top 500 songs for you now.")) {
			return true;
		}
		return false;
	}
	
	public void clickOnDownloadButtonOnDialogBox(){
		downloadButtonOnDialogBox.click();
	}
	
	public String getDownAllButtontext(){
		String status= downloadAll.getAttribute("text");
		LOGGER.info("Download status"+status);
		return status;
	}
	
	public boolean verifyEmptyPlaylist() {
		try {
			WebDriverWait wait = new WebDriverWait(androidDriver,120);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tv_empty_text")));
			return true;} catch(NoSuchElementException | TimeoutException e) {
				LOGGER.info("empty playlist is not displayed ");
				return false;
			}
	}
	
	
	public boolean verifyIntialEmptyPlaylist() {
		try {
			WebDriverWait wait = new WebDriverWait(androidDriver,10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tv_empty_text")));
			return true;} catch(NoSuchElementException | TimeoutException e) {
				LOGGER.info("empty playlist is not displayed ");
				return false;
			}
	}

	
	
	public void waitForDownloadStatus(String status){
		WebDriverWait wait = new WebDriverWait(androidDriver,300);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[contains(@text,'"+status+"')]"))); 
	}
	

	public void waitForUnfinishedDownloadedEmptyPage(){
		WebDriverWait wait = new WebDriverWait(androidDriver,240);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("iv_empty"))); 
	}
	
	public int getSizeOfRental(String rentedPath)
	{   	
		LinkedHashSet<String> songsCachedSize = new LinkedHashSet<>();
		songsCachedSize = executeADBCommandToFetchData("ls -s " + rentedPath);
		List<String> list = new ArrayList<String>(songsCachedSize);
		int size = Integer.parseInt(list.get(0).substring(6));
		
		return size;	
	}

	
	public int getFilesCountOfRental(String rentedPath)
	{   	
		LinkedHashSet<String> songsCachedCount = new LinkedHashSet<>();
		songsCachedCount = executeADBCommandToFetchData("ls -l " + rentedPath + " | wc -l");
		List<String> list = new ArrayList<String>(songsCachedCount);
		if(list.get(0) != null) {
			return Integer.parseInt(list.get(0));	
		}else
			return 0;
		
	}
	
	public boolean isElementPresent(String name) {
		List<WebElement> menuItems = androidDriver.findElements(By.id("title"));
		for(WebElement mt : menuItems) {
			if(mt.getText().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	
	public void resetRedownloadQuality(String quality) {
		androidDriver.findElement(By.xpath("//android.widget.CheckedTextView[contains(@text,'"+quality+"')]")).click();
		androidDriver.findElement(By.id("btn_dialog_2")).click();
	}
	
	
	public boolean waitForDownloadPinStatus(String status){
		try {
			WebDriverWait wait = new WebDriverWait(androidDriver,60);
			//LOGGER.info("status of element is : "+ androidDriver.findElement(By.xpath("//android.widget.CompoundButton[contains(@content-desc,'"+status+"')]")).getAttribute("text"));
			for(int i = 0; i<40;i++) {
				if(pinProgress.getAttribute("name").equalsIgnoreCase(status)) {
					break;
				} else
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
			//wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.CompoundButton[contains(@content-desc,'"+status+"')]")));
			//LOGGER.info("status of element is : "+ androidDriver.findElement(By.xpath("//android.widget.CompoundButton[contains(@name,'"+status+"')]")).getAttribute("text"));
			return true;
		} catch(NoSuchElementException | TimeoutException e) {
			LOGGER.info(status +" is not displayed for download pin progress");
			return false;
		}
	}
	
	public String getElementText(WebElement blankUnfinishedDownload) {
		String text = blankUnfinishedDownload.getText();
		return text;
	}
	
	
	public void removeRentedFolder(String path) {
		try {
			executeADBCommandToFetchData("rm -r " + path);
		}catch(Exception e) {
			LOGGER.info(path + " Path not found.");
		}
		
	}
	
	public void waitForElementLoad(String path){
		WebDriverWait wait = new WebDriverWait(androidDriver,240);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(path))); 
	}

	public void waitForWebElement(WebElement webElement) {
		WebDriverWait wait = new WebDriverWait(androidDriver, 30);
		wait.until(ExpectedConditions.visibilityOf(webElement));
	}
	
	
	public void clickScanNowBtn(){
		clickOnWebElement(androidDriver, scanNowBtn);
	}
	
	public void addLocalSongsToDevice(CommonPages commonPages){
		String fromLocation = System.getProperty("user.dir") + "/localMp3";
		String toLocation = "/sdcard/Audio";
		addLocalSongsToDevice(commonPages , fromLocation , toLocation);
	}
	
	public Set<String> getSetOfArtistNameFromSubTitle(List<WebElement> subTitle){
		Set<String> set = new HashSet<>();
		for(int i=0; i<5; i++) {
			List<WebElement> artistList = subTitle;
			for(WebElement singer : artistList) {
				String[] sub = singer.getText().split(" - ");
				set.add(sub[0]);
			}
			scrollByPointer(androidDriver, 0.0001);
		}		
		return set;
	}
	
	public void clickOnDownloadButtonPlayer() {
		WaitForElement(androidDriver, pinProgress);
		pinProgress.click();
	}

}
