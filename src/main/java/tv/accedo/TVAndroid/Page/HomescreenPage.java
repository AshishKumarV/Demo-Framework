
package tv.accedo.TVAndroid.Page;

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
import tv.accedo.TVAndroid.Utils.BaseUtil;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;

public class HomescreenPage extends BaseUtil{
	private AndroidDriver<WebElement> androidDriver;
	private static final Logger LOGGER =  Logger.getLogger(HomescreenPage.class);

	public HomescreenPage(AndroidDriver androidDriver){
		this.androidDriver = androidDriver;
		PageFactory.initElements(androidDriver, this);
	}
	CommonPages common=new CommonPages(androidDriver);
	
	@FindBy(id="tv_mood_name")
	public List<WebElement> relaxedScreenCard;
	
	@FindBy(id="tv_playlist_name")
	public List<WebElement> playListCard;
	
	@FindBy(xpath="//android.widget.LinearLayout[contains(@resource-id,'ll_search_parent')]//*[contains(@class,'android.widget.TextView')]")
	public WebElement searchTab;

	@FindBy(xpath="//android.widget.EditText[contains(@resource-id,'searchTextView')]")
	public WebElement editSearch;

	@FindBy(xpath="//android.widget.ImageView[contains(@resource-id,'action_up_btn')]")
	public WebElement backFromSearch;

	@FindBy(xpath="//android.widget.ListView[contains(@resource-id,'lv_results_list')]/*[contains(@class,'android.widget.FrameLayout')][1]")
	public WebElement firstSearchResult;

	@FindBy(xpath="//android.view.View[contains(@resource-id,'gradient_view')]/..")
	public WebElement test;

	@FindBy(id="tv_view_all")
	public WebElement more;
	
	@FindBy(xpath = "//android.widget.RelativeLayout[contains(@id,'rl_main_container')]/android.widget.TextView[contains(@id,'tv_title')]")
	public WebElement firstPlayList;

	@FindBy(id="tv_hide")
	public WebElement hide;

	@FindBy(id="tv_view_all_lang")
	public WebElement viewAll;

	@FindBy(id="tv_lang")
	public WebElement musiclanguagecard;

	@FindBy(id="iv_single_image")
	public WebElement songTile;

	@FindBy(id="iv_navigation_up")
	public WebElement hamburgerIcon;
	
	@FindBy(id="btn_dialog_cancel")
	public WebElement MP3ImportCancel;
	
	@FindBy(xpath="//*[@text='Latest Hindi ']//following-sibling::android.support.v7.widget.RecyclerView//android.widget.TextView")
	public List<WebElement> latestHindiRail;
	
	@FindBy(xpath="//*[@text='Wynk Top 100']//following-sibling::android.support.v7.widget.RecyclerView//android.widget.TextView")
	public List<WebElement> wynkTop100Rail;
	
	@FindBy(xpath="//*[@text='Recommended Artists']//following-sibling::android.support.v7.widget.RecyclerView//android.widget.TextView")
	public List<WebElement> artistsRail;
	
	@FindBy(id="tv_item_title")
	public WebElement pageTitle;
	
	@FindBy(id="tv_item_subtitle")
	public WebElement allSongsCount;
	
	@FindBy(xpath="//*[@text='Top Playlists']//following-sibling::android.support.v7.widget.RecyclerView//android.widget.TextView")
	public List<WebElement> topPlaylistsRail;
	
	@FindBy(xpath="//*[@text='Top Albums']//following-sibling::android.support.v7.widget.RecyclerView//android.widget.TextView")
	public List<WebElement> topAlbumsRail;
	
	@FindBy(xpath="//*[@text='Moods']//following-sibling::android.support.v7.widget.RecyclerView//android.widget.TextView")
	public List<WebElement> moodsRail;
	
	@FindBy(xpath="//*[@text='Followed Artists']//following-sibling::android.support.v7.widget.RecyclerView//android.widget.TextView")
	public List<WebElement> follwdArtistRail;
	
