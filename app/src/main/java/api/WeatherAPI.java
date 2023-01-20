package api;


import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherAPI {

	private static final String API_KEY = "9U986SS9RNEQH5DM82ZBBJCSS";
	private static final String API_ENDPOIT = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";
	private String location;
	private String startDate;
	private String endDate;
	private String unitGroup;
	
	
	public WeatherAPI(String location, String unitGroup, String startDate, String endDate) {
		this.setLocation(location);
		this.setUnitGroup(unitGroup);
		this.setStartDate(startDate);
		this.setEndDate(endDate);
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getUnitGroup() {
		return unitGroup;
	}

	public void setUnitGroup(String unitGroup) {
		this.unitGroup = unitGroup;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public JSONObject request() throws Exception {
		String methodString = "GET";
		
		//build URL pieces
		StringBuilder requestBuilder = new StringBuilder(API_ENDPOIT);
		requestBuilder.append(URLEncoder.encode(this.getLocation(), StandardCharsets.UTF_8.toString()));
		if(getStartDate() != null && !getStartDate().isEmpty()) {
			requestBuilder.append("/").append(getStartDate());
			if(getEndDate() != null && !getEndDate().isEmpty()) {
				requestBuilder.append("/").append(getEndDate());
			}
		}
		
		//Build Parameters to send via GET or POST
		URIBuilder builder = new URIBuilder(requestBuilder.toString());
		builder.setParameter("unitGroup", getUnitGroup()).setParameter("key", API_KEY);
		
		if("GET".equals(methodString)) {
			requestBuilder.append("?").append(builder);
		}
		
		HttpGet get = new HttpGet(builder.build());
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		CloseableHttpResponse response = httpClient.execute(get);
		
		String rawResultString = null;
		
		try {
			if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				System.out.printf("Bad response status code:%d%n", response.getStatusLine().getStatusCode());
				return null;
			}
			
			HttpEntity entity = response.getEntity();
			if(entity != null) {
				rawResultString = EntityUtils.toString(entity, Charset.forName("utf-8"));
			}
		} finally {
			response.close();
		}
		
		return parseTimeLineJson(rawResultString);
		
	}
	
	public static JSONObject parseTimeLineJson(String rawResult) {
		JSONObject response = new JSONObject(rawResult);
		
		
		JSONObject jsonObject = new JSONObject();
		
		ZoneId zoneId = ZoneId.of(response.getString("timezone"));
		String addressString = response.getString("resolvedAddress");
		String descString = response.getString("description");
		
		jsonObject.put("resolvedAddress", addressString);
		jsonObject.put("description", descString);
		
		JSONArray values = response.getJSONArray("days");
		
		JSONArray array = new JSONArray();
		for(int i = 0; i < values.length(); i++) {
			JSONObject day = new JSONObject();
			JSONObject dayValueJsonObject = values.getJSONObject(i);
			ZonedDateTime dateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(dayValueJsonObject.getLong("datetimeEpoch")), zoneId);
			ZonedDateTime sunrise = ZonedDateTime.ofInstant(Instant.ofEpochSecond(dayValueJsonObject.getLong("sunriseEpoch")), zoneId);
			ZonedDateTime sunset = ZonedDateTime.ofInstant(Instant.ofEpochSecond(dayValueJsonObject.getLong("sunsetEpoch")), zoneId);
			

			double temp = dayValueJsonObject.getDouble("temp");
			double min = dayValueJsonObject.getDouble("tempmin");
			double max = dayValueJsonObject.getDouble("tempmax");
			int humidity = dayValueJsonObject.getInt("humidity");
			int uvind = dayValueJsonObject.getInt("uvindex");
			String descriptionString = dayValueJsonObject.getString("description");
			
			day.put("datetimeEpoch", dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE));
			day.put("sunriseEpoch", sunrise.format(DateTimeFormatter.ISO_LOCAL_TIME));
			day.put("sunsetEpoch", sunset.format(DateTimeFormatter.ISO_LOCAL_TIME));
			day.put("temp", temp);
			day.put("tempmin", min);
			day.put("tempmax", max);
			day.put("humidity", humidity);
			day.put("uvindex", uvind);
			day.put("description", descriptionString);
			
			array.put(day);
		}
		
		jsonObject.put("days", array);
		
		return jsonObject;
	}
	
	
	
	
}

