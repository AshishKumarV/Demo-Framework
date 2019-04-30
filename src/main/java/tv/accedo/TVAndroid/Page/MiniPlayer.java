package tv.accedo.TVAndroid.Page;

import java.util.Set;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.accedo.TVAndroid.Utils.BaseUtil;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;

public class MiniPlayer extends BaseUtil {

	public AndroidDriver<WebElement> androidDriver;
	private static Logger LOGGER = LoggerFactory.getLogger(MiniPlayer.class);

	@FindBy(id = "ll_mini_song_details_container")
	public WebElement miniPlayer;

	@FindBy(id = "mplayer_song_title")
	public WebElement miniPlayerTitle;

	@FindBy(xpath = "//android.support.v7.widget.RecyclerView[contains(@resource-id,'rv_nested_list')]//*[contains(@resource-id,'btnQueueClearAll')]")
	public WebElement clear;

	@FindBy(id = "pin_progress")
	public WebElement pinProgress;

	@FindBy(id = "mplayer_song_image")
	public WebElement miniPlayerImage;

	@FindBy(id = "mplayer_btn_play")
	public WebElement miniPlayerPlayButton;
	
	@FindBy(id = "mplayer_artist")
	public WebElement miniPlayerAlbum;

	@FindBy(id = "ll_mini_player")
	public WebElement miniPlayer2;

	

	WebDriverWait wait;

	public MiniPlayer(AndroidDriver androidDriver) {
		this.androidDriver = androidDriver;
		PageFactory.initElements(androidDriver, this);
	}

	public void closeMiniPlayer() {
		if (miniPlayer.isDisplayed()) {
			clickOnWebElement(androidDriver, miniPlayer);
			scrollByPointer(androidDriver);
			String text = "Player Queue";
			Set<String> s = androidDriver.getContextHandles();

			for (String stringElement : s) {
				LOGGER.info("context" + stringElement);
			}

			WebElement el = androidDriver
					.findElement(MobileBy.AndroidUIAutomator("new UiSelector().textContains(\"" + text + "\")"));
			clickOnWebElement(androidDriver, clear);
		}

	}

	
	public boolean isVisibleMiniPlayer() {
		try {
			WaitForElement(androidDriver, miniPlayerTitle);
			return true;
		} catch (NoSuchElementException | org.openqa.selenium.TimeoutException e) {
			return false;
		}
	}

	public void clickOnMiniPlayer() {
		try {
			clickOnWebElement(androidDriver, miniPlayerTitle);
		} catch (TimeoutException e) {
			LOGGER.info("mini player not displayed");
		}
	}
	
	// this player contain new id 
	public void clickOnMiniPlayer_beta()
	{
		clickOnWebElement(androidDriver, miniPlayer2);
	}

	public boolean clickOnMiniPlayer_V2() {
		try {
			if (verifyIfElementIsPresent(androidDriver, miniPlayerTitle)) {
				clickOnWebElement(androidDriver, miniPlayerTitle);
				return true;
			}
		} catch (NoSuchElementException e) {
			LOGGER.info("mini player not displayed");
		}
		return false;
	}
	

	public String getCurrentSongPlayed() {
	    WaitForElement(androidDriver, miniPlayerTitle);
		String currentSong = miniPlayerTitle.getAttribute("text");
		return currentSong;
	}
	

	public String getPinProgressText() {
		WaitForElement(androidDriver, pinProgress);
		return pinProgress.getAttribute("name");
	}

	public void waitTillAutoPlayStarts() {
		try {
			WaitForElement(androidDriver, miniPlayerPlayButton);
		} catch (TimeoutException e) {
			LOGGER.info("mini player not displayed");
		}
	}
	
	public String getMiniPlayerAlbum() {
		WebElement miniPlayerText = WaitForElement(androidDriver, miniPlayerAlbum);
		String currentSong = miniPlayerText.getAttribute("text");
		return currentSong;
	}

}
