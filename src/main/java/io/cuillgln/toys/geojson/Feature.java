
package io.cuillgln.toys.geojson;

import java.util.Map;

public class Feature {

	private final String type = "Feature";

	private Geometry geometry;

	private Map<String, Object> properties;

	private Object id;

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

}
