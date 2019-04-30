package tv.accedo.TVAndroid.API;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tv.accedo.TVAndroid.Page.HomescreenPage;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class HomescreenAPI {
	private static final Logger LOGGER =  Logger.getLogger(HomescreenPage.class);

	int actualTotal;
	String railType;


	public List<String> getRecoArtistsTilte(String response,int index){
		List<String> artistTitle= new ArrayList<>();;
		try {
			JSONObject recoObj=new JSONObject(response);
			JSONArray itemsArr=recoObj.getJSONArray("items");
			LOGGER.info("Length Of JSON Array is::"+itemsArr.length());
			JSONObject latestHindiObj=itemsArr.getJSONObject(index);
			JSONArray latestHindiArr=latestHindiObj.getJSONArray("items");
			LOGGER.info("Length Of Items JSON Array is::"+latestHindiArr.length());
			for(int i=0;i<latestHindiArr.length();i++) {
				JSONObject latestHindiSongsTitleObj=latestHindiArr.getJSONObject(i);
				artistTitle.add(latestHindiSongsTitleObj.getString("title").toString().trim());
			}
		}catch(JSONException e ) {

		}
		return artistTitle;

	}

	public String getResponseString(Response res, String path) {
		JsonPath jsonPathEvaluator = res.jsonPath();
		return jsonPathEvaluator.getString(path);
	}

	public Integer getResponseInt(Response response, String item) {
		JsonPath jsonPathEvaluator = response.jsonPath();
		return jsonPathEvaluator.getInt(item);
	}

	public List<String> getResponseList(Response response, String key) {
		JsonPath jsonPathEvaluator = response.jsonPath(); 
		return jsonPathEvaluator.getList(key);
	}
	public int getResponseValue(Response response, String key) {
		JsonPath jsonPathEvaluator = response.jsonPath();
		return jsonPathEvaluator.getInt(key);
	}

	public Set<String> getRecoArtistOnScreen(List<String>reco, List<String>popular){
		Set<String>recoTitles=new HashSet<>();
		recoTitles.addAll(reco);
		recoTitles.addAll(popular);
		return recoTitles;
	}

	public Set<String>getArtistOnMoreScr(List<String>popular,List<String>reco,List<String> followed){
		Set<String>titles=new HashSet<>();
		for(String pop : popular) {
			pop=pop.trim();
			titles.add(pop);
		}
		for(String pop : reco) {
			pop= pop.trim();
			titles.add(pop);
		}
		for(String artist: followed) {
			titles.remove(artist.trim());
		}
		return titles;

	}

	public List<String>getSinglesRailTitle(String response){
		List<String> singlesTitle= new ArrayList<>();;
		try {
			JSONObject responseObj=new JSONObject(response);
			JSONArray itemsArr=responseObj.getJSONArray("items");
			for(int i=0;i<itemsArr.length();i++) {
				JSONObject railTitleObj=itemsArr.getJSONObject(i);
				if(railTitleObj.getString("railType").toString().equals("SINGLES")) {
					singlesTitle.add(railTitleObj.getString("title").toString());
					System.out.println("Title is::>> "+railTitleObj.getString("title").toString());
				}
			}

		}catch(JSONException e ) {

		}
		return singlesTitle;
	}

	public List<String> getArtistRailTitle(String response){
		List<String> artistsTitle= new ArrayList<>();;
		try {
			JSONObject responseObj=new JSONObject(response);
			JSONArray itemsArr=responseObj.getJSONArray("items");
			for(int i=0;i<itemsArr.length();i++) {
				JSONObject railTitleObj=itemsArr.getJSONObject(i);
				if(railTitleObj.getString("railType").toString().equals("ARTIST")) {
					artistsTitle.add(railTitleObj.getString("title").toString());
					System.out.println("Title is::>> "+railTitleObj.getString("title").toString());
				}
			}

		}catch(JSONException e ) {

		}
		return artistsTitle;
	}

	public List<String> getAllRailsInARailType(String response,String railType){
		List<String> artistsTitle= new ArrayList<>();;
		try {
			JSONObject responseObj=new JSONObject(response);
			JSONArray itemsArr=responseObj.getJSONArray("items");
			for(int i=0;i<itemsArr.length();i++) {
				JSONObject railTitleObj=itemsArr.getJSONObject(i);
				if(railTitleObj.getString("railType").toString().equals(railType)) {
					artistsTitle.add(railTitleObj.getString("title").toString());
					System.out.println("Title is::>> "+railTitleObj.getString("title").toString());
				}
			}

		}catch(JSONException e ) {

		}
		return artistsTitle;
	}
	public String getGenres(Response response) {
		List<String>genres=getResponseList(response, "genre_mapping.genre");
		String title="";
		for (int i=0;i<genres.size();i++) {
			title=title+genres.get(i)+"%2C";
		}
		title=StringUtils.substringBeforeLast(title, "%2C");
		title=title.replace("$", "%24");
		return title;
	}
}
