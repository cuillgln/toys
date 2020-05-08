
package io.cuillgln.toys.yaml;

import org.yaml.snakeyaml.Yaml;

public class SnakeyamlMain {

	public static void main(String[] args) {
		Yaml yaml = new Yaml();
		Iterable<Object> config = yaml.load(SnakeyamlMain.class.getResourceAsStream("/application.yml"));
		config.iterator().forEachRemaining(System.out::println);

	}
}
