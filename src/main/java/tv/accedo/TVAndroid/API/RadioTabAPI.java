package tv.accedo.TVAndroid.API;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RadioTabAPI {

	public int getItemsCount(String response) {
		int length = 0;
		try {
			JSONObject responseObj = new JSONObject(response);
//			String total = responseObj.getString("total");
			JSONArray itemsArray = responseObj.getJSONArray("items");
			length = itemsArray.length();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return length;
	}

	public List<String> getRailTitles(String response) {
		List<String> titles = new ArrayList<>();
		try {
			JSONObject responseObj = new JSONObject(response);
			JSONArray itemsArray = responseObj.getJSONArray("items");
			for (int i = 0; i < itemsArray.length(); i++) {
				JSONObject itemsObj = itemsArray.getJSONObject(i);
				titles.add(itemsObj.getString("title"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return titles;
	}

	public List<String> getRailItemsTitles(String response, String railTitle) {
		List<String> titles = new ArrayList<>();
		List<String> railTitles = new ArrayList<>();
		try {
			JSONObject responseObj = new JSONObject(response);
			JSONArray itemsArray = responseObj.getJSONArray("items");

			for (int i = 0; i < itemsArray.length(); i++) {
				JSONObject itemsObj = itemsArray.getJSONObject(i);
				titles.add(itemsObj.getString("title"));
			}
			int index = titles.indexOf(railTitle);
			JSONObject itemsOfItems = itemsArray.getJSONObject(index);
			JSONArray itemsOfItemArr = itemsOfItems.getJSONArray("items");
			for (int j = 0; j < itemsOfItemArr.length(); j++) {
				JSONObject rt = itemsOfItemArr.getJSONObject(j);
				railTitles.add(rt.getString("title"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return railTitles;
	}

	public List<String> getStationNamesItemsOfItems(String response, String railTitle, String stationName) {
		List<String> titles = new ArrayList<>();
		List<String> railTitles = new ArrayList<>();
		List<String> stationNames = new ArrayList<>();

		try {

			JSONObject responseObj = new JSONObject(response);
			JSONArray itemsArray = responseObj.getJSONArray("items");

			for (int i = 0; i < itemsArray.length(); i++) {
				JSONObject itemsObj = itemsArray.getJSONObject(i);
				titles.add(itemsObj.getString("title"));
			}

			int index = titles.indexOf(railTitle);
			JSONObject itemsOfItems = itemsArray.getJSONObject(index);
			JSONArray itemsOfItemArr = itemsOfItems.getJSONArray("items");

			for (int j = 0; j < itemsOfItemArr.length(); j++) {
				JSONObject rt = itemsOfItemArr.getJSONObject(j);
				railTitles.add(rt.getString("title"));
			}

			int number = railTitles.indexOf(stationName);
			JSONObject itemsOfStation = itemsOfItemArr.getJSONObject(number);
			JSONArray stationsArr = itemsOfStation.getJSONArray("items");

			for (int k = 0; k < stationsArr.length(); k++) {
				JSONObject sn = stationsArr.getJSONObject(k);
				stationNames.add(sn.getString("title"));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stationNames;
	}
}
