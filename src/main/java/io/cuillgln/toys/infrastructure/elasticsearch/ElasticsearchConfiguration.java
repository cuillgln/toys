
package io.cuillgln.toys.infrastructure.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(RestProperties.class)
@Configuration
public class ElasticsearchConfiguration {

	private final RestProperties properties;

	public ElasticsearchConfiguration(RestProperties properties) {
		this.properties = properties;
	}

	@Bean
	public RestClient esRestClient() {
		Node[] nodes = new Node[properties.getUris().size()];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new Node(HttpHost.create(properties.getUris().get(i)));
		}
		return RestClient.builder(nodes).build();
	}

}
