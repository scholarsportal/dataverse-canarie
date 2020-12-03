package info.scholarsportal.util;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PlatformUtil {
	
	public static String getReleaseTime() {		
		String releaseDate = "";
		final String uri = "https://api.github.com/repos/scholarsportal/dataverse/releases/latest";	     
	    RestTemplate restTemplate = new RestTemplate();
	    ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
	    String body = response.getBody();
	    ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode root = mapper.readTree(body);
			JsonNode node = root.path("created_at");
			releaseDate = node.asText();
			System.out.println("Release: "+releaseDate);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return releaseDate;
	}
	
	public static String getLastReset() {
		DateTime dt = DateTime.now().withDayOfMonth(1).withZone(DateTimeZone.UTC);
		return convertToISO8601Format(dt);
	}
	
	public static String getVersion() {
		String apiVersion = "";
		final String uri = "https://dataverse.scholarsportal.info/api/info/version";	     
	    RestTemplate restTemplate = new RestTemplate();
	    ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
	    String body = response.getBody();
	    ObjectMapper mapper = new ObjectMapper();
	    JsonNode root;
		try {
			root = mapper.readTree(body);
			JsonNode data = root.path("data");
			apiVersion = data.get("version").asText();
			System.out.println("Version: "+apiVersion);
			return apiVersion;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return apiVersion;
	}
	
	private static String convertToISO8601Format(DateTime dateTime) {
		DateTimeFormatter formatter = new DateTimeFormatterBuilder().append(ISODateTimeFormat.dateTimeNoMillis()).toFormatter().withOffsetParsed();
		return formatter.print(dateTime);
	}
}
