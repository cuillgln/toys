
package io.cuillgln.toys.infrastructure.elasticsearch;

import io.cuillgln.toys.domain.model.Repository;

import java.io.IOException;

import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class ElasticsearchRepository<T, ID> implements Repository<T, ID> {

	private RestClient client;
	private ObjectMapper objectMapper;
	private String indexName;

	public ElasticsearchRepository(RestClient client, ObjectMapper objectMapper, String indexName) {
		this.client = client;
		this.objectMapper = objectMapper;
		this.indexName = indexName;
	}

	public T save(T doc) {
		try {
			Request createDocumentRequest = new Request("POST", "/" + indexName + "/_doc/");
			performRequest(createDocumentRequest, doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}

	protected final Response performRequest(Request request, Object object) throws IOException {
		if (object instanceof String) {
			request.setJsonEntity((String) object);
		} else {
			request.setJsonEntity(objectMapper.writeValueAsString(object));
		}
		return client.performRequest(request);
	}

	public void createLifecyclePolicy() {
		try {
			Request createLifecyclePolicy = new Request("PUT", "/_ilm/policy/" + indexName + "_policy");
			performRequest(createLifecyclePolicy, indexLifecyclePolicy());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void createIndexTemplate() {
		try {
			Request createTemplate = new Request("PUT", "/_template/" + indexName + "_template");
			performRequest(createTemplate, indexTemplate());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void createBootstrapIndex() {
		try {
			Request createIndex = new Request("PUT", "/" + indexName + "-000001");
			performRequest(createIndex, index());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	ObjectNode indexLifecyclePolicy() {
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

	ObjectNode indexTemplate() {
		ObjectNode root = objectMapper.createObjectNode();
		ArrayNode indexPatterns = root.putArray("index_patterns");
		indexPatterns.add(indexName + "-*");
		settings(root.putObject("settings"));
		mappings(root.putObject("mappings"));
		return root;
	}

	protected ObjectNode index() {
		ObjectNode root = objectMapper.createObjectNode();
		ObjectNode aliases = root.putObject("aliases");
		ObjectNode logs = aliases.putObject(indexName);
		logs.put("is_write_index", true);
		return root;
	}

	protected ObjectNode settings(ObjectNode settings) {
		settings.put("number_of_shards", 1);
		settings.put("index.lifecycle.name", indexName + "_policy");
		settings.put("index.lifecycle.rollover_alias", indexName);
		return settings;
	}

	protected abstract ObjectNode mappings(ObjectNode mappings);
}