	@FindBy(id="tv_mood_name")
	public List<WebElement> moodsTitle;
	
	@FindBy(id= "rv_grid")
	public WebElement gridViewOfSection;
	
	@FindBy(id="tv_my_music")
	public WebElement fromMyMusic;
	
	@FindBy(id="tv_view_all_lang")
	public WebElement viewAllLang;
	
	@FindBy(id="tv_dialog_title")
	public WebElement selectLangScreen;
	
	@FindBy(id="tv_default_text")
	public WebElement langScreenDefaulTxt;
	
	@FindBy(id="iv_close_dialog")
	public WebElement langScrCrossBtn;
	
	@FindBy(id="tv_lang_name")
	public List<WebElement> langName;
	
	@FindBy(xpath="//*[contains(@resource-id,'iv_lang_selected_tick')]/preceding-sibling::android.widget.TextView")
	public List<WebElement> selectedLang;
	
	@FindBy(id="tv_done")
	public WebElement done;
	
	@FindBy(xpath="//*[@text='View All Settings']")
	public List<WebElement> viewAllSettings;
	
	@FindBy(xpath="//*[@class='android.widget.ImageButton']/following-sibling::android.widget.TextView")
	public WebElement currentPgTitle;
	
	@FindBy(id="tv_playlist_name")
	public List<WebElement> playlistInMoods;
	
	@FindBy(id="iv_play_icon")
	public List<WebElement> playIcon;
	
	@FindBy(id="tv_player_title")
	public WebElement playerTitle;
	
	@FindBy(id="iv_artist_relationship")
	public List<WebElement> followPlusIcon;
	
	@FindBy(id="iv_cross")
	public WebElement crossOnBanner;
	
	@FindBy(id="iv_playlist_image")
	public WebElement playlistTile;
	
	@FindBy(id="iv_mood_image")
	public WebElement moodTile;
	
	@FindBy(id="tv_first")
	public List<WebElement> sectionsInMyMusic;
	
	@FindBy(xpath="//*[contains(@resource-id,'ll_my_music')]/following-sibling::android.widget.FrameLayout//android.widget.TextView")
	public List<WebElement> viewAllOfMyMusic;
	
	@FindBy(id="ll_setting_item_container")
	public List<WebElement> itemsOfQuickSettings;
	
	@FindBy(id="switch_toggle")
	public WebElement sleepTimer;
	
	@FindBy(id = "rl_global_notification")
	public WebElement croutonBar;
	
	@FindBy(id = "action_voice_btn")
	public WebElement voiceSearchIcon;
	
	@FindBy(id = "permission_allow_button")
	public WebElement voiceSearchPermission;
	
	@FindBy(id = "featured_content_image")
	public WebElement featuredBanner;
	
	@FindBy(id = "ttv_navigation_item_title")
	public List<WebElement> hamburgerMenuOptions;
	
	@FindBy(id = "cb_nav")
	public WebElement hamburgerDSMOption;
	
	@FindBy(id = "refer_cta")
	public WebElement referNow;
	
	@FindBy(id = "btn_setting_player")
	public WebElement playPauseButton;
	
	@FindBy(id = "tv_radio_name")
	public List<WebElement> myPersonalStation;
	
	@FindBy(id = "iv_radio_play")
	public WebElement radioPlayIcon;
	
	@FindBy(id = "rl_play_my_station")
	public WebElement myStation;
	
	@FindBy(id = "tv_data_save_header")
	public WebElement dsmHeader;
	
	@FindBy(id = "iv_global_notification_close")
	public WebElement offlineMusic;
	
	

	public void clickFeaturedBanner(){
		this.featuredBanner.click();
	}
	
	
	public String getFirstPlaylistName() {
		return firstPlayList.getText().trim();
	}

