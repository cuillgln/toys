package top.cuillgln.toys.elasticsearch;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonNodeTest {

	@Test
	public void testParse() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		String json = "{\"aliases\":{\"auth_logs\":{\"writeIndex\" : false, \"writeIndex1\" : false}}}";
		JsonNode root = objectMapper.readTree(json);
		JsonNode authLogs = root.get("aliases").get("auth_logs");
		AuthLogs al = objectMapper.readValue(authLogs.traverse(), AuthLogs.class);
		System.out.println(al.isWriteIndex());
		
		System.out.println(objectMapper.writeValueAsString(authLogs));
	}
}
