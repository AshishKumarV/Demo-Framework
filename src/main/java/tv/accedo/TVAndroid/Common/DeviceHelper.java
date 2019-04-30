package tv.accedo.TVAndroid.Common;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import tv.accedo.TVAndroid.Utils.ADBCommands;
import tv.accedo.TVAndroid.Utils.BaseUtil;
import tv.accedo.TVAndroid.Utils.DriverFactory;
import tv.accedo.TVAndroid.Utils.HubNodeConfig;

@Component
public class DeviceHelper extends BaseUtil {

	private static final Logger LOGGER = Logger.getLogger(DeviceHelper.class);
	public static LinkedHashSet<HashMap<String, HashMap<String, String>>> deviceInfoList = new LinkedHashSet<HashMap<String, HashMap<String, String>>>();;
	public static LinkedHashSet<String> devices = new LinkedHashSet<String>();
	private static DeviceHelper deviceHelper = null;
	// hubHost = hubNodeConfig.getIpAddress();

	@Value("${hubPort}")
	private String hubPort;

	@Value("${appPackage}")
	private String appPackage;

	@Value("${deviceName}")
	private String model;

	@Value("${platformVersion}")
	private String version;

	@Value("${hubHost}")
	private String hubHost;

	@Autowired
	HubNodeConfig hubNodeConfig;

	@Autowired
	CommonFunctions commonFunctions;
	
	@Autowired
    DriverFactory driverFactory;

	private static Thread thread = new Thread(new Runnable() {
		@Override
		public void run() {

		}
	});