	public void clickSearchTab(){
		WaitForElement(androidDriver, searchTab);
		clickOnWebElement(androidDriver,searchTab);

	}

	public void searchTerm(String searchTerm){
		editSearch.sendKeys(searchTerm);
		androidDriver.pressKeyCode(AndroidKeyCode.BACK);
	}

	public void closeSearchBar()
	{
		boolean flag = false;
		try {
			flag=verifyIfElementIsPresent(androidDriver, backFromSearch);
			if(flag == true) {
				backFromSearch.click();
			}
			backFromSearch.click();
		} catch(Exception e) {
			LOGGER.info("Search bar not activiated");
		}
	}

	public void clickOnFirstSearchResult() {
		clickOnWebElement(androidDriver,firstSearchResult);
	}

	public void scrollToCategoryAndClick(String categoryName) {
		WebElement e = scrollToElementByVisibleText(androidDriver, categoryName);
		List<WebElement> relativeLayouts = androidDriver.findElements(By.xpath("//android.support.v7.widget.RecyclerView[contains(@resource-id,'rv_homefeed')]"
				+ "//android.widget.RelativeLayout[contains(@resource-id,'rl_main_container')]"));
		int i=0;
		boolean flag = false;

		while(i==0 && relativeLayouts.iterator().hasNext()) {
			WebElement relativeLayout = relativeLayouts.iterator().next();
			i++;
			String text = relativeLayout.findElement(By.xpath("/android.widget.TextView[contains(@resource-id,'tv_title')]")).getAttribute("text");
			LOGGER.info("category name"+text);

			if(text.equalsIgnoreCase(categoryName)) {
				e= relativeLayout;
				e.findElement(By.xpath("//android.support.v7.widget.RecyclerView[contains(@resource-id,'rv_rail')]"))
				.findElement(By.xpath("/*[contains(@class,'android.widget.FrameLayout')][1]")).click();
				flag=true;
			}	    
		}
	}

	public void clickOnViewMoreOfFirstFrameLayout() {
		try {
			clickOnWebElement(androidDriver, more);
		} catch( org.openqa.selenium.TimeoutException| NoSuchElementException e){
			scrollByPointer(androidDriver, 0.05);
			clickOnWebElement(androidDriver, more);
		}
	}

	public boolean verifyIfLanguageRailFeedIsPresent(String language)
	{
		try
		{
			WebElement e = androidDriver.findElement(By.xpath("//android.widget.RelativeLayout[contains(@resource-id,'rl_main_container')]/android.widget.TextView[contains(@text,'Latest "+language+"')]"));
			return true;
		}
		catch(NoSuchElementException e)
		{
			try
			{
				scrollByPointer(androidDriver,0.05);
				WebElement e1 = androidDriver.findElement(By.xpath("//android.widget.RelativeLayout[contains(@resource-id,'rl_main_container')]/android.widget.TextView[contains(@text,'Latest "+language+"')]"));
				return true;
			}
			catch(NoSuchElementException e1)
			{
				try 
				{
					scrollToElementByVisibleText(androidDriver,language);
					return true;
				}
				catch(NoSuchElementException e2)
				{
					return false;
				}
			}

		}

	}

