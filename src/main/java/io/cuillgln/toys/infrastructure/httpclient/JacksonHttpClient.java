
package io.cuillgln.toys.infrastructure.httpclient;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonHttpClient {

	private CloseableHttpClient httpclient;
	private ObjectMapper objectMapper;
	private MeterRegistry meterRegistry;

	public JacksonHttpClient(CloseableHttpClient httpclient, ObjectMapper objectMapper, MeterRegistry meterRegistry) {
		this.httpclient = httpclient;
		this.objectMapper = objectMapper;
		this.meterRegistry = meterRegistry;
	}

	public JsonNode postForObject(String url, JsonNode root) throws IOException {
		Timer.Sample sample = Timer.start(meterRegistry);
		try {
			HttpPost request = new HttpPost(url);
			ByteArrayEntity entity = new ByteArrayEntity(objectMapper.writeValueAsBytes(root),
							ContentType.create("application/json", StandardCharsets.UTF_8));
			request.setEntity(entity);
			return httpclient.execute(request, new JsonResponseHandler());
		} finally {
			sample.stop(meterRegistry.timer("http.client.request", "uri", url));
		}
	}

	class JsonResponseHandler implements ResponseHandler<JsonNode> {

		@Override
		public JsonNode handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
			StatusLine statusLine = response.getStatusLine();
			HttpEntity entity = response.getEntity();
			if (statusLine.getStatusCode() >= 300) {
				throw new HttpResponseException(statusLine.getStatusCode(),
								statusLine.getReasonPhrase());
			}
			if (entity == null) {
				throw new ClientProtocolException("Response contains no content");
			}
			return objectMapper.readTree(entity.getContent());
		}
	}
}
