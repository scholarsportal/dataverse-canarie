package info.scholarsportal.util;

import java.io.IOException;

import org.joda.time.DateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PlatformUtil {
	
	public static String getReleaseTime() {
		DateTime dt = new DateTime("2020-04-01T01:01");
		return dt.toString();
	}
	
	public static String getLastReset() {
		DateTime dt = DateTime.now();
		return dt.toString();
	}
	
	public static String getVersion() {
		final String uri = "https://dataverse.scholarsportal.info/api/info/version";	     
	    RestTemplate restTemplate = new RestTemplate();
	    ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
	    String body = response.getBody();
	    ObjectMapper mapper = new ObjectMapper();
	    JsonNode root;
		try {
			root = mapper.readTree(body);
			JsonNode data = root.path("data");
			JsonNode version = data.get("version");
			System.out.println("Version: "+version.asText());
			return version.asText();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "v4.19-SP";
	}
}
