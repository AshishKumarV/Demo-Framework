package tv.accedo.TVAndroid.Test;

import org.apache.log4j.Logger;

import io.appium.java_client.android.AndroidDriver;
import tv.accedo.TVAndroid.API.APICommons;
import tv.accedo.TVAndroid.API.HomescreenAPI;
import tv.accedo.TVAndroid.API.RadioTabAPI;
import tv.accedo.TVAndroid.Common.DeviceHelper;
import tv.accedo.TVAndroid.Page.CommonPages;
import tv.accedo.TVAndroid.Page.DownloadPage;
import tv.accedo.TVAndroid.Page.HomescreenPage;
import tv.accedo.TVAndroid.Page.MiniPlayer;
import tv.accedo.TVAndroid.Page.MyProfilePage;
import tv.accedo.TVAndroid.Page.Notifications;
import tv.accedo.TVAndroid.Page.SplashScreenPage;
import tv.accedo.TVAndroid.Page.StartPlayerPage;


public class PageInit {

	public CommonPages commonPages;
	public MiniPlayer miniPlayer;
	public HomescreenPage homePage;
	public MyProfilePage myProfilePage;
	public DownloadPage downloadPage;
	public Notifications notification;
	public SplashScreenPage splashScreenPage;
	public HomescreenPage homescreen;
	public APICommons apiCommons;
	public HomescreenAPI homescreenAPI;
	public RadioTabAPI radioTabAPI;
	public DeviceHelper deviceHelper;
	public Logger logger;
	public StartPlayerPage startPlayerPage;
	public  AndroidDriver androidDriver;
	
	//TODO autowiring 
	public  PageInit(AndroidDriver androidDriver)
	{
		this.logger=logger;
		commonPages = new CommonPages(androidDriver);
		miniPlayer = new MiniPlayer(androidDriver);
		homePage = new HomescreenPage(androidDriver);
		myProfilePage = new MyProfilePage(androidDriver);
		downloadPage = new DownloadPage(androidDriver);
		notification = new Notifications(androidDriver);
		splashScreenPage = new SplashScreenPage(androidDriver);
		homescreen = new HomescreenPage(androidDriver);
		deviceHelper = new DeviceHelper();
		apiCommons = new APICommons();
		homescreenAPI = new HomescreenAPI();
		radioTabAPI = new RadioTabAPI();
		startPlayerPage = new StartPlayerPage();

	}

}