	public void initDevices() {
		getConnectedDevices();
		if(devices.size()>1) {
			try {
				hubNodeConfig.startGrid();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			getConnectedDevices();
			deviceInfo();
			makeJsonForDevice();
		}
		else {
			try {
				driverFactory.startAppiumServer();
			} catch (IOException |InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}

	public void deviceInfo() {
		for (String device : devices) {
			LinkedHashSet<String> resultSet = new LinkedHashSet<>();
			HashMap<String, HashMap<String, String>> deviceInfoMap = new HashMap<String, HashMap<String, String>>();
			resultSet = executeCommand("-s " + device + " shell " + ADBCommands.DEVICE_INFO.toString());
			HashMap<String, String> devicePropMap = new HashMap<String, String>();

			for (String deviceInfo : resultSet) {
				devicePropMap.put(StringUtils.substringBetween(deviceInfo, "[", "]:"),
						StringUtils.substringBetween(deviceInfo, ": [", "]"));
				deviceInfoMap.put(device, devicePropMap);
			}
			deviceInfoList.add(deviceInfoMap);
		}
		LOGGER.info("device info : " + deviceInfoList);
	}

	public void getConnectedDevices() {
		LinkedHashSet<String> resultSet = new LinkedHashSet<>();
		resultSet = executeCommand("devices");

		for (String device : resultSet) {
			device.replaceAll("\\n", "");
			if (!device.contains("List of devices attached") && device != null && !device.isEmpty()) {
				device = device.replace("	device", "");
				devices.add(device);
			}
		}
	}

	public LinkedHashSet<String> executeCommand(String command) {
		LinkedHashSet<String> resultSet = new LinkedHashSet<>();
		String commandToBeExecuted = "/platform-tools/adb " + command;

		try {
			System.out.println("command to be executed " + commandToBeExecuted);
			Process process = Runtime.getRuntime().exec(ADBCommands.ANDROID_HOME.toString() + commandToBeExecuted);
			System.out.println("command to be executed2 " + ADBCommands.ANDROID_HOME.toString() + commandToBeExecuted);
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String s;
			while ((s = reader.readLine()) != null) {
				System.out.println(s);
				resultSet.add(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resultSet;
	}

	public void makeJsonForDevice() {
		String udid = null;
		String deviceName = null;
		String platformVersion = null;
		String nodePort = null;
		String nodeUrl = null;
		String nodeIP = hubHost;

		for (HashMap<String, HashMap<String, String>> entry1 : deviceInfoList) {
			for (Map.Entry<String, HashMap<String, String>> entry2 : entry1.entrySet()) {
				udid = entry2.getKey();
				try {
					nodePort = String.valueOf(hubNodeConfig.getFreePort());
					nodeUrl = "http://" + hubHost + ":" + nodePort + "/wd/hub";
				} catch (IOException e1) {
					System.out.println("Exception while getting free port : " + e1);
				}
				deviceName = entry2.getValue().get(model);
				platformVersion = entry2.getValue().get(version);
			}

			String deviceFile = null;
			try {
				deviceFile = makeJson(udid, deviceName, platformVersion, nodePort, nodeUrl);
			} catch (IOException e) {
				System.out.println("error in creating json for device " + udid + " " + e);
			}

			hubNodeConfig.connectNode(deviceFile, nodeIP, nodePort, udid);
		}
	}

	public String makeJson(String udid, String deviceName, String platformVersion, String nodePort, String nodeUrl)
			throws IOException {
		JsonObject json = new JsonObject();
		JsonObject json2 = new JsonObject();
		JsonArray json3 = new JsonArray();

		// String url ="http://"+nodeIP+":"+nodePort+"/wd/hub";
		// String hub ="http://"+hubIP+":"+hubPort+"/grid/register/";

		json.addProperty("cleanUpCycle", 4000);
		json.addProperty("timeout", 30000);
		json.addProperty("proxy", "org.openqa.grid.selenium.proxy.DefaultRemoteProxy");
		json.addProperty("url", nodeUrl);
		json.addProperty("host", hubHost);
		json.addProperty("port", nodePort);
		json.addProperty("maxSession", 1);
		json.addProperty("register", true);
		json.addProperty("registerCycle", 6000);
		json.addProperty("hubPort", hubPort);
		json.addProperty("hubHost", hubHost);
		JsonObject json1 = makeJsonObjectOfKeyAndJsonObject("configuration", json);

		json2.addProperty("udid", udid);
		json2.addProperty("deviceName", deviceName);
		json2.addProperty("platformVersion", platformVersion);
		json2.addProperty("maxInstances", 1);
		json2.addProperty("platform", "android");
		json2.addProperty("appPackage", appPackage);
		json2.addProperty("appActivity", "tv.accedo.airtel.wynk.presentation.view.activity.SplashActivity");
		json2.addProperty("noReset", "true");
		// json2.addProperty("automationName", "uiautomator2");
		json3.add(json2);

		JsonObject json4 = makeJsonObjectOfKeyAndJsonArray("capabilities", json3);
		String finalJson = mergeJsonObjects(json4, json1);
		System.out.println("final json for " + udid + " is : " + finalJson);
		String path = System.getProperty("user.dir") + "/src/main/resources/" + deviceName.replace(" ", "_") + ".json";
		commonFunctions.writeFile(path, finalJson);
		return path;
	}

	public String mergeJsonObjects(JsonObject json1, JsonObject json2) {
		String capabilities = new Gson().toJson(json1);
		String config = new Gson().toJson(json2);

		capabilities = new StringBuilder(capabilities).deleteCharAt(0).toString();
		capabilities = new StringBuilder(capabilities).deleteCharAt(capabilities.toCharArray().length - 1).toString();

		config = new StringBuilder(config).deleteCharAt(0).toString();
		config = new StringBuilder(config).deleteCharAt(config.toCharArray().length - 1).toString();

		return "{" + capabilities + "," + config + "}";

	}

	public JsonObject makeJsonObjectOfKeyAndJsonArray(String key, JsonArray arr) {
		JsonObject obj = new JsonObject();
		obj.add(key, arr);
		return obj;
	}

	public JsonObject makeJsonObjectOfKeyAndJsonObject(String key, JsonObject object) {
		JsonObject obj = new JsonObject();
		obj.add(key, object);
		return obj;
	}

	public void deleteFile() {
		for (HashMap<String, HashMap<String, String>> entry1 : deviceInfoList) {
			for (Map.Entry<String, HashMap<String, String>> entry2 : entry1.entrySet()) {
				String deviceName = entry2.getValue().get(model).replace(" ", "_") + ".json";
				File file = new File(System.getProperty("user.dir") + "/src/main/resources/" + deviceName);
				file.delete();
			}
		}
	}

	public void setAirplaneSetting(String code) throws IOException, InterruptedException {
		String ADB = System.getProperty("user.home") + "/Library/Android/sdk/platform-tools/";
		String command = "adb shell am start -a android.settings.AIRPLANE_MODE_SETTINGS "
				+ "& adb shell input keyevent KEYCODE_ENTER";
		String commandToBeExecuted = command;
		LOGGER.info(ADB + commandToBeExecuted);
		Process process = Runtime.getRuntime().exec(ADB + commandToBeExecuted);
		process.waitFor();

	}

	public void setAirplane() throws IOException, InterruptedException {
		String ADB = System.getProperty("user.home") + "/Library/Android/sdk/platform-tools/";
		String command = "adb shell input keyevent KEYCODE_ENTER";
		String commandToBeExecuted = command;
		LOGGER.info(ADB + commandToBeExecuted);
		Process process = Runtime.getRuntime().exec(ADB + commandToBeExecuted);
		process.waitFor();
	}

	/*
	 * Autor shubham verma
	 */

	public void enableAirplaneMode() throws IOException, InterruptedException {
		// executeADBCommand("adb shell settings put global airplane_mode_on 1 & adb
		// shell am broadcast -a android.intent.action.AIRPLANE_MODE");
		executeADBCommand("shell settings put global airplane_mode_on 1");
		executeADBCommand("shell am broadcast -a android.intent.action.AIRPLANE_MODE");

	}

	/*
	 * Also  BaseUtil Autor shubham verma
	 */

	public void disableAirplaneMode() throws IOException, InterruptedException {

		executeADBCommand("shell settings put global airplane_mode_on 0");
		executeADBCommand("shell am broadcast -a android.intent.action.AIRPLANE_MODE");

	}

}
