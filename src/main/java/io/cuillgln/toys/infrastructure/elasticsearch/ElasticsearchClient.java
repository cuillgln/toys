
package io.cuillgln.toys.infrastructure.elasticsearch;

import io.cuillgln.toys.infrastructure.elasticsearch.SearchHits.Hit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ElasticsearchClient {

	public static final int DEFAULT_QUERY_SIZE = 1000;

	public static final JsonPointer RESULT_HITS = JsonPointer.valueOf("/hits");
	public static final JsonPointer RESULT_SCROLL_ID = JsonPointer.valueOf("/_scroll_id");
	public static final JsonPointer RESULT_COUNT = JsonPointer.valueOf("/count");

	private Logger log = LoggerFactory.getLogger(getClass());

	private ObjectMapper objectMapper;
	private RestClient client;

	public ElasticsearchClient(ObjectMapper objectMapper, List<String> uris) {
		this.objectMapper = objectMapper;
		HttpHost[] nodes = parseNodesUris(uris);
		if (nodes.length == 0) {
			throw new IllegalStateException("elasticsearch server urls is null");
		}
		this.client = RestClient.builder(nodes).build();
	}

	private HttpHost[] parseNodesUris(List<String> uris) {
		Set<HttpHost> hosts = new HashSet<HttpHost>();
		for (String sn : uris) {
			String[] split = sn.split(":");
			if (split.length == 2) {
				HttpHost host = new HttpHost(split[0], Integer.parseInt(split[1]), "http");
				hosts.add(host);
			}
		}
		HttpHost[] nodes = new HttpHost[hosts.size()];
		hosts.toArray(nodes);
		return nodes;
	}

	/* search API */
	/**
	 * 搜索
	 * @param indexNames
	 * @param must
	 * @param filter
	 * @param sort
	 * @param from
	 * @param size
	 * @param clazz
	 * @return
	 */
	public <S> List<S> search(String indexNames, JsonNode must, JsonNode filter, JsonNode sort, int from, int size,
			Class<S> clazz) {
		try {
			Request createindexRequest = new Request("GET", "/" + indexNames + "/_search");
			JsonNode query = boolQuery(must, filter, null, null);
			Response resp = performRequest(createindexRequest, createSearchRequestBody(query, sort, null, from, size));
			List<S> result = new ArrayList<>();
			JavaType valueType = objectMapper.getTypeFactory().constructParametricType(SearchHits.class, clazz);
			JsonNode hitsNode = objectMapper.readTree(resp.getEntity().getContent()).at(RESULT_HITS);
			SearchHits<S> hits = objectMapper.readValue(hitsNode.traverse(), valueType);
			for (Hit<S> hit : hits.getHits()) {
				result.add(hit.getSource());
			}
			return result;
		} catch (IOException e) {
			throw new ElasticsearchException(e);
		}
	}

	/**
	 * 统计总数
	 * @param indexNames
	 * @param must
	 * @param filter
	 * @return
	 */
	public long count(String indexNames, JsonNode must, JsonNode filter) {
		Request countRequest = new Request("GET", "/" + indexNames + "/_count");
		ObjectNode root = objectMapper.createObjectNode();
		root.set("query", boolQuery(must, filter, null, null));
		try {
			Response resp = performRequest(countRequest, root);
			return objectMapper.readTree(resp.getEntity().getContent()).at(RESULT_COUNT).asLong();
		} catch (IOException e) {
			throw new ElasticsearchException(e);
		}
	}

	/**
	 * 用Scroll API查询所有记录
	 * @param indexNames
	 * @param must
	 * @param filter
	 * @param sort
	 * @param clazz
	 * @return
	 */
	public <S> List<S> searchAll(String indexNames, JsonNode must, JsonNode filter, JsonNode sort, Class<S> clazz) {
		try {
			Request scrollRequest = new Request("GET", "/" + indexNames + "/_search?scroll=1m");
			JsonNode query = boolQuery(must, filter, null, null);
			Response resp = performRequest(scrollRequest, createSearchRequestBody(query, sort, null, 0, 100));
			List<S> result = new ArrayList<>();
			scrollSearch(resp, clazz, result);
			return result;
		} catch (IOException e) {
			throw new ElasticsearchException(e);
		}
	}

	private <S> void scrollSearch(Response resp, Class<S> clazz, List<S> result) {
		try {
			JavaType valueType = objectMapper.getTypeFactory().constructParametricType(SearchHits.class, clazz);
			JsonNode root = objectMapper.readTree(resp.getEntity().getContent());
			String scrollId = root.at(RESULT_SCROLL_ID).asText();
			JsonNode hitsNode = root.at(RESULT_HITS);
			SearchHits<S> hits = objectMapper.readValue(hitsNode.traverse(), valueType);
			if (!hits.getHits().isEmpty()) {
				for (Hit<S> hit : hits.getHits()) {
					result.add(hit.getSource());
				}
				Request searchRequest = new Request("POST", "/_search/scroll");
				ObjectNode requestRoot = objectMapper.createObjectNode();
				requestRoot.put("scroll", "1m"); // 1分钟
				requestRoot.put("scroll_id", scrollId);
				scrollSearch(performRequest(searchRequest, requestRoot), clazz, result); // 编历查询
			} else {
				// clear scroll
				Request clearScrollRequest = new Request("DELETE", "/_search/scroll");
				ObjectNode requestRoot = objectMapper.createObjectNode();
				requestRoot.put("scroll_id", scrollId);
				performRequest(clearScrollRequest, requestRoot);
			}
		} catch (Exception e) {
			throw new ElasticsearchException(e);
		}
	}

	/* document API*/
	/**
	 * 索引文档
	 * @param indexName
	 * @param id
	 * @param doc
	 */
	public void index(String indexName, String id, Object doc) {
		try {
			Request putDocRequest = new Request("PUT", "/" + indexName + "/_doc/" + id);
			Response resp = performRequest(putDocRequest, doc);
		} catch (IOException e) {
			throw new ElasticsearchException(e);
		}
	}

	/**
	 * 批量索引文档
	 * @param indexName
	 * @param ids
	 * @param docs
	 */
	public <T> void bulkIndex(String indexName, List<String> ids, Collection<T> docs) {
		try {
			Request bulkRequest = new Request("POST", "/_bulk/");
			ObjectNode indexAction = objectMapper.createObjectNode();
			ObjectNode action = indexAction.putObject("index").put("_index", indexName);
			StringBuffer entity = new StringBuffer();
			int i = 0;
			for (T doc : docs) {
				action.put("_id", ids.get(i++));
				entity.append(objectMapper.writeValueAsString(indexAction)).append("\r\n")
						.append(objectMapper.writeValueAsString(doc)).append("\r\n");
			}
			performRequest(bulkRequest, entity.toString());
		} catch (IOException e) {
			throw new ElasticsearchException(e);
		}
	}

	/**
	 * 更新文档
	 * @param indexName
	 * @param id
	 */
	public void update(String indexName, String id) {
		Request updateDocRequest = new Request("POST", "/" + indexName + "/_update/" + id);
		// TODO
	}

	/**
	 * 按id获取文档
	 * @param indexName
	 * @param id
	 * @param clazz
	 * @return
	 */
	public <T> T get(String indexName, String id, Class<T> clazz) {
		try {
			Request getDocRequest = new Request("GET", "/" + indexName + "/_source/" + id);
			Response resp = performRequest(getDocRequest);
			return objectMapper.readValue(resp.getEntity().getContent(), clazz);
		} catch (IOException e) {
			throw new ElasticsearchException(e);
		}
	}

	/**
	 * 检查文档是否存在
	 * @param indexName
	 * @param id
	 * @return
	 */
	public boolean documentExists(String indexName, String id) {
		try {
			Request headDocRequest = new Request("HEAD", "/" + indexName + "/_doc/" + id);
			Response resp = performRequest(headDocRequest);
			if (resp.getStatusLine().getStatusCode() == 200) {
				return true;
			}
			return false;
		} catch (IOException e) {
			throw new ElasticsearchException(e);
		}
	}

	/**
	 * 删除文档
	 * @param indexName
	 * @param id
	 */
	public void delete(String indexName, String id) {
		try {
			Request deleteDocRequest = new Request("DELETE", "/" + indexName + "/_doc/" + id);
			performRequest(deleteDocRequest);
		} catch (IOException e) {
			throw new ElasticsearchException(e);
		}
	}

	/* index API */
	/**
	 * 创建索引
	 * @param indexName
	 * @param mappings
	 * @param settings
	 * @param aliases
	 */
	public void createIndex(String indexName, JsonNode mappings, JsonNode settings, JsonNode aliases) {
		try {
			Request createIndexRequest = new Request("PUT", "/" + indexName);
			performRequest(createIndexRequest, createIndexRequestBody(mappings, settings, aliases));
		} catch (IOException e) {
			throw new ElasticsearchException(e);
		}
	}

	/**
	 * 检查索引是否存在
	 * @param indexName
	 * @return
	 */
	public boolean indexExists(String indexName) {
		try {
			Request indexExistsRequest = new Request("HEAD", "/" + indexName);
			Response resp = performRequest(indexExistsRequest);
			if (resp.getStatusLine().getStatusCode() == 200) {
				return true;
			}
			return false;
		} catch (IOException e) {
			throw new ElasticsearchException(e);
		}
	}

	/**
	 * 更新索引mappings
	 * @param indexName
	 * @param mappings
	 */
	public void putMapping(String indexName, JsonNode mappings) {
		try {
			Request putMappingRequest = new Request("PUT", "/" + indexName + "/_mapping");
			performRequest(putMappingRequest, mappings);
		} catch (IOException e) {
			throw new ElasticsearchException(e);
		}
	}

	/* index template API */
	/**
	 * 新建或更新索引模板
	 * @param indexTemplateName
	 * @param indexPatterns
	 * @param mappings
	 * @param settings
	 * @param aliases
	 */
	public void putIndexTemplate(String indexTemplateName, String[] indexPatterns, JsonNode mappings,
			JsonNode settings, JsonNode aliases) {
		try {
			Request createTemplateRequest = new Request("PUT", "/_template/" + indexTemplateName);
			performRequest(createTemplateRequest,
					putIndexTemplateRequestBody(indexPatterns, mappings, settings, aliases));
		} catch (IOException e) {
			throw new ElasticsearchException(e);
		}
	}

	/**
	 * 检查索引模板是否存在
	 * @param indexTemplateName
	 * @return
	 */
	public boolean indexTemplateExists(String indexTemplateName) {
		try {
			Request templateExistsRequest = new Request("HEAD", "/_template/" + indexTemplateName);
			Response resp = performRequest(templateExistsRequest);
			if (resp.getStatusLine().getStatusCode() == 200) {
				return true;
			}
			return false;
		} catch (IOException e) {
			throw new ElasticsearchException(e);
		}
	}

	/* index lifecycle management API */
	/**
	 * 创建IML policy
	 * @param policyName
	 * @param phases
	 */
	public void createLifecyclePolicy(String policyName, Map<String, JsonNode> phases) {
		try {
			Request createPolicyRequest = new Request("PUT", "/_ilm/policy/" + policyName);
			performRequest(createPolicyRequest, createPolicyRequestBody(phases));
		} catch (IOException e) {
			throw new ElasticsearchException(e);
		}
	}

	/* http performRequest API */
	Response performRequest(Request request) throws IOException {
		return performRequest(request, null);
	}

	Response performRequest(Request request, Object requestBody) throws IOException {
		Map<String, String> empty = Collections.emptyMap();
		return performRequest(request, empty, requestBody);
	}

	Response performRequest(Request request, Map<String, String> parameters, Object requestBody) throws IOException {
		for (String key : parameters.keySet()) {
			request.addParameter(key, parameters.get(key));
		}

		if (requestBody != null) {
			if (requestBody instanceof HttpEntity) {
				request.setEntity((HttpEntity) requestBody);
			} else if (requestBody instanceof String) {
				request.setJsonEntity((String) requestBody);
			} else {
				log.debug(objectMapper.writeValueAsString(requestBody));
				request.setJsonEntity(objectMapper.writeValueAsString(requestBody));
			}
		}
		return client.performRequest(request);
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

	private JsonNode putIndexTemplateRequestBody(String[] indexPatterns, JsonNode mappings, JsonNode settings,
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

	private JsonNode createSearchRequestBody(JsonNode query, JsonNode sort, JsonNode searchAfter, int from, int size) {
		ObjectNode root = objectMapper.createObjectNode();
		root.put("from", from);
		if (size >= 0) {
			root.put("size", size);
		}
		if (searchAfter != null) {
			root.set("search_after", searchAfter);
		}
		if (sort != null) {
			root.set("sort", sort);
		}
		root.set("query", query);
		return root;
	}

	private JsonNode boolQuery(JsonNode must, JsonNode filter, JsonNode should, JsonNode mustnot) {
		ObjectNode query = objectMapper.createObjectNode();
		ObjectNode boolNode = query.putObject("bool");
		if (must != null) {
			boolNode.set("must", must);
		} else {
			boolNode.putObject("must").putObject("match_all");
		}
		if (filter != null) {
			boolNode.set("filter", filter);
		}
		if (should != null) {
			boolNode.set("should", should);
		}
		if (mustnot != null) {
			boolNode.set("mustnot", mustnot);
		}
		return query;
	}

}