	public Set<String> getAllLanguageRailFeed()
	{
		List<WebElement> railFeedLayOut = androidDriver.findElements(By.xpath("//android.support.v4.widget.DrawerLayout[contains(@resource-id,'dl_navigation_drawer_container')]//android.support.v7.widget.RecyclerView[contains(@resource-id,'rv_homefeed')]/android.widget.RelativeLayout[contains(@resource-id,'rl_main_container')]"));
		Set<String> languageRailSet = new HashSet<String>();
		for(int i =0;i<railFeedLayOut.size();i++)
		{
			if(railFeedLayOut.get(i).findElement(By.xpath("//*[contains(@class,'android.widget.TextView')]")).getAttribute("text").equalsIgnoreCase("Wynk Top 100")) 
			{
				System.out.println(railFeedLayOut.get(i).findElement(By.xpath("//*[contains(@class,'android.widget.TextView')]")).getAttribute("text"));
				return languageRailSet;
			}
			else 
			{
				String text =railFeedLayOut.get(i).findElement(By.xpath("//*[contains(@class,'android.widget.TextView')]")).getAttribute("text");
				//String text =railFeedLayOut.get(i).findElement(By.xpath("/android.widget.TextView[0]")).getAttribute("text");
				System.out.println(text);
				if(i == railFeedLayOut.size()-1)
				{
					languageRailSet.add(text);
					scrollByPointer(androidDriver, 0.6);
					railFeedLayOut = androidDriver.findElements(By.xpath("//android.support.v4.widget.DrawerLayout[contains(@resource-id,'dl_navigation_drawer_container')]//android.support.v7.widget.RecyclerView[contains(@resource-id,'rv_homefeed')]/android.widget.RelativeLayout[contains(@resource-id,'rl_main_container')]"));
					i=1;
				}
				else
				{
					languageRailSet.add(text);
				}

			}		    	
		}
		return languageRailSet; 
	}

	//click on More button of any section on home page
	public void clickOnMoreButtonOfRailFeed(String section) {
		if(section.contains("Artists")) {
			scrollByPointer(androidDriver,0.06);
		}

		scrollToElementByVisibleTextAndClick(androidDriver, section);
		androidDriver.findElement(By.xpath("//android.widget.TextView[contains(@text,'"+section+"')]/../android.widget.TextView[contains(@text,'More')]")).click();
	}


	public void clickFirstSongTile(){
		List<WebElement> songTiles = androidDriver.findElements(By.id("iv_single_image"));
		songTiles.get(0).click();
	}
	
	public void clickFirstPlaylistTile(){
		List<WebElement> playlistTiles = androidDriver.findElements(By.id("iv_playlist_image"));
		playlistTiles.get(0).click();
	}
	
	public void clickFirstMoodTile(){
		List<WebElement> playlistTiles = androidDriver.findElements(By.id("iv_mood_image"));
		playlistTiles.get(0).click();
	}
	
	public void clickFirstArtistTile(){
		List<WebElement> artistTiles = androidDriver.findElements(By.id("iv_artist_image"));
		artistTiles.get(0).click();
	}

	public String getTextSongTitleByIndex(int index){
		List<WebElement> songTiles = androidDriver.findElements(By.id("tv_single_name"));
		return songTiles.get(index).getText();
	}

	public void clickOnViewAllForLangCard()
	{
		WebElement e = scrollToElementByVisibleText(androidDriver, "View All Languages");
		e.click();
	}

	public boolean isVisibleFeaturedBanner(){
		try{
			WaitForElement(androidDriver, featuredBanner);
			return true;
		}
		catch(NoSuchElementException | org.openqa.selenium.TimeoutException e){
			return false;
		}
	}

	public boolean isVisibleSearchTab(){
		try{
			WaitForElement(androidDriver, searchTab);
			return true;
		}
		catch(NoSuchElementException | org.openqa.selenium.TimeoutException e){
			return false;
		}
	}
	
	public LinkedHashSet<String> getAllSectionsTitle(){
		LinkedHashSet<String> menuItems= new LinkedHashSet<String>();
		
		for(int i=0;i<25;i++) {
			List<WebElement> heading = androidDriver.findElements(By.id("tv_title"));
			int size = heading.size();
			for (int j=0;j<size;j++) {
				menuItems.add(heading.get(j).getText().trim());
			}
			scrollByPointer(androidDriver, 0.0001);
		}
		
	        return menuItems;
	}
	
	public String getPageTitle() {
		WaitForElement(androidDriver, pageTitle);
		return pageTitle.getText().trim();
	}
	
