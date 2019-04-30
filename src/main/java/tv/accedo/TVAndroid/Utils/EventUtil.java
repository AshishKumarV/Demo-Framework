package tv.accedo.TVAndroid.Utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.iterators.EntrySetMapIterator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.testng.annotations.Test;

import tv.accedo.TVAndroid.Common.CommonFunctions;
import com.google.gson.Gson;


@Component
public class EventUtil 
{
	public static final Logger LOGGER =  Logger.getLogger(EventUtil.class);
	HashMap<String,Object> events = new HashMap<String,Object>();
	CommonFunctions commonFunctions;
	
	public EventUtil() {
		commonFunctions =new CommonFunctions();
	}
	
			
	public String getEventSongID(IOUtil iOUtil,String event,String before,String after) throws IOException, InterruptedException {
		String log=iOUtil.stopEventThreadToGetLogs();
		LOGGER.info(LOG.EVENT_LOGS+log);
		String songID = StringUtils.substringBetween(log,before, after);
		LOGGER.info("Song id is :"+ songID);
		return songID;
	}
	
	public String getEventLogs(IOUtil iOUtil,String event) throws IOException, InterruptedException {
		String log=iOUtil.stopEventThreadToGetLogs();
		LOGGER.info(LOG.EVENT_LOGS+log);
		return log;
	}
	
	
	
	
	public String compareEventStats(String sheetName, String eventType,String eventLogs) throws ParseException, IllegalArgumentException, IllegalAccessException, SecurityException, ClassNotFoundException {
		commonFunctions.readExcel(sheetName);
		LOGGER.info("Searching in excel for event : "+eventType);
		String eventExcel = commonFunctions.getEvent(eventType);
		LOGGER.info("event from excel is : "+ eventExcel);
		Gson gson = new Gson();
		String difference = "No difference";
		String differenceInEvent = "No difference";
		int row = commonFunctions.getRowNo(eventType);
		EventPOJO eventLogsObj = gson.fromJson(eventLogs,EventPOJO.class);
		
		if(eventLogsObj != null) {
			LOGGER.info("LOGS"+ eventLogsObj.toString());
			EventPOJO eventExcelObj =  gson.fromJson(eventExcel,EventPOJO.class);
			LOGGER.info("LOGS"+ eventExcelObj.toString());		
			
			List<String> list = commonFunctions.getEventMandatoryList(eventType);
			LOGGER.info("Mandatory list is :"+ list);
			
			difference =EventPOJO.compareEvents(eventExcelObj,eventLogsObj,list);
			LOGGER.info("flag value on comparing event :"+difference);
		}
		else {
			difference = "no event logs found";
			//differenceInEvent = differenceInEvent
		}
		
		try {
			commonFunctions.writeExcel(sheetName, difference, row);

		} catch (IOException e) {			
			LOGGER.info("This event not found " + eventType);

			if(difference.equals("No difference")) {			
				return difference;
				}
			else 
			{
				return difference;}
			
		} catch (Exception e) {

			e.printStackTrace();
			return difference;
		}
		return difference;
	}
	
	
	public String getADBLogs(IOUtil iOUtil,String event) throws InterruptedException{
		String log=iOUtil.stopEventThreadToGetLogs();
		return log;
	}
	
	
	@Test
	public void test1() throws IOException {
		EventUtil e= new EventUtil();
		//e.commonFunctions.writeExcel("12", 12);
		try {
			
			e.compareEventStats("Search","FOLLOW","{\"event_type\":\"FOLLOW\",\"timestamp\":1533792702199,\"lang\":\"en\",\"net\":\"2\\/-1\\/3\",\"meta\":{\"mode\":\"manual3\",\"type\":\"artist\",\"label\":\"artist\",\"screen_id\":\"ARTIST\",\"item_id\":\"sonu-nigam\"},\"did\":\"b2b122d584fead4f\\/Android\\/25\\/145\\/2.0.2.7\",\"uid\":\"u1Rfg-Z-jRE5weJHk0\"}");
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException | ClassNotFoundException
				| ParseException e1) {
			LOGGER.info("exception in calling event comparision method : "+ e1);
		}
		
	}
	


}
