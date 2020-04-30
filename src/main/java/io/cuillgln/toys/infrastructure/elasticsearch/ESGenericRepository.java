
package io.cuillgln.toys.infrastructure.elasticsearch;

import java.io.IOException;

import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ESGenericRepository<T> {

	public static final String INDEX_ALIAS_AUTH_LOGS = "idauth_logs";
	private int shardNumber = 3;

	private RestClient client;
	private ObjectMapper objectMapper;

	public ESGenericRepository(RestClient client, ObjectMapper objectMapper) {
		this.client = client;
		this.objectMapper = objectMapper;
	}

	public void createDocument(String doc) {
		Request createDocumentRequest = new Request("POST", "/" + INDEX_ALIAS_AUTH_LOGS + "/_doc/");
		performRequest(createDocumentRequest, doc);
	}

	void performRequest(Request request, Object object) {
		try {
			if (object instanceof String) {
				request.setJsonEntity((String) object);
			} else {
				request.setJsonEntity(objectMapper.writeValueAsString(object));
			}
			client.performRequest(request);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createLifecyclePolicy() {
		Request createLifecyclePolicy = new Request("PUT", "/_ilm/policy/" + INDEX_ALIAS_AUTH_LOGS + "_policy");
		performRequest(createLifecyclePolicy, indexLifecyclePolicy());
	}

	public void createIndexTemplate() {
		Request createTemplate = new Request("PUT", "/_template/" + INDEX_ALIAS_AUTH_LOGS + "_template");
		performRequest(createTemplate, indexTemplate());
	}

	public void createBootstrapIndex() {
		Request createIndex = new Request("PUT", "/" + indexName(1));
		performRequest(createIndex, index());
	}

	private String indexName(int number) {
		return String.format(INDEX_ALIAS_AUTH_LOGS + "-%06d", number);
	}

	public ObjectNode indexLifecyclePolicy() {
		ObjectNode root = objectMapper.createObjectNode();
		ObjectNode policy = root.putObject("policy");
		ObjectNode phases = policy.putObject("phases");
		ObjectNode hot = phases.putObject("hot");
		ObjectNode actions = hot.putObject("actions");
		ObjectNode rollover = actions.putObject("rollover");
		rollover.put("max_size", "30GB");
		rollover.put("max_age", "30d");

		ObjectNode delete = phases.putObject("delete");
		delete.put("min_age", "90d");
		ObjectNode delActions = delete.putObject("actions");
		delActions.putObject("delete");
		return root;
	}

	public ObjectNode indexTemplate() {
		ObjectNode root = objectMapper.createObjectNode();
		ArrayNode indexPatterns = root.putArray("index_patterns");
		indexPatterns.add(INDEX_ALIAS_AUTH_LOGS + "-*");
		settings(root.putObject("settings"));
		mappings(root.putObject("mappings"));
		return root;
	}

	public ObjectNode index() {
		ObjectNode root = objectMapper.createObjectNode();
		ObjectNode aliases = root.putObject("aliases");
		ObjectNode logs = aliases.putObject(INDEX_ALIAS_AUTH_LOGS);
		logs.put("is_write_index", true);
		return root;
	}

	ObjectNode settings(ObjectNode settings) {
		settings.put("number_of_shards", shardNumber);
		settings.put("index.lifecycle.name", INDEX_ALIAS_AUTH_LOGS + "_policy");
		settings.put("index.lifecycle.rollover_alias", INDEX_ALIAS_AUTH_LOGS);
		return settings;
	}

	ObjectNode mappings(ObjectNode mappings) {
		ObjectNode properties = mappings.putObject("properties");
		ObjectNode timestamp = properties.putObject("timestamp");
		timestamp.put("type", "date");
		ObjectNode appId = properties.putObject("applicationId");
		appId.put("type", "keyword");
		ObjectNode appName = properties.putObject("applicationName");
		appName.put("type", "keyword");
		ObjectNode partyId = properties.putObject("partyId");
		partyId.put("type", "keyword");
		ObjectNode partyName = properties.putObject("partyName");
		partyName.put("type", "keyword");
		ObjectNode authMode = properties.putObject("authMode");
		authMode.put("type", "keyword");
		ObjectNode requestTimestamp = properties.putObject("requestTimestamp");
		requestTimestamp.put("type", "keyword");
		ObjectNode businessSerialNumber = properties.putObject("businessSerialNumber");
		businessSerialNumber.put("type", "keyword");
		ObjectNode photoData = properties.putObject("photoData");
		photoData.put("type", "boolean");
		ObjectNode responseTimestamp = properties.putObject("responseTimestamp");
		responseTimestamp.put("type", "keyword");
		ObjectNode success = properties.putObject("success");
		success.put("type", "boolean");
		ObjectNode errorDesc = properties.putObject("errorDesc");
		errorDesc.put("type", "text");
		ObjectNode authResult = properties.putObject("authResult");
		authResult.put("type", "keyword");
		idAuthData(properties.putObject("idAuthData"));
		authRetainData(properties.putObject("authRetainData"));
		authResultRetainData(properties.putObject("authResultRetainData"));
		thirdService(properties.putObject("thirdService"));
		return mappings;
	}

	ObjectNode idAuthData(ObjectNode idAuthData) {
		ObjectNode idAuthDataProps = idAuthData.putObject("properties");
		ObjectNode idType = idAuthDataProps.putObject("idType");
		idType.put("type", "keyword");
		ObjectNode nation = idAuthDataProps.putObject("nation");
		nation.put("type", "keyword");
		ObjectNode idNumber = idAuthDataProps.putObject("idNumber");
		idNumber.put("type", "keyword");
		ObjectNode expiryDate = idAuthDataProps.putObject("expiryDate");
		expiryDate.put("type", "keyword");
		ObjectNode name = idAuthDataProps.putObject("name");
		name.put("type", "keyword");
		ObjectNode sex = idAuthDataProps.putObject("sex");
		sex.put("type", "keyword");
		ObjectNode birthDate = idAuthDataProps.putObject("birthDate");
		birthDate.put("type", "keyword");
		ObjectNode effectDate = idAuthDataProps.putObject("effectDate");
		effectDate.put("type", "keyword");
		return idAuthData;
	}

	ObjectNode authRetainData(ObjectNode authRetainData) {
		ObjectNode authRetainDataProps = authRetainData.putObject("properties");
		ObjectNode venderName = authRetainDataProps.putObject("venderName");
		venderName.put("type", "text");
		ObjectNode venderIp = authRetainDataProps.putObject("venderIp");
		venderIp.put("type", "keyword");
		ObjectNode dealDate = authRetainDataProps.putObject("dealDate");
		dealDate.put("type", "keyword");
		ObjectNode dealSite = authRetainDataProps.putObject("dealSite");
		dealSite.put("type", "keyword");
		ObjectNode businessType = authRetainDataProps.putObject("businessType");
		businessType.put("type", "keyword");
		ObjectNode agentType = authRetainDataProps.putObject("agentType");
		agentType.put("type", "keyword");
		return authRetainData;
	}

	private ObjectNode authResultRetainData(ObjectNode authResultRetainData) {
		ObjectNode authResultRetainDataProps = authResultRetainData.putObject("properties");
		ObjectNode rxfs = authResultRetainDataProps.putObject("rxfs");
		rxfs.put("type", "keyword");
		return authResultRetainData;
	}

	ObjectNode thirdService(ObjectNode thirdService) {
		ObjectNode thirdServiceProps = thirdService.putObject("properties");
		ObjectNode serviceId = thirdServiceProps.putObject("serviceId");
		serviceId.put("type", "keyword");
		ObjectNode serviceName = thirdServiceProps.putObject("serviceName");
		serviceName.put("type", "keyword");
		ObjectNode request = thirdServiceProps.putObject("request");
		request.put("type", "keyword");
		ObjectNode response = thirdServiceProps.putObject("response");
		response.put("type", "keyword");
		return thirdService;
	}
}
