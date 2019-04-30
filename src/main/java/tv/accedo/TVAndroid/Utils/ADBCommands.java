package tv.accedo.TVAndroid.Utils;



	public enum ADBCommands 

	{	
			AIRPLANE_MODE_ON("settings put global airplane_mode_on 1"),
			AIRPLANE_MODE_OFF("settings put global airplane_mode_on 0"),

		    AIRPLANE_MODE_BROADCAST("am broadcast -a android.intent.action.AIRPLANE_MODE"),
		    WYNK_LOGS("/platform-tools/adb shell logcat | grep -i wynk "),

		    WYNK_LOGS_LOGCAT("shell  logcat -v threadtime -f /sdcard/logcat.txt"),
		    REBOOT("reboot"),
		    //TODO wifi network
		    NETWORK("shell settings put global preferred_network_mode "),
		    DEVICE_INFO("getprop | grep -e ro.serialno -e ro.product.model -e ro.build.version.release"),
		    ANDROID_HOME(System.getProperty("user.home")+"/Library/Android/sdk"),
		    SEARCH_EVENT("ITEM_SEARCH_CONSUMED"),
		    CLICK_EVENT("CLICK"),
		    SONG_PLAYED_EVENT("SONG_PLAYED"),
		    FOLLOW("FOLLOW"),
		    UNFOLLOW("UNFOLLOW"),
		    OPEN_Wynk_WITH_URL("am start -a android.intent.action.VIEW -d"),
		    OPEN_Chrome_WITH_URL("am start -n com.android.chrome/com.google.android.apps.chrome.Main -a android.intent.action.VIEW -d"),
	        TEXT_PASTE("input keyevent 279"),
	        MOBILE_DATA_ENABLE("svc data enable"),
	        MOBILE_DATA_DISABLE("svc data disable"),
	        OPEN_WIFI_SETTING("am start -a android.intent.action.MAIN -n com.android.settings/.wifi.WifiSettings"),
	        ENABLE_WIFI("am broadcast -a io.appium.settings.wifi --es setstatus enable"),
	        DISABLE_WIFI("am broadcast -a io.appium.settings.wifi --es setstatus disable"),
	        CURSOR_MOVE_TO_RIGHT_END("input keyevent KEYCODE_MOVE_END"),
	        START_ESEXPLORER("shell am start -n com.estrongs.android.pop/.view.FileExplorerActivity"),
		    STOP_ESEXPLORER(" am force-stop com.estrongs.android.pop");
		
			  private final String command;

			  private ADBCommands(String command) {
			    this.command = command;
			  }
			

			  public String getCommand() {
			     return command;
			  }

		

			  @Override
			  public String toString() {
			    return command;
			  }
		

	}