	public  String getAllSongsCount() {
		WaitForElement(androidDriver, allSongsCount);
		System.out.println("All songs count is:"+StringUtils.substringBetween(allSongsCount.getText(), "Followers | ", " songs").trim());
		return StringUtils.substringBetween(allSongsCount.getText(), "Followers | ", " songs").trim();
	}
	
	public int getRecoSongsCount() {
		WaitForElement(androidDriver, allSongsCount);
		String text=allSongsCount.getText().toString();
		int count=Integer.parseInt(StringUtils.substringBefore(text, " "));
		return count;
	}
	
	public Set<String> getArtistsTitleInMore(int noOfSwipes){
		Set<String> titles= new HashSet<String>();
		for(int i=0;i<noOfSwipes;i++) {
			List<WebElement> heading = androidDriver.findElements(By.id("tv_artist_name"));
			int size = heading.size();
			for (int j=0;j<size;j++) {
				titles.add(heading.get(j).getText().trim());
			}
			scrollByPointer(androidDriver, 0.0001);
		}
		
	        return titles;
	}
	
	public void cancelMP3Import() {
		try {
			if(MP3ImportCancel.isEnabled()) {
				MP3ImportCancel.click();
			}
		} catch(NoSuchElementException e) {
			LOGGER.info("mp3 import pop up not present");
		}
	}

	public Set<String> getTitleOfMoods( int noOfSwipes) {
		Set<String> titles = new HashSet<>();
		for(int i=0;i<noOfSwipes;i++) {
			List<WebElement> type = androidDriver.findElements(By.id("tv_mood_name"));
			int size = type.size();
			for (int j=0;j<size;j++) {
				titles.add(type.get(j).getText().trim());
			}
			scrollByPointer(androidDriver, 0.0001);
		}
		
	        return titles;
	}
	
	public void selectALanguage(String lang ) {
		if(!isLangSelected(lang)) {
			clickOnWebElement(androidDriver, viewAllLang);
			
			int size=langName.size();
			for(int i=0; i<size; i++) {
				if(langName.get(i).getText().contains(lang)) {
					clickOnWebElement(androidDriver, langName.get(i));
					clickOnWebElement(androidDriver, done);
					break;
				}
			}
		}else {
			clickOnWebElement(androidDriver, viewAllLang);
			for(int i=0;i<2;i++) {
				int size=langName.size();
				for(int j=0; j<size; j++) {
					if(langName.get(j).getText().contains(lang)) {
						clickOnWebElement(androidDriver, langName.get(j));
						clickOnWebElement(androidDriver, done);
						break;
					}
				}
				
			}
		}
	}
	
	public boolean isLangSelected(String lang) {
		try {
			WebElement langSelected=androidDriver.findElement(By.xpath("//*[contains(@text,'"+lang+"')]/following-sibling::android.widget.ImageView"));
			return langSelected.isEnabled();
		}catch(NoSuchElementException e) {
			return false;
		}
	}
	
	public boolean isViewAllLangDisplayed() {
		return common.isWebElementDisplayed(viewAllLang);
	}
	
	public boolean isLangCardDisplayed() {
		return common.isWebElementDisplayed(musiclanguagecard);
	}
	
	public boolean isHideLblDisplayed() {
		return common.isWebElementDisplayed(hide);
	}
	
	public void clickViewAllLang() {
		clickOnWebElement(androidDriver, viewAllLang);
	}
	
	public String getTitleOfLangScreen() {
		return selectLangScreen.getText().trim();
	}
	
	public String getDefaultText() {
		return langScreenDefaulTxt.getText().trim();
	}
	
	public void clickCrossBtn() {
		clickOnWebElement(androidDriver, langScrCrossBtn);
	}
	
	public int getSelectedLangSize(){
		List<String>name = new ArrayList<>();
		int size=selectedLang.size();
		for(int i=0; i<size;i++) {
			name.add(selectedLang.get(i).getText());
		}
		return name.size();
	}
	
