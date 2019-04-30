package tv.accedo.TVAndroid.Stats;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tv.accedo.TVAndroid.Utils.EventUtil;
import tv.accedo.TVAndroid.Utils.IOUtil;


@Component
public class CommonStatsFunctions {

	private static final Logger LOGGER =  Logger.getLogger(CommonStatsFunctions.class);

	@Autowired
	IOUtil iOUtil ;

	@Autowired
	EventUtil eventUtil ;

	String eventLogData;
	LinkedHashSet<String> eventLogsSet ;
	
	public String difference = null;
	public String diffEvent = null;

	public boolean verifyStats(String sheetName, LinkedHashSet<String> eventTypes) throws InterruptedException {
		Thread.sleep(10000);
		eventLogsSet= iOUtil.getEventSet();
		iOUtil.stopEventThread();
		eventLogData= null;
		int count =0 ;
		
		diffEvent= "Difference:";
		for(String eventType :eventTypes)
		{
			difference = null;
			LOGGER.info("Starting evaluation for Event "+ eventType);
			//			try {
			eventLogData= getEventFromLogs(eventType, eventLogsSet);
			//			} catch (NullPointerException e) {
			//				eventLogData= "{\"event_type\":\"no logs\"}";
			//			}
			LOGGER.info("Starting evaluation for Event "+ eventType +" logs : "+ eventLogData);

		try {
				difference =eventUtil.compareEventStats(sheetName, eventType, eventLogData);
				if(!difference.equalsIgnoreCase("No difference")) {
					diffEvent=diffEvent+" "+eventType + " "+difference + "\n";
					count ++;
				}
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException | ClassNotFoundException
				| ParseException e) {
			e.printStackTrace();
		}

		}
		if(count == 0) { 
			return true;
		} else {
			return false;
		}
//		return difference;
	}


	public String getEventFromLogs(String eventType,LinkedHashSet<String> eventLogs) {
		String eventLogData = null;

		if(eventType.contains(",")) {
			String[] events = StringUtils.split(eventType,",");

			for(String eventLog :eventLogs) {
				int count = 0;

				for(int i = 0;i<events.length;i++) {
					if(!eventLog.contains(events[i])) {
						count++;
						break;
					}
				}
				if(count ==0) {
					eventLogData= eventLog;
					break;
				}
			}
		}

		else {
			for(String eventLog :eventLogs) {
				if(eventLog.contains(eventType+"\"")) {
					eventLogData= eventLog;
					break;
				}
			}
		}

		return eventLogData;
	}


	public int verifyCount(String event) {
		//LinkedHashSet<String> eventLogsSet = iOUtil.getEventSet();
		String[] events = null ;
		int occurence = 0;

		if(event.contains(",")) {
			events= StringUtils.split(event,",");
		}

		else {
			events = new String[1];
			events[0]= event;
		}

			
		HashSet<String> eventsToCount = new HashSet<String>();

		for(String eventLog :eventLogsSet) {
			int count = 0;
			
			for(int i = 0;i<events.length;i++) {
				if(!eventLog.contains(events[i])) {
					++count;
					break;
				}
			}
			if(count ==0) {
				LOGGER.info("event to be added :"+eventLog);
				eventsToCount.add(eventLog);
			}
			occurence = eventsToCount.size();
			
		}
		return occurence;
	}


	public int verifyCount_v2(String event) {
		//LinkedHashSet<String> eventLogsSet = iOUtil.getEventSet();
		String[] events = null ;
		int occurence = 0;

		if(event.contains(",")) {
			events= StringUtils.split(event,",");
		}

		else {
			events = new String[1];
			events[0]= event;
		}

		HashSet<String> eventsToCount = new HashSet<String>();
		for(String eventLog :eventLogsSet) {
			int count = 0;

			for(int i = 0;i<events.length;i++) {
				if(!eventLog.contains(events[i])) {
					count++;
					break;
				}
			}
			if(count ==0) {
				eventsToCount.add(eventLog);
			}
			occurence = eventsToCount.size();

		}
		return occurence;
	}




}
