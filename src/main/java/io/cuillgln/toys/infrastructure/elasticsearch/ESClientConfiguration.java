
package io.cuillgln.toys.infrastructure.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;

public class ESClientConfiguration {

	private ESConfigurationProperties properties;

	public RestClient esRestClient() {
		Node[] nodes = new Node[properties.getUris().size()];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new Node(HttpHost.create(properties.getUris().get(i)));
		}
		return RestClient.builder(nodes).build();
	}

}