	public void clickOnSelectedLang() {
		int size=getSelectedLangSize();
		clickOnWebElement(androidDriver, selectedLang.get(size-1));
	}
	
	public void validateFifthLangSelection() {
		int size=getSelectedLangSize();
		if(size==4) {
			clickOnWebElement(androidDriver, langName.get(size));
			clickOnWebElement(androidDriver, done);
		}else if (size<4) {
			for(int i=size;i<4;i++) {
				clickOnWebElement(androidDriver, langName.get(i));
				clickOnWebElement(androidDriver, done);
			}
		}
	}
	
	public void updateSelectedLangToOne() {
		int size=getSelectedLangSize();
		if(size>1) {
			for(int i=1;i<4;i++) {
				clickOnWebElement(androidDriver, langName.get(i));
			}
			clickOnWebElement(androidDriver, done);
		}
	}
	
	public boolean isGridViewDisplayed() {
		WaitForElement(androidDriver, gridViewOfSection);
		return common.isWebElementDisplayed(gridViewOfSection);
	}
	
	public void clickOnFirstMoodOfGrid() {
		clickOnWebElement(androidDriver, moodsTitle.get(0));
	}
	
	public String getTitleOfFirstMood() {
		return moodsTitle.get(0).getText().trim();
	}
	
	public String getCurrentPageTitle() {
		WaitForElement(androidDriver, currentPgTitle);
		return currentPgTitle.getText().trim();
	}
	
	public void clickOnFirstPlaylistInMoods() {
		clickOnWebElement(androidDriver, playlistInMoods.get(0));
	}
	
	public String getTiltleOfFirstPlaylistInMood() {
		return playlistInMoods.get(0).getText().trim();
	}
	
	public void clickOnPlayIcon() {
		clickOnWebElement(androidDriver, playIcon.get(0));
	}
	
	public boolean isPlayerDisplayed() {
		WaitForElement(androidDriver, playerTitle);
		return common.isWebElementDisplayed(playerTitle);
	}
	
	public void followArtist(int index) {
		// sleep required because sometimes it opens the artist page
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clickOnWebElement(androidDriver, followPlusIcon.get(index));
	}
	
	public void goToRailOnScreen(String railName) {
		List<WebElement> rail=androidDriver.findElements(By.xpath("//*[@text='"+railName+"']//following-sibling::android.support.v7.widget.RecyclerView//android.widget.TextView"));
		while(rail.isEmpty()) {
			scrollByPointer(androidDriver, 0.0001);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			rail=androidDriver.findElements(By.xpath("//*[@text='"+railName+"']//following-sibling::android.support.v7.widget.RecyclerView//android.widget.TextView"));

		}
	}
	
	public Set<String> getItemsTitleFromRail(String railName){
		List<WebElement> rail=androidDriver.findElements(By.xpath("//*[@text='"+railName+"']//following-sibling::android.support.v7.widget.RecyclerView//android.widget.TextView"));
		return common.getTitleOfItemsPresentInRail(androidDriver,rail, Direction.RIGHTTOLEFT, 8);
	}
	
	public void closeWynkBanner() {
		try {
			crossOnBanner.click();
		} catch (NoSuchElementException e) {
			LOGGER.info("Brancd New Wynk Banner not present on home page");
		}
	}
	

	public void clickOnAFollowedArtistFromRail() {
		clickOnWebElement(androidDriver, follwdArtistRail.get(1));
	}
	
	public String getAFollowedArtistName() {
		return follwdArtistRail.get(1).getText().trim();
	}
	
	public boolean isFollowedArtistRailDisplayed() {
		return isElementDisplayed(androidDriver, follwdArtistRail.get(0));
	}
	

	public void clickOnRelaxedCards(int index)
	{
		clickOnWebElement(androidDriver, relaxedScreenCard.get(index));
		
	}
	
	public void clickOnPlaylistCard(int index)
	{
		clickOnWebElement(androidDriver, playListCard.get(index));
	}
	
