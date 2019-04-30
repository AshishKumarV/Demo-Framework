package tv.accedo.TVAndroid.API;


import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class APICommons {

	private static final Logger LOGGER = Logger.getLogger(APICommons.class);
	private final static String salt = "DGE$5SGr@3VsHYUMas2323E4d57vfBfFSTRU@!DSH(*%FDSdfg13sgfsg";

	public Response getResponse(String baseUrl, String endPoint, String authenticationKey) {
		RestAssured.baseURI = baseUrl;
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.request(Method.GET, endPoint + authenticationKey);

		return response;
	}

	public String getResponseBody(Response response) {
		return response.getBody().asString();
	}

	public int getStatusCode(Response response) {
		return response.getStatusCode();
	}

	public Map<String, String> getHeaders(Response response) {
		Map<String, String> responseHeader = new HashMap<>();
		Headers allHeaders = response.headers();
		for (Header header : allHeaders) {
			responseHeader.put(header.getName(), header.getValue());
		}
		return responseHeader;
	}
	
	public List<String> getProfilePackagesOrder(String responseBody){
		List<String>packagesOrder = new ArrayList<>();
		try {
			JSONObject responseObj=new JSONObject(responseBody);
			JSONObject profileObj=responseObj.getJSONObject("profile");
			JSONArray packagesOrderArr=profileObj.getJSONArray("packagesOrder");
			for(int i=0;i<packagesOrderArr.length();i++) {
				packagesOrder.add(packagesOrderArr.getString(i));
			}
			}catch(JSONException e ) {
				
			}
		return packagesOrder;
	}
	
	public List<String> getSongTiltesInRail(String response, int index){
		List<String> itemTitle= new ArrayList<>();;
		try {
		JSONObject itemsObj=new JSONObject(response);
		JSONArray itemsArr=itemsObj.getJSONArray("items");
		LOGGER.info("Length Of JSON Array is::"+itemsArr.length());
		JSONObject railObj=itemsArr.getJSONObject(index);
		JSONArray railArr=railObj.getJSONArray("items");
		LOGGER.info("Length Of Items JSON Array is::"+railArr.length());
		for(int i=0;i<railArr.length();i++) {
			JSONObject songsTitleObj=railArr.getJSONObject(i);
			itemTitle.add(songsTitleObj.getString("title").toString().trim());
		}
		}catch(JSONException e ) {
			
		}
		return itemTitle;
		
	}
	
	public List<String> getPageInItData(String baseUrl,String endPoint,String auth,int noOfCalls,String path){
		int offset=0;
		List<String>data=new ArrayList<>();
		for(int i=0;i<noOfCalls;i++) {
			JsonPath jsonPathEvaluator = getResponse(baseUrl, endPoint+Integer.toString(offset), auth).jsonPath();
			data.addAll(jsonPathEvaluator.getList(path));
			offset+=50;
		}
		return data;
	}

	public String getSelectedLangFromConfig(String response) {
		List<String> langs = new ArrayList<>();
		String selectedLang = "";
		try {
			JSONObject responseObj = new JSONObject(response);
			JSONObject profileObj = responseObj.getJSONObject("profile");
			JSONArray selectedLangs = profileObj.getJSONArray("selectedContentLangs");
			for (int i = 0; i < selectedLangs.length(); i++) {
				langs.add(selectedLangs.getString(i));
				System.out.println("Lang at i is:>> "+selectedLangs.getString(i));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		selectedLang = langs.toString().replace("[", "").replace("]", "").replace(" ", "");
		return selectedLang;
//		return langs;
	}

	public Response getResponse(String baseUrl, String targetUrl, String parameters, HashMap<String, String> headers) {
		RequestSpecification httpRequest = RestAssured.given();
		String trimmed = baseUrl.replace(baseUrl, "");
		headers.put("auth", md5Hash(trimmed));
		String url = baseUrl + targetUrl + parameters;
		Response response = httpRequest.headers(headers).when().get(url);
		return response;
	}

	private static String generateSignaturePost(String httpVerb, String requestUri, String payload,
			long requestTimestamp, String secret) {
		String signature = "";
		String digestString = new StringBuilder(httpVerb).append(requestUri).append(payload).append(requestTimestamp)
				.toString();
		try {
			signature = calculateRFC2104HMAC(digestString, secret);
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		return signature;
	}

	private static String generateSignatureGet(String httpVerb, String requestUri, String secret) {
		String signature = "";
		String digestString = new StringBuilder(httpVerb).append(requestUri).toString();
		try {
			signature = calculateRFC2104HMAC(digestString, secret);
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		return signature;
	}

	public String getUtkn(String method, String urlString, String payload, String token, String uid)
			throws MalformedURLException {
		LOGGER.info("uid" + uid);
		LOGGER.info("token" + token);
		long requestTimestamp = System.currentTimeMillis();
		URL url = new URL(urlString);
		String requestUri = url.getPath();

		if (null != url.getQuery() && !url.getQuery().isEmpty()) {
			requestUri = requestUri + '?' + url.getQuery();
		}

		String signature = "";

		if (method.trim().equalsIgnoreCase("GET")) {
			signature = generateSignatureGet(method, requestUri, token);
		} else if (method.trim().equalsIgnoreCase("POST")) {
			signature = generateSignaturePost(method, requestUri, payload, requestTimestamp, token);
		}

		return uid.trim() + ":" + signature;
	}

	private static String calculateRFC2104HMAC(String data, String secretKey) throws java.security.SignatureException {
		String result;
		try {
			Mac mac = Mac.getInstance("HmacSHA1");
			SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA1");
			mac.init(key);
			byte[] authentication = mac.doFinal(data.getBytes());
			byte[] encodeBase64 = org.apache.commons.codec.binary.Base64.encodeBase64(authentication);
			result = new String(encodeBase64);
		} catch (Exception e) {
			throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
		}
		return result;

	}

	public static String md5Hash(String message) {
		String md5 = null;
		if (null == message)
			return null;

		message = message + salt;// adding a salt to the string before it gets
		// hashed.
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");// Create
			// MessageDigest
			// object
			// for MD5
			digest.update(message.getBytes(), 0, message.length());// Update
			// input
			// string in
			// message
			// digest
			md5 = new BigInteger(1, digest.digest()).toString(16);// Converts
			// message
			// digest
			// value in
			// base 16
			// (hex)

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md5;
	}
	
	public Response getResponseUsingHeader(String method, String baseUrl,String targetUrl, String payload, String token, String uid) {
		String utkn = "";
		HashMap<String, String> graphHeader;
		String  compUrl=baseUrl+targetUrl;
		System.out.println("Complete URL iS::>"+compUrl);
		try {
		utkn =	getUtkn(method, compUrl, "", token, uid);
		System.out.println("UTOKEN IS::>"+utkn.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		graphHeader = new HashMap<>();
		graphHeader.put("x-bsy-utkn", utkn);	
		Response resp=getResponse(baseUrl,targetUrl , "",graphHeader);
		return  resp;
	}
	
	

}
