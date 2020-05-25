
package io.cuillgln.toys.infrastructure.elasticsearch;

import java.io.IOException;
import java.util.*;

import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ElasticsearchRestClient {

	public static final JsonPointer RESULT_HITS = JsonPointer.valueOf("/hits");

	private RestClient client;
	private ObjectMapper objectMapper;

	public ElasticsearchRestClient(ObjectMapper objectMapper, RestClient client) {
		this.objectMapper = objectMapper;
		this.client = client;
	}

	Response performRequest(Request request, Map<String, String> parameters, Object requestBody) throws IOException {
		parameters.forEach(request::addParameter);
		if (requestBody != null) {
			request.setJsonEntity(objectMapper.writeValueAsString(requestBody));
		}
		return client.performRequest(request);
	}

	Response performRequest(Request request) throws IOException {
		return performRequest(request, Collections.emptyMap(), null);
	}

	Response performRequest(Request request, Object requestBody) throws IOException {
		return performRequest(request, Collections.emptyMap(), requestBody);
	}

	/* search API */

	public <S> List<S> search(String indexNames, Map<String, String> parameters, JsonNode query, int from,
					int size, Class<S> clazz) throws IOException {
		Request createindexRequest = new Request("POST", "/" + indexNames + "/_search");
		Response resp = performRequest(createindexRequest, parameters, createSearchRequestBody(query, from, size));
		List<S> result = new ArrayList<>();
		JavaType valueType = objectMapper.getTypeFactory().constructParametricType(SearchHits.class, clazz);
		JsonNode hitsNode = objectMapper.readTree(resp.getEntity().getContent()).at(RESULT_HITS);
		SearchHits<S> hits = objectMapper.readValue(hitsNode.traverse(), valueType);
		hits.getHits().stream().map(SearchHits.Hit::getSource).forEach(result::add);
		return result;
	}

	public <S> List<S> filter(String indexNames, Map<String, String> parameters, JsonNode filter, int from,
					int size, Class<S> clazz) throws IOException {
		Request createindexRequest = new Request("POST", "/" + indexNames + "/_search");
		Response resp = performRequest(createindexRequest, parameters, createFilterRequestBody(filter, from, size));
		List<S> result = new ArrayList<>();
		JavaType valueType = objectMapper.getTypeFactory().constructParametricType(SearchHits.class, clazz);
		JsonNode hitsNode = objectMapper.readTree(resp.getEntity().getContent()).at(RESULT_HITS);
		SearchHits<S> hits = objectMapper.readValue(hitsNode.traverse(), valueType);
		hits.getHits().stream().map(SearchHits.Hit::getSource).forEach(result::add);
		return result;
	}

	public <S> List<S> filter(String indexNames, JsonNode filter, Class<S> clazz) throws IOException {
		return filter(indexNames, Collections.emptyMap(), filter, 0, 0, clazz);
	}

	/* document API */

	public void save(String indexName, Object document) {
		try {
			Request createindexRequest = new Request("POST", "/" + indexName + "/_doc/");
			performRequest(createindexRequest, document);
		} catch (IOException e) {
			throw new ElasticsearchException(e);
		}
	}

	public <T> void saveAll(String indexName, Collection<T> docs) {
		for (T doc : docs) {
			save(indexName, doc);
		}
	}

	/* index API */

	public void createBootstrapIndex(String indexNameAlias) {
		String indexName = indexNameAlias + "-000001";
		ObjectNode aliasesNode = objectMapper.createObjectNode();
		aliasesNode.putObject(indexNameAlias).put("is_write_index", true);
		createIndex(indexName, null, null, aliasesNode);
	}

	public void createIndex(String indexName, JsonNode mappings, JsonNode settings, JsonNode aliases) {
		try {
			Request createIndexRequest = new Request("PUT", "/" + indexName);
			performRequest(createIndexRequest, createIndexRequestBody(mappings, settings, aliases));
		} catch (IOException e) {
			throw new ElasticsearchException(e);
		}
	}

	public boolean indexExists(String indexName) {
		try {
			Request indexExistsRequest = new Request("HEAD", "/" + indexName);
			Response resp = performRequest(indexExistsRequest);
			if (resp.getStatusLine().getStatusCode() == 200) {
				return true;
			}
		} catch (IOException e) {
			throw new ElasticsearchException(e);
		}
		return false;
	}

	public void updateIndexMapping(String indexName, JsonNode mappings) {
		try {
			Request putMappingRequest = new Request("PUT", "/" + indexName + "/_mapping");
			performRequest(putMappingRequest, mappings);
		} catch (IOException e) {
			throw new ElasticsearchException(e);
		}
	}

	/*index  template API*/

	public void createIndexTemplate(String indexTemplateName, String[] indexPatterns, JsonNode mappings,
					JsonNode settings, JsonNode aliases) {
		try {
			Request createTemplateRequest = new Request("PUT", "/_template/" + indexTemplateName);
			performRequest(createTemplateRequest,
							createIndexTemplateRequestBody(indexPatterns, mappings, settings, aliases));
		} catch (IOException e) {
			throw new ElasticsearchException(e);
		}
	}

	public void createIndexManagementPolicy(String policyName, Map<String, JsonNode> phases) {
		try {
			Request createPolicyRequest = new Request("PUT", "/_ilm/policy/" + policyName);
			performRequest(createPolicyRequest, createPolicyRequestBody(phases));
		} catch (IOException e) {
			throw new ElasticsearchException(e);
		}
	}

	public boolean indexTemplateExists(String indexTemplateName) {
		try {
			Request templateExistsRequest = new Request("HEAD", "/_template/" + indexTemplateName);
			Response resp = performRequest(templateExistsRequest);
			if (resp.getStatusLine().getStatusCode() == 200) {
				return true;
			}
		} catch (IOException e) {
			throw new ElasticsearchException(e);
		}
		return false;
	}

	private JsonNode createIndexRequestBody(JsonNode mappings, JsonNode settings, JsonNode aliases) {
		ObjectNode root = objectMapper.createObjectNode();
		if (mappings != null) {
			root.set("mappings", mappings);
		}
		if (settings != null) {
			root.set("settings", settings);
		}
		if (aliases != null) {
			root.set("aliases", aliases);
		}
		return root;
	}

	private JsonNode createIndexTemplateRequestBody(String[] indexPatterns, JsonNode mappings, JsonNode settings,
					JsonNode aliases) {
		ObjectNode root = objectMapper.createObjectNode();
		ArrayNode indexPatternsNode = root.putArray("index_patterns");
		for (String pattern : indexPatterns) {
			indexPatternsNode.add(pattern);
		}
		if (mappings != null) {
			root.set("mappings", mappings);
		}
		if (settings != null) {
			root.set("settings", settings);
		}
		if (aliases != null) {
			root.set("aliases", aliases);
		}
		return root;
	}

	private JsonNode createPolicyRequestBody(Map<String, JsonNode> phases) {
		ObjectNode root = objectMapper.createObjectNode();
		ObjectNode phasesNode = root.putObject("policy").putObject("phases");
		phasesNode.setAll(phases);
		return root;
	}

	private JsonNode createSearchRequestBody(JsonNode query, int from, int size) {
		ObjectNode root = objectMapper.createObjectNode();
		root.put("from", from);
		if (size > 0) {
			root.put("size", size);
		}
		root.set("query", query);
		return root;
	}

	private JsonNode createFilterRequestBody(JsonNode filter, int from, int size) {
		ObjectNode root = objectMapper.createObjectNode();
		root.put("from", from);
		if (size > 0) {
			root.put("size", size);
		}
		ObjectNode boolNode = root.putObject("query").putObject("bool");
		boolNode.putObject("must").putObject("match_all");
		boolNode.set("filter", filter);
		return root;
	}
}