	public void clickOnPlaylistInTopPlaylistRail(int index) {
		clickOnWebElement(androidDriver, topPlaylistsRail.get(index));
	}
	
	public void clickOnArtistInRecoArtistRail(int index) {
		clickOnWebElement(androidDriver, artistsRail.get(index));

	}

	public void goToMusicLanguageCard() {
		while(!isViewAllLangDisplayed()) {
			scrollByPointer(androidDriver,0.0001);
		}
	}
	
	public void clickAnySectionOfMyMusicCard(int rowNo) {
		clickOnWebElement(androidDriver, sectionsInMyMusic.get(rowNo));
	}
	
	public void clickOnViewAllOfMyMusicCard() {
		clickOnWebElement(androidDriver, viewAllOfMyMusic.get(0));
	}
	
	public void goToMyMusicCard() {
		while(viewAllOfMyMusic.isEmpty()) {
			scrollByPointer(androidDriver,0.0001);
		}
	}
	
	public void goToQuickSettings() {
		while(viewAllSettings.isEmpty()) {
			scrollByPointer(androidDriver,0.0001);
		}
	}
	
	public void clickAnyItemsOfQuickSettings(int rowNo) {
		clickOnWebElement(androidDriver, itemsOfQuickSettings.get(rowNo));
	}
	
	public void clickOnSleepTimer() {
		clickOnWebElement(androidDriver, sleepTimer);
	}
	
	public void clickOnHamburgerIcon() {
		clickOnWebElement(androidDriver, hamburgerIcon);
	}
	
	public void clickAHamburgerMenuOption(int index) {
		clickOnWebElement(androidDriver, hamburgerMenuOptions.get(index));
	}
	
	public void clickDSMOptionFromHamburger() {
		clickOnWebElement(androidDriver, hamburgerDSMOption);
	}
	
	public int getHamburgerMenuOptionsSize() {
		clickOnHamburgerIcon();
		return hamburgerMenuOptions.size();
	}
	
	public void clickReferNowOptionFromHamburger() {
		clickOnWebElement(androidDriver, referNow);
	}
	
	public void clickVoiceSearchIcon() {
		clickOnWebElement(androidDriver, voiceSearchIcon);
	}
	
	public void clickLanguageFromLangCard(int index) {
		clickOnWebElement(androidDriver, langName.get(index));
	}
	
	public int getSizeOfLanguageFromLangCard() {
		return langName.size();
	}
	
	public void clickOnPlayPauseButton() {
		clickOnWebElement(androidDriver, playPauseButton);
	}
	
	public void unFollowArtistFromFollowedRail(int index) {
		List<WebElement> follow=androidDriver.findElements(By.xpath("//*[contains(@text,'Followed Artists')]/following-sibling::android.support.v7.widget.RecyclerView//android.widget.ImageView[contains(@resource-id,'iv_artist_relationship')]"));
		clickOnWebElement(androidDriver, follow.get(index));
	}
	
	public void goToMyPersonalStation() {
		while(myPersonalStation.isEmpty()) {
			scrollByPointer(androidDriver, 0.0001);
		}
	}
	
	public void clickPlayIconOfRadio() {
		clickOnWebElement(androidDriver, radioPlayIcon);
	}
	
	public void clickMyStation() {
		clickOnWebElement(androidDriver, myStation);
	}
	
	public void clickOnDsmHeader() {
		clickOnWebElement(androidDriver, dsmHeader);
	}
	
	public void clickOnOfflineMusicHeader() {
		clickOnWebElement(androidDriver, offlineMusic);
	}
	
	public boolean isDsmHeaderDisplayed() {
		return isElementDisplayed(androidDriver, dsmHeader);
	}
	
	public void enableDSMMode() {
		if(hamburgerDSMOption.getText().equals("OFF")) {
			clickDSMOptionFromHamburger();
		}
	}
}
